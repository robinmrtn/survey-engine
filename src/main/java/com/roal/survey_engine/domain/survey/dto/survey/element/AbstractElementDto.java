package com.roal.survey_engine.domain.survey.dto.survey.element;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import javax.validation.constraints.NotBlank;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = OpenQuestionDto.class, name = "opq"),
        @JsonSubTypes.Type(value = ClosedQuestionDto.class, name = "clq"),
        @JsonSubTypes.Type(value = OpenNumericQuestionDto.class, name = "opnq"),
})
public interface AbstractElementDto {
    int position();

    long id();

    @NotBlank
    String type();
}
