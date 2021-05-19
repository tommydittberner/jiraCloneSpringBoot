package itf.todi.refinement.issue;

import lombok.*;

import javax.persistence.*;


@Entity
@Table(name = "issue")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class Issue {

    @Id
    @SequenceGenerator(
            name = "issue_sequence",
            sequenceName = "issue_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "issue_sequence"
    )
    private Long id;
    private String title;
    private String description;
    private Integer storypoints;

    @Enumerated(EnumType.STRING)
    private IssueStatus status;

    @Enumerated(EnumType.STRING)
    private IssuePriority priority;

    @Enumerated(EnumType.STRING)
    private IssueType type;

    public Issue(
            String title,
            String description,
            Integer storypoints,
            IssueStatus status,
            IssuePriority priority,
            IssueType type
    ) {
        this.title = title;
        this.description = description;
        this.storypoints = storypoints;
        this.status = status;
        this.priority = priority;
        this.type = type;
    }
}
