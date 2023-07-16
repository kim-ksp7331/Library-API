package ksp7331.practice.libraryAPI.library.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import ksp7331.practice.libraryAPI.library.service.LibraryService;
import ksp7331.practice.libraryAPI.util.UriCreator;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartHttpServletRequest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LibraryController.class)
@MockBean(JpaMetamodelMappingContext.class)
class LibraryControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private LibraryService libraryService;
    @Test
    void postLibrary() throws Exception {
        // given
        Map<String, String> post = Map.of("name", "newLib");
        String content = new ObjectMapper().writeValueAsString(post);
        long id = 1L;
        given(libraryService.createLibrary(Mockito.anyString())).willReturn(id);
        String url = "/libraries";

        // when
        ResultActions actions = mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content));
        // then
        actions
                .andExpect(status().isCreated())
                .andExpect(header().string("location", UriCreator.createUri(url, id).toString()));
    }
}