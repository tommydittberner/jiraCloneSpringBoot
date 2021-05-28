package itf.todi.refinement.issue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class IssueService {

    private final IssueRepository issueRepository;

    @Autowired
    public IssueService(IssueRepository issueRepository) {
        this.issueRepository = issueRepository;
    }

    public List<Issue> getIssues() {
        return issueRepository.findAll();
    }

    public Issue addIssue(Issue issue) {
        return issueRepository.save(issue);
    }

    @Transactional
    public Issue updateIssue(Long issueId, Issue issue) {
        Issue toUpdate = issueRepository.findById(issueId)
                .orElseThrow(() -> new IllegalStateException(
                        String.format("Student with id %d does not exist", issueId)
                ));

        //id does not change
        //status is only changeable from board right now
        toUpdate.setTitle(issue.getTitle());
        toUpdate.setDescription(issue.getDescription());
        toUpdate.setType(issue.getType());
        toUpdate.setPriority(issue.getPriority());
        toUpdate.setStorypoints(issue.getStorypoints());

        issueRepository.save(toUpdate);
        return toUpdate;
    }

    public void deleteIssue(Long issueId) {
        if (issueRepository.findById(issueId).isPresent()) {
            issueRepository.deleteById(issueId);
        }
    }
}
