package itf.todi.refinement.model;

import lombok.*;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Table(name = "issue")
@Data
@NoArgsConstructor
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
    private int storypoints;

    @Column(nullable = false, length = 24)
    @Enumerated(EnumType.ORDINAL)
    private IssueStatus status;

    @Column(nullable = true) //todo: should be false
    private int sortingIdx;

    @Column(nullable = false, length = 24)
    @Enumerated(EnumType.STRING)
    private IssuePriority priority;

    @Column(nullable = false, length = 24)
    @Enumerated(EnumType.STRING)
    private IssueType type;

    public Issue(
            String title,
            String description,
            int storypoints,
            IssueStatus status,
            int sortingIdx,
            IssuePriority priority,
            IssueType type
    ) {
        this.title = title;
        this.description = description;
        this.storypoints = storypoints;
        this.status = status;
        this.sortingIdx = sortingIdx;
        this.priority = priority;
        this.type = type;
    }
}
