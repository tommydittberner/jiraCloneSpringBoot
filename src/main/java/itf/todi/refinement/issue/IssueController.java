package itf.todi.refinement.issue;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping(path = "api/issue")
@AllArgsConstructor
public class IssueController {

    private final IssueService issueService;

    @GetMapping
    public ResponseEntity<List<Issue>> getIssue() {
        return new ResponseEntity<>(issueService.getIssues(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Issue> addIssue(@RequestBody Issue issue) {
        return new ResponseEntity<>(issueService.addIssue(issue), HttpStatus.OK);
    }

    @PutMapping(path = "{issueId}")
    public ResponseEntity<Issue> updateIssueWithId(
            @PathVariable Long issueId,
            @RequestParam(required = false) IssueStatus status,
            @RequestParam(required = false) IssuePriority priority,
            @RequestParam(required = false) IssueType type,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) Integer storypoints
    ) {
        return new ResponseEntity<>(
                issueService.updateIssue(issueId, status, priority, type, title, description, storypoints),
                HttpStatus.OK
        );
    }

    @DeleteMapping(path = "{issueId}")
    public ResponseEntity<Void> deleteIssueWithId(@PathVariable Long issueId) {
        issueService.deleteIssue(issueId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}