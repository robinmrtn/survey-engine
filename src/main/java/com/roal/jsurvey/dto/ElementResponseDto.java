package com.roal.jsurvey.dto;

public class ElementResponseDto {

    private long elementId;

    private String value;

    public ElementResponseDto() {
    }

    public ElementResponseDto(long elementId, String value) {
        this.elementId = elementId;
        this.value = value;
    }

    public long getElementId() {
        return elementId;
    }

    public void setElementId(long elementId) {
        this.elementId = elementId;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
