package itf.todi.refinement.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import itf.todi.refinement.model.Issue;
import itf.todi.refinement.model.IssueStatus;

import java.util.List;

@Repository
public interface IssueRepository extends JpaRepository<Issue, Long> {
    List<Issue> findAllByStatus(IssueStatus status);
}
