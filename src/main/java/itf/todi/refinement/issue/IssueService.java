package itf.todi.refinement.issue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
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
        issue.setSortingIdx(issueRepository.findAllByStatus(issue.getStatus()).size());
        return issueRepository.save(issue);
    }

    /**
     * Updates the board after a drag and drop action (same column & moving between columns)
     * 1. extract and update moved issue
     * 2. reorder affected columns
     * 3. put moved issue into destination column
     * 4. save updated issues
     * */
    public List<List<Issue>> updateBoard(Long issueId, int sourceColIdx, int sourceIdx, int destColIdx, int destIdx) {
        Optional<IssueStatus> sourceStatus = getStatusByColumnIndex(sourceColIdx);

        List<Issue> sourceCol = issueRepository
                .findAllByStatus(sourceStatus.orElseThrow(() ->
                        new IllegalStateException(String.format("No issues for status %s found", sourceStatus))));

        if(sourceColIdx == destColIdx){
            Issue toMove = issueRepository
                    .findById(issueId).orElseThrow(() ->
                            new IllegalStateException(String.format("no issue with id %s found", issueId)));

            sourceCol.remove(toMove);

            toMove.setSortingIdx(destIdx);

            reorderSameColumn(sourceCol, sourceIdx, destIdx);

            sourceCol.add(destIdx, toMove);

            issueRepository.saveAll(sourceCol);

            return List.of(sourceCol, List.of());
        } else {
            Optional<IssueStatus> destStatus = getStatusByColumnIndex(destColIdx);

            List<Issue> destCol = issueRepository
                    .findAllByStatus(destStatus.orElseThrow(() ->
                            new IllegalStateException(String.format("No issues for status %s found", destStatus))));

            Issue toMove = issueRepository
                    .findById(issueId).orElseThrow(() ->
                            new IllegalStateException(String.format("no issue with id %s found", issueId)));

            sourceCol.remove(toMove);

            toMove.setStatus(destStatus.get()); //assign new status
            toMove.setSortingIdx(destIdx);

            reorderColumnChange(sourceCol, sourceIdx, destCol, destIdx);

            destCol.add(destIdx, toMove);

            issueRepository.saveAll(Stream
                    .of(sourceCol, destCol)
                    .flatMap(Collection::stream)
                    .collect(Collectors.toList()));

            return List.of(sourceCol, destCol);
        }
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

    private void reorderSameColumn(List<Issue> column, int sourceIdx, int destIdx) {
        column.forEach(issue -> {
            int sortingIdx = issue.getSortingIdx();
            if (sortingIdx >= Math.min(sourceIdx, destIdx) &&
                    sortingIdx <= Math.max(sourceIdx, destIdx)) {
                if (sourceIdx < destIdx) {
                    issue.setSortingIdx(sortingIdx - 1); //move up
                } else {
                    issue.setSortingIdx(sortingIdx + 1); //push down
                }
            }
        });
    }
    private void reorderColumnChange(List<Issue> sourceCol, int sourceIdx, List<Issue> destCol, int destIdx) {
        reorderSourceColumn(sourceCol, sourceIdx);
        reorderDestinationColumn(destCol, destIdx);
    }

    private void reorderSourceColumn(List<Issue> column, int idx) {
        column.forEach(issue -> {
            int sortingIdx = issue.getSortingIdx();
            if (sortingIdx > idx){
                issue.setSortingIdx(sortingIdx - 1); //move up
            }
        });
    }

    private void reorderDestinationColumn(List<Issue> column, int idx) {
        column.forEach(issue -> {
            int sortingIdx = issue.getSortingIdx();
            if (sortingIdx >= idx) { //insert before item with (currently) the same idx
                issue.setSortingIdx(sortingIdx + 1); //push down
            }
        });
    }
}
