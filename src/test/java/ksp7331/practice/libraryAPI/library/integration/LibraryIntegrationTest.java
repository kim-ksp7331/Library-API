package ksp7331.practice.libraryAPI.library.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import ksp7331.practice.libraryAPI.library.entity.Library;
import ksp7331.practice.libraryAPI.library.repository.LibraryRepository;
import ksp7331.practice.libraryAPI.member.IntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.stream.LongStream;

import static org.assertj.core.api.Assertions.*;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class LibraryIntegrationTest extends IntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private LibraryRepository libraryRepository;

    @Test
    void postLibrary() throws Exception {
        // given
        String name = "newLib";
        Map<String, String> post = Map.of("name", name);
        String content = new ObjectMapper().writeValueAsString(post);
        String url = "/libraries";

        // when
        ResultActions actions = mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content));
        // then
        actions
                .andExpect(status().isCreated())
                .andExpect(header().string("location", is(startsWith(url))));

        long id = getId(url, actions);
        Library library = libraryRepository.findById(id).get();
        assertThat(library).isNotNull();
        assertThat(library.getName()).isEqualTo(name);
    }
    @Test
    void getLibraries() throws Exception {
        //given
        String url = "/libraries";

        // when
        ResultActions actions = mockMvc.perform(
                get(url)
                        .accept(MediaType.APPLICATION_JSON)
        );
        // then
        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("서울"))
                .andExpect(jsonPath("$[1].name").value("부산"));

    }
}
