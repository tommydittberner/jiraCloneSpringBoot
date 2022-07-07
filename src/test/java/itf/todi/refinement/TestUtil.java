package itf.todi.refinement;

import itf.todi.refinement.model.Issue;
import itf.todi.refinement.model.IssuePriority;
import itf.todi.refinement.model.IssueStatus;
import itf.todi.refinement.model.IssueType;

public class TestUtil {
    public static Issue createMockIssue(String title) {
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
