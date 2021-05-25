package itf.todi.refinement.issue;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class IssueConfig {

    @Bean
    CommandLineRunner commandLineRunner(IssueRepository repository) {
        return args -> {
            Issue i1 = new Issue(
                    "Setup Project",
                    "This is the very first issue, yeah!",
                    8,
                    IssueStatus.OPEN,
                    IssuePriority.HIGH,
                    IssueType.TASK
            );
            Issue i2 = new Issue(
                    "Fix dependencies",
                    "Some UI thing broke. Plz fix.",
                    3,
                    IssueStatus.IN_PROGRESS,
                    IssuePriority.BLOCKER,
                    IssueType.BUG
            );
            Issue i3 = new Issue(
                    "Issue Details navigation",
                    "Some UI thing broke. Plz fix.",
                    1,
                    IssueStatus.IN_PROGRESS,
                    IssuePriority.TRIVIAL,
                    IssueType.STORY
            );
            Issue i4 = new Issue(
                    "Performance Improvement for Service XYZ",
                    "This thing is slow. Make it fast.",
                    8,
                    IssueStatus.OPEN,
                    IssuePriority.HIGH,
                    IssueType.IMPROVEMENT
            );
            Issue i5 = new Issue(
                    "Button for issue needs new layout",
                    "This thing is slow. Make it fast.",
                    8,
                    IssueStatus.OPEN,
                    IssuePriority.LOW,
                    IssueType.IMPROVEMENT
            );
            Issue i6 = new Issue(
                    "Search bar implementation",
                    "This thing is slow. Make it fast.",
                    8,
                    IssueStatus.IN_PROGRESS,
                    IssuePriority.HIGH,
                    IssueType.STORY
            );
            Issue i7 = new Issue(
                    "Write matching algorithms for search bar",
                    "This thing is slow. Make it fast.",
                    8,
                    IssueStatus.OPEN,
                    IssuePriority.BLOCKER,
                    IssueType.STORY
            );
            Issue i8 = new Issue(
                    "Text of button should be lowercase",
                    "This thing is slow. Make it fast.",
                    8,
                    IssueStatus.DONE,
                    IssuePriority.TRIVIAL,
                    IssueType.IMPROVEMENT
            );
            Issue i9 = new Issue(
                    "Drag and drop",
                    "This thing is slow. Make it fast.",
                    8,
                    IssueStatus.DONE,
                    IssuePriority.HIGH,
                    IssueType.STORY
            );
            Issue i10 = new Issue(
                    "Columns with issues come from backend",
                    "This thing is slow. Make it fast.",
                    8,
                    IssueStatus.DONE,
                    IssuePriority.NORMAL,
                    IssueType.IMPROVEMENT
            );
            Issue i11 = new Issue(
                    "Issue delete",
                    "This thing is slow. Make it fast.",
                    8,
                    IssueStatus.IN_PROGRESS,
                    IssuePriority.NORMAL,
                    IssueType.TASK
            );
            Issue i12 = new Issue(
                    "Issue update on drag and drop",
                    "This thing is slow. Make it fast.",
                    8,
                    IssueStatus.PEER_REVIEW,
                    IssuePriority.HIGH,
                    IssueType.TASK
            );

            repository.saveAll(List.of(i1, i2, i3, i4, i5, i6, i7, i8, i9, i10, i11, i12));
        };
    }
}
