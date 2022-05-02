package com.roal.survey_engine.domain.survey.dto.survey;

import com.roal.survey_engine.domain.survey.dto.survey.element.*;
import com.roal.survey_engine.domain.survey.entity.Survey;
import com.roal.survey_engine.domain.survey.entity.SurveyPage;
import com.roal.survey_engine.domain.survey.entity.question.*;
import com.roal.survey_engine.domain.survey.service.WorkspaceService;
import org.hashids.Hashids;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
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

    private final Hashids surveyHashids;
    private final WorkspaceService workspaceService;

    public SurveyDtoMapper(@Qualifier("surveyHashids") Hashids surveyHashids, WorkspaceService workspaceService) {
        this.surveyHashids = surveyHashids;
        this.workspaceService = workspaceService;
    }

    public SurveyDto entityToDto(Survey survey) {

        List<SurveyPageDto> surveyPageDtos = new ArrayList<>();

        for (SurveyPage surveyPage : survey.getSurveyPages()) {

            List<AbstractSurveyElement> surveyPageElements = surveyPage.getSurveyPageElements();
            List<AbstractElementDto> elementDtos = getElementDtos(surveyPageElements);
            surveyPageDtos.add(new SurveyPageDto(surveyPage.getPosition(), elementDtos));
        }
        return new SurveyDto(surveyHashids.encode(wrapperToPrimitive(survey.getId())), survey.getTitle(),
                survey.getDescription(), workspaceService.idToHashid(survey.getWorkspace().getId()), surveyPageDtos);
    }

    private List<AbstractElementDto> getElementDtos(List<AbstractSurveyElement> surveyPageElements) {

        List<AbstractElementDto> elementDtos = new ArrayList<>();

        for (AbstractSurveyElement surveyPageElement : surveyPageElements) {
            elementDtos.add(parseElements(surveyPageElement));
        }
        return elementDtos;
    }

    private AbstractElementDto parseElements(AbstractSurveyElement surveyPageElement) {

        if (surveyPageElement instanceof ClosedQuestion closedQuestion) {

            Set<ClosedQuestionAnswerDto> answersDto = closedQuestion.getAnswers().stream()
                    .map(answer -> new ClosedQuestionAnswerDto(answer.getValue(), wrapperToPrimitive(answer.getId())))
                    .collect(Collectors.toSet());

            return new ClosedQuestionDto(closedQuestion.getQuestion(),
                    wrapperToPrimitive(closedQuestion.getId()),
                    closedQuestion.getPosition(),
                    answersDto);

        } else if (surveyPageElement instanceof OpenNumericQuestion openNumericQuestion) {

            return new OpenNumericQuestionDto(openNumericQuestion.getQuestion(),
                    openNumericQuestion.getPosition(),
                    wrapperToPrimitive(openNumericQuestion.getId()));

        } else if (surveyPageElement instanceof OpenTextQuestion openTextQuestion) {
            return new OpenQuestionDto(openTextQuestion.getQuestion(),
                    openTextQuestion.getPosition(),
                    wrapperToPrimitive(openTextQuestion.getId()));
        }

        throw new IllegalArgumentException();
    }

    public Survey dtoToEntity(CreateSurveyDto surveyDto) {
        var survey = new Survey();

        for (SurveyPageDto surveyPageDto : surveyDto.surveyPages()) {
            var surveyPage = new SurveyPage()
                .setPosition(surveyPageDto.position());

            parseElements(surveyPageDto, surveyPage);
            survey.addSurveyPage(surveyPage);
        }
        return survey;
    }

    private void parseElements(SurveyPageDto surveyPageDto, SurveyPage surveyPage) {
        for (AbstractElementDto elementDto : surveyPageDto.surveyPageElements()) {
            parseElementDto(surveyPage, elementDto);
        }
    }

    private void parseElementDto(SurveyPage surveyPage, AbstractElementDto elementDto) {
        switch (elementDto.type()) {
            case "opq" -> {
                var element = new OpenTextQuestion(((OpenQuestionDto) elementDto).question());
                element.setId(primitiveToWrapper(elementDto.id()));
                element.setPosition(elementDto.position());
                surveyPage.addSurveyElement(element);
            }
            case "opnq" -> {
                var element = new OpenNumericQuestion(((OpenNumericQuestionDto) elementDto).question());
                element.setId(primitiveToWrapper(elementDto.id()));
                element.setPosition(elementDto.position());
                surveyPage.addSurveyElement(element);
            }
            case "clq" -> {
                Set<ClosedQuestionAnswerDto> answers = ((ClosedQuestionDto) elementDto).answers();
                ClosedQuestion element = new ClosedQuestion(((ClosedQuestionDto) elementDto).question());
                element.setId(primitiveToWrapper(elementDto.id()));
                element.setPosition(elementDto.position());
                element.setAnswers(dtoAnswersToEntity(answers));
                surveyPage.addSurveyElement(element);
            }
            default -> throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid format: Element type '" +
                elementDto.type() + "' not valid");
        }
    }

    public Page<SurveyListElementDto> surveysToListDto(Page<Survey> surveys) {

        return surveys.map(survey -> {
            String hashid = surveyHashids.encode(survey.getId());
            return new SurveyListElementDto(hashid, survey.getTitle(), survey.getDescription());
        });
    }

    private List<ClosedQuestionAnswer> dtoAnswersToEntity(Collection<ClosedQuestionAnswerDto> answerDtos) {
        return answerDtos.stream()
                .map((e) -> new ClosedQuestionAnswer(e.id(), e.answer()))
                .collect(Collectors.toList());
    }

    private long wrapperToPrimitive(Long value) {
        if (value == null) {
            return 0;
        }
        return value;
    }

    private Long primitiveToWrapper(long value) {
        if (value == 0) {
            return null;
        }
        return value;
    }
}
