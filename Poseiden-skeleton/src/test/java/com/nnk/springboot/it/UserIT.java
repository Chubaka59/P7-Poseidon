package com.nnk.springboot.it;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.repositories.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
public class UserIT {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserRepository userRepository;

    @Test
    public void homeTest() throws Exception {
        mockMvc.perform(get("/user/list"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(view().name("user/list"))
                .andExpect(model().attributeExists("users"));
    }

    @Test
    public void addUserFormTest() throws Exception {
        mockMvc.perform(get("/user/add"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(view().name("user/add"));
    }

    @Test
    public void addUserTest() throws Exception {
        // GIVEN
        int initialCount = userRepository.findAll().size();

        // WHEN
        mockMvc.perform(post("/user/validate")
                        .param("username", "test")
                        .param("password", "Abcd123*")
                        .param("fullname", "test")
                        .param("role", "test")
                        .with(csrf())
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/user/list"));

        Assertions.assertThat(userRepository.findAll().size()).isEqualTo(initialCount + 1);
    }

    @Test
    public void addUserWhenErrorInTheFormTest() throws Exception {
        int initialCount = userRepository.findAll().size();

        mockMvc.perform(post("/user/validate")
                        .param("password", "test")
                        .param("fullname", "test")
                        .param("role", "test")
                        .with(csrf())
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(view().name("user/add"));

        assertEquals(initialCount, userRepository.findAll().size());
    }

    @Test
    public void addUserWhenUserAlreadyExist() throws Exception {
        int initialCount = userRepository.findAll().size();

        mockMvc.perform(post("/user/validate")
                .param("username", "testUsername")
                .param("password", "test")
                .param("fullname", "test")
                .param("role", "test")
                .with(csrf())
        )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(view().name("user/add"));

        assertEquals(initialCount, userRepository.findAll().size());
    }

    @Test
    public void showUpdateFormTest() throws Exception {
        mockMvc.perform(get("/user/update/1"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("user"))
                .andExpect(view().name("user/update"));
    }
    @Test
    public void updateUserTest() throws Exception {
        User userToUpdate = new User(null, "test", "Abcd123*", "test", "test");

        mockMvc.perform(post("/user/update/1")
                        .param("username", userToUpdate.getUsername())
                        .param("password", userToUpdate.getPassword())
                        .param("fullname", userToUpdate.getFullname())
                        .param("role", userToUpdate.getRole())
                        .with(csrf())
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/user/list"));

        User updatedUser = userRepository.findById(1).get();
        assertEquals(userToUpdate.getUsername(), updatedUser.getUsername());
        assertEquals(userToUpdate.getRole(), updatedUser.getRole());
        assertEquals(userToUpdate.getFullname(), updatedUser.getFullname());
    }

    @Test
    public void updateUserWhenErrorInTheFormTest() throws Exception {
        mockMvc.perform(post("/user/update/1")
                        .param("password", "testNotUpdated")
                        .param("fullname", "testNotUpdated")
                        .param("role", "testNotUpdated")
                        .with(csrf())
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(view().name("user/update"));

        User notUpdatedUser = userRepository.findById(1).get();
        assertNotEquals("testNotUpdated", notUpdatedUser.getUsername());
    }

    @Test
    public void updateUserWhenUserDoesNotExistTest() throws Exception {
        mockMvc.perform(post("/user/update/10")
                        .param("username", "testNotUpdated")
                        .param("password", "Abcd123*")
                        .param("fullname", "testNotUpdated")
                        .param("role", "testNotUpdated")
                        .with(csrf())
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(view().name("user/update"))
                .andExpect(model().attributeExists("error"));
    }

    @Test
    public void updateUserWhenUsernameIsAlreadyUsed() throws Exception {
        mockMvc.perform(post("/user/update/2")
                        .param("username", "testUsername")
                        .param("password", "Abcd123*")
                        .param("fullname", "testFullname")
                        .param("role", "testRole")
                        .with(csrf())
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(view().name("user/update"))
                .andExpect(model().attributeExists("error"));

        User notUpdatedUser = userRepository.findById(2).orElseThrow();
        assertNotEquals("testUsername", notUpdatedUser.getUsername());
    }

    @Test
    public void deleteUserTest() throws Exception {
        int initialCount = userRepository.findAll().size();

        mockMvc.perform(get("/user/delete/1"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/user/list"));

        List<User> userList = userRepository.findAll();
        assertEquals(initialCount - 1, userList.size());
    }

    @Test
    public void deleteUserWhenUserIsNotFoundTest() throws Exception {
        int initialCount = userRepository.findAll().size();

        mockMvc.perform(get("/user/delete/3"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(view().name("/user/list"))
                .andExpect(model().attributeExists("error"))
                .andExpect(model().attributeExists("users"));

        List<User> userList = userRepository.findAll();
        assertEquals(initialCount, userList.size());
    }
}
