package itf.todi.refinement.issue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class IssueService {

    private final IssueRepository issueRepository;

    @Autowired
    public IssueService(IssueRepository issueRepository) {
        this.issueRepository = issueRepository;
    }

    public List<Issue> getIssues() {
        return issueRepository.findAll();
    }

    public Issue addIssue(Issue issue) {
        return issueRepository.save(issue);
    }

    public List<List<Issue>> updateIssueSorting(int sourceColIdx, int sourceIdx, int destColIdx, int destIdx) {
        Optional<IssueStatus> sourceStatus = getStatusByColumnIndex(sourceColIdx);
        Optional<IssueStatus> destStatus = getStatusByColumnIndex(destColIdx);

        List<Issue> sourceCol = issueRepository
                .findAllByStatus(sourceStatus.orElseThrow(() ->
                        new IllegalStateException("source items not found")));

        List<Issue> destCol = issueRepository
                .findAllByStatus(destStatus.orElseThrow(() ->
                        new IllegalStateException("destination items not found")));

        //todo: find issue by something else than the index in the array (too error prone)
        Issue toMove = sourceCol.remove(sourceIdx);
        toMove.setStatus(destStatus.get()); //assign new status
        toMove.setSortingIdx(destIdx);

        moveAffectedIssues(sourceCol, sourceIdx, destCol, destIdx);
        destCol.add(destIdx, toMove);
        updateAffectedIssues(sourceCol, destCol);

        return List.of(sourceCol, destCol);
    }

    @Transactional
    public Issue updateIssue(Long issueId, Issue issue) {
        Issue toUpdate = issueRepository.findById(issueId)
                .orElseThrow(() -> new IllegalStateException(
                        String.format("Student with id %d does not exist", issueId)
                ));

        //id does not change
        //status is only changeable from board right now
        toUpdate.setTitle(issue.getTitle());
        toUpdate.setDescription(issue.getDescription());
        toUpdate.setType(issue.getType());
        toUpdate.setPriority(issue.getPriority());
        toUpdate.setStorypoints(issue.getStorypoints());

        issueRepository.save(toUpdate);
        return toUpdate;
    }

    public void deleteIssue(Long issueId) {
        if (issueRepository.findById(issueId).isPresent()) {
            issueRepository.deleteById(issueId);
        }
    }

    private Optional<IssueStatus> getStatusByColumnIndex(int idx) {
        return Arrays
                .stream(IssueStatus.values())
                .filter(s -> s.ordinal() == (idx - 1))
                .findFirst();
    }

    private void moveAffectedIssues(List<Issue> sourceCol, int sourceIdx, List<Issue> destCol, int destIdx) {
        //move up
        sourceCol.forEach(issue -> {
            int sortingIdx = issue.getSortingIdx();
            if (sortingIdx > sourceIdx){
                issue.setSortingIdx(sortingIdx - 1);
            }
        });

        //push down
        destCol.forEach(issue -> {
            int sortingIdx = issue.getSortingIdx();
            if (sortingIdx >= destIdx) { //insert before item with (currently) the same idx
                issue.setSortingIdx(sortingIdx + 1);
            }
        });
    }

    private void updateAffectedIssues(List<Issue> sourceCol, List<Issue> destCol) {
        issueRepository.saveAll(Stream
                .of(sourceCol, destCol)
                .flatMap(Collection::stream)
                .collect(Collectors.toList()));
    }
}
