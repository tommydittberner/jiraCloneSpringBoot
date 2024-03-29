package itf.todi.refinement;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import itf.todi.refinement.model.Issue;
import itf.todi.refinement.model.IssuePriority;
import itf.todi.refinement.repositories.IssueRepository;
import itf.todi.refinement.services.IssueServiceImpl;

import static itf.todi.refinement.TestUtil.createMockIssue;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class IssueServiceTest {

    @Mock
    private IssueRepository issueRepository;

    private IssueServiceImpl issueService;

    @BeforeEach
    void setUp() {
        issueService = new IssueServiceImpl(issueRepository);
    }

    @Test
    void canGetAllIssues() {
        issueService.getIssues();
        //just test that repository was called
        verify(issueRepository).findAll();
    }

    @Test
    void canAddIssues() {
        //prepare
        Issue newIssue = createMockIssue("TestIssue");
        issueService.addIssue(newIssue);
        ArgumentCaptor<Issue> issueArgumentCaptor = ArgumentCaptor.forClass(Issue.class);
        //verify save was called & capture saved object
        verify(issueRepository).save(issueArgumentCaptor.capture());
        //assert that captured object is correct
        assertThat(issueArgumentCaptor.getValue()).isEqualTo(newIssue);
    }

    @Test
    void canUpdateIssue() {
        Issue toUpdate = createMockIssue("WillBeUpdated");
        toUpdate.setId(1L);
        when(issueRepository.findById(1L)).thenReturn(java.util.Optional.of(toUpdate));

        Issue updated = createMockIssue("This has been updated");
        updated.setId(1L);
        updated.setPriority(IssuePriority.HIGH);
        updated.setStorypoints(13);
        issueService.updateIssue(1L, updated);

        ArgumentCaptor<Issue> issueArgumentCaptor = ArgumentCaptor.forClass(Issue.class);
        verify(issueRepository).save(issueArgumentCaptor.capture());
        assertThat(issueArgumentCaptor.getValue()).isEqualTo(updated);
    }
}
