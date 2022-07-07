package itf.todi.refinement.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateInfo {
    private Long issueId;
    private int sourceCol;
    private int sourceIdx;
    private int destCol;
    private int destIdx;
}
