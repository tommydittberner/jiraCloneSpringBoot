package itf.todi.refinement.services;

import java.util.List;
import itf.todi.refinement.model.Issue;

public interface IssueService {
    /**
     * retrieves all issues
     * @return 
     */
    List<Issue> getIssues();


    /**
     * creates a new issue
     * @param issue to save
     * @return the saved issue
     */
    Issue addIssue(Issue issue);

    /**
     * Reorders the board and updates all issues in affected columns 
     * after a change was made
     * @param issueId of the issue moved
     * @param sourceColIdx the initial status
     * @param sourceIdx the row in the initial status the issue was shown before
     * @param destColIdx the status the issue was moved to
     * @param destIdx the row in the status the issue was moved at which the issue should be shown
     * @return all issues that were reordered
     */
    List<List<Issue>> updateBoard(
        Long issueId,
        int sourceColIdx,
        int sourceIdx,
        int destColIdx,
        int destIdx
    );

    /**
     * Updates an issue with the newly saved values
     * @param issueId of the issue to update
     * @param issue containing the new information to be saved to the issue
     * @return the updated issue
     */
    Issue updateIssue(Long issueId, Issue issue);

    /**
     * Deletes the issue of the provided ID
     * @param issueId of the issue to delete
     */
    void deleteIssue(Long issueId);
}
