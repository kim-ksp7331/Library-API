package ksp7331.practice.libraryAPI.library.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import ksp7331.practice.libraryAPI.library.dto.LibraryControllerDTO;
import ksp7331.practice.libraryAPI.library.mapper.LibraryMapper;
import ksp7331.practice.libraryAPI.library.service.LibraryService;
import ksp7331.practice.libraryAPI.util.UriCreator;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LibraryController.class)
@MockBean(JpaMetamodelMappingContext.class)
class LibraryControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private LibraryService libraryService;
    @MockBean
    private LibraryMapper libraryMapper;
    @Autowired
    private ObjectMapper objectMapper;
    @Test
    void postLibrary() throws Exception {
        // given
        String name = "newLib";
        Map<String, String> post = Map.of("name", name);
        String content = objectMapper.writeValueAsString(post);
        long id = 1L;
        BDDMockito.given(libraryService.createLibrary(Mockito.anyString())).willReturn(id);
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
    @Test
    void getLibraries() throws Exception {
        //given
        int repeat = 3;
        String libName = "newLib";
        List<LibraryControllerDTO.Response> libraries = LongStream.rangeClosed(1, repeat).mapToObj(i -> LibraryControllerDTO.Response.builder()
                .id(i)
                .name(libName + i)
                .build()).collect(Collectors.toList());
        BDDMockito.given(libraryMapper.ServiceDTOsToControllerDTOs(Mockito.anyList())).willReturn(libraries);
        String url = "/libraries";

        // when
        ResultActions actions = mockMvc.perform(
                get(url)
                        .accept(MediaType.APPLICATION_JSON)
        );
        // then
        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].name").value(everyItem(is(startsWith(libName)))));

    }
}