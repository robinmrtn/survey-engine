package com.roal.survey_engine.domain.survey.entity;

import com.roal.survey_engine.domain.user.entity.UserEntity;

import javax.persistence.*;
import java.util.*;

@Entity
public class Workspace {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_workspace")
    @SequenceGenerator(name = "seq_workspace")
    private Long id;

    private String title;

    private Boolean deleted = Boolean.FALSE;

    @OneToMany(mappedBy = "workspace")
    private Set<Survey> surveys = new HashSet<>();


    @ManyToMany
    @JoinTable(
            name = "workspace_user",
            joinColumns = @JoinColumn(
                    name = "workspace_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "user_id", referencedColumnName = "id"))
    private Collection<UserEntity> users = new ArrayList<>();

    public Workspace(String title) {
        this.title = title;
        users = new ArrayList<>();
        surveys = new HashSet<>();
    }

    public Workspace() {
    }

    public Long getId() {
        return id;
    }

    public Workspace setId(Long id) {
        this.id = id;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public Workspace setTitle(String title) {
        this.title = title;
        return this;
    }

    public Set<Survey> getSurveys() {
        return surveys;
    }

    public Workspace setSurveys(Set<Survey> surveys) {
        this.surveys = surveys;
        return this;
    }

    public Workspace addSurvey(Survey survey) {
        surveys.add(survey);
        return this;
    }

    public Collection<UserEntity> getUsers() {
        return Collections.unmodifiableCollection(users);
    }

    public Workspace addUser(UserEntity user) {
        users.add(user);
        return this;
    }

    public Workspace removeUser(UserEntity user) {
        users.remove(user);
        return this;
    }

    public boolean containsUserByUsername(String username) {
        return users.stream()
            .anyMatch(user -> user.getUsername().equals(username));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (id == null) return false;
        Workspace workspace = (Workspace) o;

        return Objects.equals(id, workspace.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    public Boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }
}
