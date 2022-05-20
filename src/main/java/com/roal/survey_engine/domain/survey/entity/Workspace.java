package com.roal.survey_engine.domain.survey.entity;

import com.roal.survey_engine.domain.user.entity.User;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

@Entity
public class Workspace {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_workspace")
    @SequenceGenerator(name = "seq_workspace")
    private Long id;

    private String title;

    private Boolean deleted = Boolean.FALSE;

    @OneToMany(mappedBy = "workspace")
    private Collection<Survey> surveys = new ArrayList<>();


    @ManyToMany
    @JoinTable(
            name = "workspace_user",
            joinColumns = @JoinColumn(
                    name = "workspace_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "user_id", referencedColumnName = "id"))
    private Collection<User> users = new ArrayList<>();

    public Workspace(String title) {
        this.title = title;
        users = new ArrayList<>();
        surveys = new ArrayList<>();
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

    public Collection<Survey> getSurveys() {
        return surveys;
    }

    public Workspace setSurveys(Collection<Survey> surveys) {
        this.surveys = surveys;
        return this;
    }

    public Workspace addSurvey(Survey survey) {
        surveys.add(survey);
        return this;
    }

    public Collection<User> getUsers() {
        return Collections.unmodifiableCollection(users);
    }

    public Workspace addUser(User user) {
        users.add(user);
        return this;
    }

    public Workspace removeUser(User user) {
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
