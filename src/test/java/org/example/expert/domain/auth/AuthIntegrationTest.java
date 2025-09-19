package org.example.expert.domain.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.expert.domain.auth.dto.request.SigninRequest;
import org.example.expert.domain.auth.dto.request.SignupRequest;
import org.example.expert.domain.user.enums.UserRole;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class AuthIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void 회원가입과_로그인_후_ADMIN_인가를_통과하고_유저_정보를_확인한다() throws Exception {

        String adminEmail = "admin@admin.com";
        String adminPassword = "Admin123";
        String adminNickname = "admin";

        // 1. 회원가입
        SignupRequest signupRequest = new SignupRequest(adminEmail, adminPassword, adminNickname, UserRole.Authority.ADMIN);
        mockMvc.perform(post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signupRequest))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andReturn();

        // 2. 로그인
        SigninRequest signinRequest = new SigninRequest(adminEmail, adminPassword);
        MvcResult mvcResult = mockMvc.perform(post("/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signinRequest))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bearerToken").exists())
                .andReturn();

        String bearerToken = objectMapper
                .readTree(mvcResult.getResponse().getContentAsString())
                .path("bearerToken").asText(null);

        assertThat(bearerToken).isNotBlank();

        // 3. /test 엔드포인트 호출
        mockMvc.perform(get("/test").header(HttpHeaders.AUTHORIZATION, bearerToken))
                .andExpect(status().isOk());
    }

    @Test
    public void 회원가입과_로그인_후_ADMIN_인가_통과를_실패한다() throws Exception {

        String userEmail = "user@user.com";
        String  userPassword = "user123";
        String userNickname = "user";

        // 1. 회원가입
        SignupRequest signupRequest = new SignupRequest(userEmail, userPassword, userNickname, UserRole.Authority.USER);
        mockMvc.perform(post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signupRequest))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andReturn();

        // 2. 로그인
        SigninRequest signinRequest = new SigninRequest(userEmail, userPassword);
        MvcResult mvcResult = mockMvc.perform(post("/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signinRequest))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bearerToken").exists())
                .andReturn();

        String bearerToken = objectMapper
                .readTree(mvcResult.getResponse().getContentAsString())
                .path("bearerToken").asText(null);

        assertThat(bearerToken).isNotBlank();

        // 3. /test 엔드포인트 호출
        mockMvc.perform(get("/test").header(HttpHeaders.AUTHORIZATION, bearerToken))
                .andExpect(status().isForbidden());
    }
}