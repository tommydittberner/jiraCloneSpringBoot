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
    public Issue updateIssue(
            Long issueId,
            IssueStatus status,
            IssuePriority priority,
            IssueType type,
            String title,
            String description,
            Integer storypoints
    ) {
        Issue issue = issueRepository.findById(issueId)
                .orElseThrow(() -> new IllegalStateException(
                        String.format("Student with id %d does not exist", issueId)
                ));

        if (status != null) {
            issue.setStatus(status);
        }

        if (priority != null) {
            issue.setPriority(priority);
        }

        if (type != null) {
            issue.setType(type);
        }

        if (title != null && title.length() > 0) {
            issue.setTitle(title);
        }

        if (description != null && description.length() > 0){
            issue.setDescription(description);
        }

        if (storypoints != null && storypoints > 0){
            issue.setStorypoints(storypoints);
        }

        issueRepository.save(issue);
        return issue;
    }

    public void deleteIssue(Long issueId) {
        if (issueRepository.findById(issueId).isPresent()) {
            issueRepository.deleteById(issueId);
        }
    }
}
