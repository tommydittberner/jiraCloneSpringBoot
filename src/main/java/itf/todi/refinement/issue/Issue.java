package itf.todi.refinement.issue;

import lombok.*;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

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

    @Column(nullable = false)
    @Size(min = 1, max = 70)
    @NotBlank(message = "Title is required")
    private String title;

    @Column(nullable = false)
    @Size(min = 12, max = 240)
    @NotBlank(message = "Description is required")
    private String description;

    @Column(nullable = false)
    private Integer storypoints;

    @Column(nullable = false, length = 24)
    @Enumerated(EnumType.STRING)
    private IssueStatus status;

    @Column(nullable = false, length = 24)
    @Enumerated(EnumType.STRING)
    private IssuePriority priority;

    @Column(nullable = false, length = 24)
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
