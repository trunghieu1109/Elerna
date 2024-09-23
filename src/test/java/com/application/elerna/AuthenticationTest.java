package com.application.elerna;

import com.application.elerna.model.User;
import com.application.elerna.repository.UserRepository;

import org.junit.After;
import org.junit.jupiter.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

@DataJpaTest
@RunWith(SpringRunner.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class AuthenticationTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void signUpTest() {

        User user = User.builder()
                .firstName("Hieu")
                .lastName("Nguyen")
                .dateOfBirth(new Date())
                .email("hieu.nguyen@gmail.com")
                .username("hieu.nguyen")
                .password("password123")
                .address("trung vuong, viet tri")
                .cardNumber("default card number")
                .phone("030303033")
                .build();

        userRepository.save(user);

        Assertions.assertEquals(1L, userRepository.findAll().size());

    }

    @After
    public void clean() {
        userRepository.deleteAll();
    }

}
