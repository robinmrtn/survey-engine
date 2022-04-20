package com.roal.survey_engine.domain.survey.entity;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class Survey {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_survey")
    @SequenceGenerator(name = "seq_survey")
    private Long id;

    private String title;

    private String description;

    private boolean deleted = Boolean.FALSE;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "survey_id")
    @OrderBy("position")
    private List<SurveyPage> surveyPages = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    private Workspace workspace;

    public Survey() {
        // needed by hibernate
    }

    public Long getId() {
        return id;
    }

    public Survey(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public Survey setDescription(String description) {
        this.description = description;
        return this;
    }

    public Survey addSurveyPage(SurveyPage surveyPage) {
        if (surveyPage.getPosition() <= 0) {
            int position = surveyPages.size() + 1;
            surveyPage.setPosition(position);
        }
        surveyPages.add(surveyPage);
        return this;
    }

    public List<SurveyPage> getSurveyPages() {
        return surveyPages;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public Survey setTitle(String title) {
        this.title = title;
        return this;
    }

    public Workspace getWorkspace() {
        return workspace;
    }

    public Survey setWorkspace(Workspace workspace) {
        this.workspace = workspace;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Survey survey = (Survey) o;
        return Objects.equals(id, survey.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
