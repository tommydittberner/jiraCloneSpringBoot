package itf.todi.refinement.services;

import org.springframework.stereotype.Service;

import itf.todi.refinement.model.Issue;
import itf.todi.refinement.model.IssueStatus;
import itf.todi.refinement.repositories.IssueRepository;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class IssueServiceImpl implements IssueService {

    private final IssueRepository issueRepository;

    public IssueServiceImpl(IssueRepository issueRepository) {
        this.issueRepository = issueRepository;
    }

    @Override
    public List<Issue> getIssues() {
        return issueRepository.findAll();
    }

    @Override
    public Issue addIssue(Issue issue) {
        // set sortingIdx so the issue is shown at the bottom of the current status
        issue.setSortingIdx(issueRepository.findAllByStatus(issue.getStatus()).size());
        return issueRepository.save(issue);
    }

    @Override
    public List<List<Issue>> updateBoard(
        Long issueId,
        int sourceColIdx,
        int sourceIdx,
        int destColIdx,
        int destIdx
    ) {
        List<Issue> sourceIssues = issueRepository.findAllByStatus(getStatusForIndex(sourceColIdx));

        if(sourceColIdx == destColIdx){
            Issue toMove = issueRepository
                    .findById(issueId)
                    .orElseThrow(() -> new IllegalStateException(
                        String.format("no issue with id %s found", issueId)));

            sourceIssues.remove(toMove);
            toMove.setSortingIdx(destIdx);
            reorderSameColumn(sourceIssues, sourceIdx, destIdx);
            sourceIssues.add(destIdx, toMove);
            issueRepository.saveAll(sourceIssues);

            return List.of(sourceIssues, List.of());
        } else {
            IssueStatus destStatus = getStatusForIndex(destColIdx);
            List<Issue> destIssues = issueRepository
                    .findAllByStatus(destStatus);

            Issue toMove = issueRepository
                    .findById(issueId)
                    .orElseThrow(() -> new IllegalStateException(
                        String.format("no issue with id %s found", issueId)));

            sourceIssues.remove(toMove);
            toMove.setStatus(destStatus);
            toMove.setSortingIdx(destIdx);
            reorderColumnChange(sourceIssues, sourceIdx, destIssues, destIdx);
            destIssues.add(destIdx, toMove);

            issueRepository.saveAll(Stream
                    .of(sourceIssues, destIssues)
                    .flatMap(Collection::stream)
                    .collect(Collectors.toList()));

            return List.of(sourceIssues, destIssues);
        }
    }

    @Transactional
    public Issue updateIssue(Long issueId, Issue issue) {
        Issue toUpdate = issueRepository.findById(issueId)
                .orElseThrow(() -> new IllegalStateException(
                        String.format("Issue with id %d does not exist", issueId)));
                        
        // TODO: status is only changeable from board right now
        toUpdate.setTitle(issue.getTitle());
        toUpdate.setDescription(issue.getDescription());
        toUpdate.setType(issue.getType());
        toUpdate.setPriority(issue.getPriority());
        toUpdate.setStorypoints(issue.getStorypoints());

        issueRepository.save(toUpdate);
        return toUpdate;
    }

    @Override
    public void deleteIssue(Long issueId) {
        if (issueRepository.findById(issueId).isPresent()) {
            issueRepository.deleteById(issueId);
        }
    }

    private IssueStatus getStatusForIndex(int idx) {
        return Arrays
                .stream(IssueStatus.values())
                .filter(s -> s.ordinal() == (idx - 1))
                .findAny() // faster than findFirst
                .orElseThrow(() -> new IllegalStateException(
                    String.format("Invalid index '%d' for status passed", idx))); 
    }

    private void reorderSameColumn(List<Issue> issues, int sourceIdx, int destIdx) {
        issues.forEach(issue -> {
            int sortingIdx = issue.getSortingIdx();
            if (sortingIdx >= Math.min(sourceIdx, destIdx) && sortingIdx <= Math.max(sourceIdx, destIdx)) {
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
