package com.tardin.bookstoremanager.users.controller;

import com.tardin.bookstoremanager.users.builder.UserDTOBuilder;
import com.tardin.bookstoremanager.users.dto.MessageDTO;
import com.tardin.bookstoremanager.users.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static com.tardin.bookstoremanager.utils.JsonConversionUtils.asJsonString;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    private static final String USER_API_URL_PATH = "/api/v1/users";

    private MockMvc mockMvc;

    private UserDTOBuilder userDTOBuilder;

    @Mock
    private UserService service;

    @InjectMocks
    private UserController controller;

    @BeforeEach
    void setUp() {
        this.userDTOBuilder = UserDTOBuilder.builder().build();
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .setViewResolvers(((viewName, locale) -> new MappingJackson2JsonView()))
                .build();
    }

    @Test
    void whenDELETEIsCalledThenNoContentStatusShouldBeInformed() throws Exception {
        var expectedUserToDeleteDTO = userDTOBuilder.buildUserDTO();
        Long id = expectedUserToDeleteDTO.getId();

        doNothing().when(service).delete(anyLong());

        mockMvc.perform(delete(USER_API_URL_PATH + "/" + id)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void whenPOSTIsCalledThenCreatedStatusShouldBeInformed() throws Exception {
        var expectedCreatedUserDTO = userDTOBuilder.buildUserDTO();
        Long id = expectedCreatedUserDTO.getId();
        String username = expectedCreatedUserDTO.getUsername();
        var expectedMessage = String.format("User %s with ID %s successfully created", username, id);
        MessageDTO expectedCreatedMessageDTO = MessageDTO.builder().message(expectedMessage).build();

        when(service.create(expectedCreatedUserDTO)).thenReturn(expectedCreatedMessageDTO);

        mockMvc.perform(post(USER_API_URL_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(expectedCreatedUserDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message", is(expectedMessage)));
    }

    @Test
    void whenPOSTIsCalledWithoutRequiredFieldsThenBadRequestShouldBeReturned() throws Exception {
        var expectedCreatedUserDTO = userDTOBuilder.buildUserDTO();
        expectedCreatedUserDTO.setEmail(null);

        mockMvc.perform(post(USER_API_URL_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(expectedCreatedUserDTO)))
                .andExpect(status().isBadRequest());
    }
}
