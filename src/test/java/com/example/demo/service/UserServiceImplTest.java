package com.example.demo.service;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    private UserServiceImpl testInstance;

    private final String USERNAME = "user";
    private final String PASSWORD = "$2a$04$I9Q2sDc4QGGg5WNTLmsz0.fvGv3OjoZyj81PrSFyGOqMphqfS2qKu";

    @Before
    public void before() {
        testInstance = new UserServiceImpl(userRepository);

        User user = new User(USERNAME, PASSWORD);
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());
        when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.of(user));

    }

    @Test(expected = UsernameNotFoundException.class)
    public void loadUserByUsername_notFound() {
        testInstance.loadUserByUsername("otherUser");
    }

    @Test
    public void loadUserByUsername_success() {
        UserDetails result = testInstance.loadUserByUsername(USERNAME);
        assertEquals(result.getUsername(), USERNAME);
        assertEquals(result.getPassword(), PASSWORD);
        assertEquals(result.getAuthorities().size(), 2);
    }
}
