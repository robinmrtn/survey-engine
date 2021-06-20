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

    protected long elementId;

    protected ElementResponseDto(long elementId) {
        this.elementId = elementId;
    }

    public ElementResponseDto() {
    }

    public long getElementId() {
        return elementId;
    }

    public void setElementId(long elementId) {
        this.elementId = elementId;
    }


}
