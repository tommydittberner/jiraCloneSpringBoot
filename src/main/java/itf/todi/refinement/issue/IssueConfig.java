package itf.todi.refinement.issue;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class IssueConfig {

    @Bean
    CommandLineRunner commandLineRunner(IssueRepository repository) {
        return args -> {
            Issue i1 = new Issue(
                    1L,
                    "My first issue",
                    "This is the very first issue, yeah!",
                    8,
                    IssueStatus.OPEN,
                    IssuePriority.HIGH,
                    IssueType.TASK
            );

            repository.save(i1);
        };
    }
}
