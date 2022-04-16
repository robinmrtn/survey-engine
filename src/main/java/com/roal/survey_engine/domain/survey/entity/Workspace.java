package com.roal.survey_engine.domain.survey.entity;

import com.roal.survey_engine.domain.user.entity.User;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

@Entity
public class Workspace {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String title;

    private Boolean deleted = Boolean.FALSE;

    @OneToMany(mappedBy = "workspace")
    private Collection<Survey> surveys;

    @ManyToMany
    @JoinTable(
            name = "workspaces_users",
            joinColumns = @JoinColumn(
                    name = "workspace_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "user_id", referencedColumnName = "id"))
    private Collection<User> users;

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

        Workspace workspace = (Workspace) o;

        return id != null ? id.equals(workspace.id) : workspace.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
