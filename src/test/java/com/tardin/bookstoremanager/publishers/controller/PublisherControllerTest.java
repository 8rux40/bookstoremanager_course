package com.tardin.bookstoremanager.publishers.controller;

import com.tardin.bookstoremanager.publishers.builder.PublisherDTOBuilder;
import com.tardin.bookstoremanager.publishers.service.PublisherService;
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

import java.util.Collections;

import static com.tardin.bookstoremanager.utils.JsonConversionUtils.asJsonString;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class PublisherControllerTest {

    private final static String PUBLISHERS_API_URL_PATH = "/api/v1/publishers";

    private MockMvc mockMvc;

    @Mock
    private PublisherService service;

    @InjectMocks
    private PublisherController controller;

    private PublisherDTOBuilder publisherDTOBuilder;

    @BeforeEach
    void setUp() {
        publisherDTOBuilder = PublisherDTOBuilder.builder().build();
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .setViewResolvers((s, locale) -> new MappingJackson2JsonView())
                .build();
    }

    @Test
    void whenPOSTIsCalledThenCreatedStatusShouldBeInformed() throws Exception {
        var expectedCreatedPublisherDTO = publisherDTOBuilder.buildPublisherDTO();

        when(service.create(expectedCreatedPublisherDTO)).thenReturn(expectedCreatedPublisherDTO);

        mockMvc.perform(post(PUBLISHERS_API_URL_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(expectedCreatedPublisherDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(expectedCreatedPublisherDTO.getId().intValue())))
                .andExpect(jsonPath("$.name", is(expectedCreatedPublisherDTO.getName())))
                .andExpect(jsonPath("$.code", is(expectedCreatedPublisherDTO.getCode())));
    }

    @Test
    void whenPOSTIsCalledWithoutRequiredFieldsThenBadRequestStatusShouldBeInformed() throws Exception {
        var expectedCreatedPublisherDTO = publisherDTOBuilder.buildPublisherDTO();
        expectedCreatedPublisherDTO.setName(null);

        mockMvc.perform(post(PUBLISHERS_API_URL_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(expectedCreatedPublisherDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenGETWithValidIsCalledThenOkStatusShouldBeInformed() throws Exception {
        var expectedPublisherFoundDTO = publisherDTOBuilder.buildPublisherDTO();
        final var publisherFoundDTOId = expectedPublisherFoundDTO.getId();

        when(service.findById(publisherFoundDTOId)).thenReturn(expectedPublisherFoundDTO);

        mockMvc.perform(get(PUBLISHERS_API_URL_PATH + "/" + publisherFoundDTOId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(publisherFoundDTOId.intValue())))
                .andExpect(jsonPath("$.name", is(expectedPublisherFoundDTO.getName())))
                .andExpect(jsonPath("$.code", is(expectedPublisherFoundDTO.getCode())));
    }

    @Test
    void whenGETListIsCalledThenOkStatusShouldBeInformed() throws Exception {
        var expectedPublisherFoundDTO = publisherDTOBuilder.buildPublisherDTO();

        when(service.findAll()).thenReturn(Collections.singletonList(expectedPublisherFoundDTO));

        mockMvc.perform(get(PUBLISHERS_API_URL_PATH))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(expectedPublisherFoundDTO.getId().intValue())))
                .andExpect(jsonPath("$[0].name", is(expectedPublisherFoundDTO.getName())))
                .andExpect(jsonPath("$[0].code", is(expectedPublisherFoundDTO.getCode())));
    }

    @Test
    void whenDELETEIsCalledThenNoContentStatusShouldBeInformed() throws Exception {
        var expectedPublisherToDeleteDTO = publisherDTOBuilder.buildPublisherDTO();
//        when(service.delete(expectedPublisherToDeleteDTO.getId())).thenReturn();
        final var expectedPublisherToDeleteDTOId = expectedPublisherToDeleteDTO.getId();
        doNothing().when(service).delete(expectedPublisherToDeleteDTOId);

        mockMvc.perform(delete(PUBLISHERS_API_URL_PATH + "/" + expectedPublisherToDeleteDTOId))
                .andExpect(status().isNoContent());
    }
}
