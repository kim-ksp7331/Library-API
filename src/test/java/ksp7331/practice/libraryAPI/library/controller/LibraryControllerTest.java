package ksp7331.practice.libraryAPI.library.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import ksp7331.practice.libraryAPI.library.domain.Library;
import ksp7331.practice.libraryAPI.library.dto.LibraryCreate;
import ksp7331.practice.libraryAPI.library.service.LibraryService;
import ksp7331.practice.libraryAPI.util.UriCreator;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import static org.hamcrest.Matchers.*;
import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LibraryController.class)
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureRestDocs
class LibraryControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private LibraryService libraryService;
    @Autowired
    private ObjectMapper objectMapper;
    @Test
    void postLibrary() throws Exception {
        // given
        String name = "newLib";
        Map<String, String> post = Map.of("name", name);
        String content = objectMapper.writeValueAsString(post);
        long id = 1L;
        BDDMockito.given(libraryService.createLibrary(Mockito.any(LibraryCreate.class))).willReturn(id);
        String url = "/libraries";

        // when
        ResultActions actions = mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content));
        // then
        actions
                .andExpect(status().isCreated())
                .andExpect(header().string("location", UriCreator.createUri(url, id).toString()))
                .andDo(document(
                        "post-library",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("name").type(JsonFieldType.STRING).description("도서관 이름")
                        ), responseHeaders(
                                headerWithName(HttpHeaders.LOCATION).description("도서관 리소스의 엔드포인트: /libraries/{library-id}")
                        )
                ));
    }
    @Test
    void getLibraries() throws Exception {
        //given
        int repeat = 3;
        String libName = "newLib";
        List<Library> libraries = LongStream.rangeClosed(1, repeat).mapToObj(i -> Library.builder()
                .id(i)
                .name(libName + i)
                .build()).collect(Collectors.toList());

        BDDMockito.given(libraryService.findLibraries()).willReturn(libraries);
        String url = "/libraries";

        // when
        ResultActions actions = mockMvc.perform(
                get(url)
                        .accept(MediaType.APPLICATION_JSON)
        );
        // then
        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].name").value(everyItem(is(startsWith(libName)))))
                .andDo(document(
                        "get-libraries",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("[].id").type(JsonFieldType.NUMBER).description("도서관 id"),
                                fieldWithPath("[].name").type(JsonFieldType.STRING).description("도서관 이름")
                        )
                ));

    }
}