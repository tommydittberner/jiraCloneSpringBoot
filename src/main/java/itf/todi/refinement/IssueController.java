package itf.todi.refinement;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import itf.todi.refinement.model.Issue;
import itf.todi.refinement.model.UpdateInfo;
import itf.todi.refinement.services.IssueServiceImpl;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping(path = "api/issue")
@AllArgsConstructor
public class IssueController {

    private final IssueServiceImpl issueService;

    @GetMapping
    public ResponseEntity<List<Issue>> getIssue() {
        return new ResponseEntity<>(
                issueService.getIssues(),
                HttpStatus.OK
        );
    }

    @PostMapping
    public ResponseEntity<Issue> addIssue(@RequestBody Issue issue) {
        return new ResponseEntity<>(
                issueService.addIssue(issue),
                HttpStatus.OK
        );
    }

    @PutMapping
    public ResponseEntity<List<List<Issue>>> updateBoard(@RequestBody UpdateInfo updateInfo)
    {
        return new ResponseEntity<>(
                issueService.updateBoard(
                    updateInfo.getIssueId(), 
                    updateInfo.getSourceCol(), 
                    updateInfo.getSourceIdx(), 
                    updateInfo.getDestCol(),
                    updateInfo.getDestIdx()
                ),
                HttpStatus.OK
        );
    }

    @PutMapping(path = "{issueId}")
    public ResponseEntity<Issue> updateIssueWithId(@PathVariable Long issueId, @RequestBody Issue issue) {
        return new ResponseEntity<>(
                issueService.updateIssue(issueId, issue),
                HttpStatus.OK
        );
    }

    @DeleteMapping(path = "{issueId}")
    public ResponseEntity<Void> deleteIssueWithId(@PathVariable Long issueId) {
        issueService.deleteIssue(issueId);
        return new ResponseEntity<>(
                HttpStatus.NO_CONTENT
        );
    }
}
