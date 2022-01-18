package com.roal.survey_engine.domain.survey.entity.question;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class AbstractSurveyElement implements Comparable<AbstractSurveyElement> {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    protected long id;

    protected int position;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

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
