package com.application.elerna;

import com.application.elerna.controller.v1.AuthenticationController;
import com.application.elerna.dto.request.SignUpRequest;
import com.application.elerna.dto.response.TokenResponse;
import com.application.elerna.service.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class SignUpTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    private AuthenticationService authenticationService;

    @InjectMocks
    private AuthenticationController authenticationController;

    @Test
    public void signUp_success() {

        ObjectMapper objectMapper = new ObjectMapper();

        when(authenticationService.signUp(any(SignUpRequest.class))).thenReturn(TokenResponse.builder()
                        .accessToken("access_token")
                        .refreshToken("refresh_token")
                        .resetToken("reset_token")
                        .userId(1L)
                        .uuid("~~~")
                .build());

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        SignUpRequest request = null;

        try {
            request = SignUpRequest.builder()
                    .address("Viet Tri")
                    .email("user09106@gmail.com")
                    .phone("31231")
                    .dateOfBirth(format.parse("2003-11-09"))
                    .firstName("Hieu")
                    .lastName("Nguyen")
                    .cardNumber("54641546453455453453453453133")
                    .username("user09106")
                    .password("user09106")
                    .systemRole("user")
                    .build();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        try {
            mockMvc.perform(MockMvcRequestBuilders
                    .post("/api/v1/auth/signup")
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .characterEncoding("UTF-8")
                    .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data.accessToken").value("access_token"))
                    .andExpect(jsonPath("$.data.refreshToken").value("refresh_token"))
                    .andExpect(jsonPath("$.data.resetToken").value("reset_token"))
                    .andDo(print());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

}

//"{\"address\": \"Viet Tri\", " +
//        "\"email\": \"user09107@gmail.com\", " +
//        "\"phone\": \"1453453453453\", " +
//        "\"dateOfBirth\": \"2003-11-09\", " +
//        "\"firstName\": \"Hieu\", " +
//        "\"lastName\": \"Nguyen\", " +
//        "\"cardNumber\": \"516544545601313\", " +
//        "\"username\": \"user09107\", " +
//        "\"password\": \"user09107\", " +
//        "\"systemRole\": \"user\"}"