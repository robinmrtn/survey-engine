package com.roal.survey_engine.dto.response;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = OpenQuestionResponseDto.class, name = "opq"),
        @JsonSubTypes.Type(value = ClosedQuestionResponseDto.class, name = "clq"),
        @JsonSubTypes.Type(value = OpenNumericQuestionResponseDto.class, name = "opnq"),
})
public abstract class ElementResponseDto {

    protected long id;

    protected ElementResponseDto(long elementId) {
        this.id = elementId;
    }

    public ElementResponseDto() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


}
