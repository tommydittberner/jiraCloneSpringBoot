package itf.todi.refinement.issue;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@TestPropertySource(
        locations = "classpath:application-it.properties"
)
//@WebMvcTest(controllers = {IssueController.class})
@AutoConfigureMockMvc
class IssueControllerIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private IssueRepository repository;

    //todo: beforeAll and afterAll seems smarter
    @AfterEach
    void teardown() {
        repository.deleteAll();
    }

    @Test
    void itShouldGetAllIssues() throws Exception {
        Issue i1 = createIssueForTesting("My first issue");
        Issue i2 = createIssueForTesting("A second issue");
        repository.saveAll(List.of(i1, i2));

        //this is the more verbose approach without all the static imports
        RequestBuilder request = MockMvcRequestBuilders.get("/api/issue");
        MvcResult result = mvc.perform(request).andReturn();

        assertThat(result
                .getResponse()
                .getContentAsString())
                .contains(objectMapper.writeValueAsString(List.of(i1, i2)));
    }

    @Test
    void itShouldAddANewIssue() throws Exception {
        Issue issue = createIssueForTesting("My first issue");

        mvc.perform(post("/api/issue")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(issue)))
                .andExpect(status().isOk());

        List<Issue> issues = repository.findAll();

        assertThat(issues)
                .usingElementComparatorIgnoringFields("id")
                .contains(issue);
    }

    @Test
    @Disabled
    void updateIssueWithId() {
    }

    @Test
    @Disabled
    void deleteIssueWithId() {
    }

    private Issue createIssueForTesting(String title) {
        return new Issue(
                title,
                "default desc",
                3,
                IssueStatus.OPEN,
                0,
                IssuePriority.NORMAL,
                IssueType.TASK
        );
    }
}