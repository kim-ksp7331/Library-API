package ksp7331.practice.libraryAPI.library.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import ksp7331.practice.libraryAPI.config.DbTestInitializer;
import ksp7331.practice.libraryAPI.library.infrastructure.entity.LibraryEntity;
import ksp7331.practice.libraryAPI.library.infrastructure.LibraryJpaRepository;
import ksp7331.practice.libraryAPI.IntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

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
    private LibraryJpaRepository libraryRepository;
    @Autowired
    private DbTestInitializer dbTestInitializer;
    @Autowired
    private ObjectMapper objectMapper;

    @DisplayName("도서관 등록")
    @Test
    void postLibrary() throws Exception {
        // given
        String name = "newLib";
        Map<String, String> post = Map.of("name", name);
        String content = objectMapper.writeValueAsString(post);
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
        LibraryEntity library = libraryRepository.findById(id).get();
        assertThat(library).isNotNull();
        assertThat(library.getName()).isEqualTo(name);
    }
    @DisplayName("도서관 조회")
    @Test
    void getLibraries() throws Exception {
        //given
        String url = "/libraries";
        List<LibraryEntity> libraries = dbTestInitializer.getLibraries();

        // when
        ResultActions actions = mockMvc.perform(
                get(url)
                        .accept(MediaType.APPLICATION_JSON)
        );
        // then
        actions.andExpect(status().isOk());
        for (int i = 0; i < libraries.size(); i++) {
            actions.andExpect(jsonPath("$[%d].name", i).value(libraries.get(i).getName()));
        }

    }
}
