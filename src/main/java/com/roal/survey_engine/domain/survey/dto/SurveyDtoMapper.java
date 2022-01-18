package com.roal.survey_engine.domain.survey.dto;

import com.roal.survey_engine.domain.survey.dto.element.*;
import com.roal.survey_engine.domain.survey.entity.Survey;
import com.roal.survey_engine.domain.survey.entity.SurveyPage;
import com.roal.survey_engine.domain.survey.entity.question.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class SurveyDtoMapper {
    public SurveyDto entityToDto(Survey survey) {


        List<SurveyPageDto> surveyPageDtos = new ArrayList<>();

        for (SurveyPage surveyPage : survey.getSurveyPages()) {

            List<AbstractSurveyElement> surveyPageElements = surveyPage.getSurveyPageElements();

            List<AbstractElementDto> elementDtos = getElementDtos(surveyPageElements);

            surveyPageDtos.add(new SurveyPageDto(surveyPage.getPosition(), elementDtos));
        }
        return new SurveyDto(survey.getId(), survey.getTitle(), survey.getDescription(), surveyPageDtos);
    }

    private List<AbstractElementDto> getElementDtos(List<AbstractSurveyElement> surveyPageElements) {

        List<AbstractElementDto> elementDtos = new ArrayList<>();

        for (AbstractSurveyElement surveyPageElement : surveyPageElements) {
            if (surveyPageElement instanceof ClosedQuestion closedQuestion) {

                Set<ClosedQuestionAnswerDto> answersDto = closedQuestion.getAnswers().stream()
                        .map(answer -> new ClosedQuestionAnswerDto(answer.getValue(), answer.getId()))
                        .collect(Collectors.toSet());

                elementDtos.add(new ClosedQuestionDto(closedQuestion.getQuestion(),
                        closedQuestion.getId(),
                        closedQuestion.getPosition(),
                        answersDto));

            } else if (surveyPageElement instanceof OpenNumericQuestion openNumericQuestion) {

                elementDtos.add(new OpenNumericQuestionDto(openNumericQuestion.getQuestion(),
                        openNumericQuestion.getPosition(),
                        openNumericQuestion.getId()));

            } else if (surveyPageElement instanceof OpenTextQuestion openTextQuestion) {
                elementDtos.add(new OpenQuestionDto(openTextQuestion.getQuestion(),
                        openTextQuestion.getPosition(),
                        openTextQuestion.getId()));
            }
        }
        return elementDtos;
    }

    public Survey dtoToEntity(SurveyDto surveyDto) {
        var survey = new Survey();

        for (SurveyPageDto surveyPageDto : surveyDto.surveyPages()) {
            var surveyPage = new SurveyPage()
                    .setPosition(surveyPageDto.position());

            for (AbstractElementDto elementDto : surveyPageDto.surveyPageElements()) {
                switch (elementDto.type()) {
                    case "opq" -> {
                        AbstractSurveyElement element = new OpenTextQuestion(((OpenQuestionDto) elementDto).question());
                        element.setId(elementDto.id());
                        element.setPosition(elementDto.position());
                        surveyPage.addSurveyElement(element);
                    }
                    case "opnq" -> {
                        AbstractSurveyElement element = new OpenNumericQuestion(((OpenNumericQuestionDto) elementDto).question());
                        element.setId(elementDto.id());
                        element.setPosition(elementDto.position());
                        surveyPage.addSurveyElement(element);
                    }
                    case "clq" -> {
                        Set<ClosedQuestionAnswerDto> answers = ((ClosedQuestionDto) elementDto).answers();
                        ClosedQuestion element = new ClosedQuestion(((ClosedQuestionDto) elementDto).question());
                        element.setId(elementDto.id());
                        element.setPosition(elementDto.position());
                        element.setAnswers(dtoAnswersToEntity(answers));
                        surveyPage.addSurveyElement(element);
                    }
                    default -> {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid format");
                    }
                }
            }
            survey.addSurveyPage(surveyPage);
        }
        return survey;
    }

    private List<ClosedQuestionAnswer> dtoAnswersToEntity(Collection<ClosedQuestionAnswerDto> answerDtos) {
        return answerDtos.stream()
                .map((e) -> new ClosedQuestionAnswer(e.id(), e.answer()))
                .collect(Collectors.toList());
    }

}
