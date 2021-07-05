package com.roal.survey_engine.entity.question;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.roal.survey_engine.entity.survey.SurveyPage;

import javax.persistence.*;
import java.util.Objects;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = OpenTextQuestion.class, name = "opq"),
        @JsonSubTypes.Type(value = ClosedQuestion.class, name = "clq"),
        @JsonSubTypes.Type(value = OpenNumericQuestion.class, name = "opnq"),
})
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = OpenTextQuestion.class, name = "opq"),
        @JsonSubTypes.Type(value = ClosedQuestion.class, name = "clq"),
        @JsonSubTypes.Type(value = OpenNumericQuestion.class, name = "opnq"),
})
public abstract class AbstractSurveyElement implements Comparable<AbstractSurveyElement> {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "element_seq")
    protected long id;

    protected int position;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    protected SurveyPage surveyPage;

    public SurveyPage getSurveyPage() {
        return surveyPage;
    }

    public void setSurveyPage(SurveyPage surveyPage) {
        this.surveyPage = surveyPage;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getPosition() {
        return position;
    }

    public abstract AbstractSurveyElement setPosition(int position);

    @Override
    public int compareTo(AbstractSurveyElement o) {
        return Integer.compare(this.position, o.getPosition());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OpenTextQuestion that = (OpenTextQuestion) o;
        return id == that.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
