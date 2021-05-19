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
                    "My first issue",
                    "This is the very first issue, yeah!",
                    8,
                    IssueStatus.OPEN,
                    IssuePriority.HIGH,
                    IssueType.TASK
            );
            Issue i2 = new Issue(
                    "Something broke and needs to be fixed immediately",
                    "Some UI thing broke. Plz fix.",
                    3,
                    IssueStatus.IN_PROGRESS,
                    IssuePriority.BLOCKER,
                    IssueType.BUG
            );
            Issue i3 = new Issue(
                    "This is an issue of type 'Story'",
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
                    IssueStatus.DONE,
                    IssuePriority.HIGH,
                    IssueType.IMPROVEMENT
            );

            repository.saveAll(List.of(i1, i2, i3, i4));
        };
    }
}
