package ksp7331.practice.libraryAPI.member;

import org.springframework.test.web.servlet.ResultActions;

public class IntegrationTest {
    protected long getId(String url, ResultActions actions) {
        return Long.parseLong(actions.andReturn().getResponse().getHeader("location").replace(url, "").substring(1));
    }
}
