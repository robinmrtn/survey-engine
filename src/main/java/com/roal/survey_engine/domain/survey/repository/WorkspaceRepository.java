package com.roal.survey_engine.domain.survey.repository;

import com.roal.survey_engine.domain.survey.entity.Workspace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface WorkspaceRepository extends JpaRepository<Workspace, Long> {

    @Override
    @Modifying
    @Query("update Workspace w set w.deleted = true where w.id =:id")
    void deleteById(Long id);

    @Query("select w from Workspace w join w.users u where u.username =:username")
    List<Workspace> findByUsername(String username);
}
