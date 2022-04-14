package com.roal.survey_engine.domain.survey;

import com.roal.survey_engine.domain.survey.dto.survey.SurveyDto;
import com.roal.survey_engine.domain.survey.dto.survey.SurveyDtoMapper;
import com.roal.survey_engine.domain.survey.entity.Survey;
import com.roal.survey_engine.domain.survey.entity.SurveyPage;
import com.roal.survey_engine.domain.survey.entity.Workspace;
import com.roal.survey_engine.domain.survey.entity.question.ClosedQuestion;
import com.roal.survey_engine.domain.survey.entity.question.ClosedQuestionAnswer;
import com.roal.survey_engine.domain.survey.entity.question.OpenNumericQuestion;
import com.roal.survey_engine.domain.survey.entity.question.OpenTextQuestion;
import com.roal.survey_engine.domain.survey.service.WorkspaceService;
import org.hashids.Hashids;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;

@ExtendWith(MockitoExtension.class)
public class SurveyDtoMapperTest {

    SurveyDtoMapper surveyDtoMapper;
    @Mock
    private WorkspaceService workspaceService;
    @Mock
    private Hashids hashids;

    @BeforeEach
    void init() {
        surveyDtoMapper = new SurveyDtoMapper(hashids, workspaceService);
        Mockito.when(workspaceService.idToHashid(anyLong())).thenReturn("aaa");
    }

    @Test
    void testMapping() {

        Survey survey = new Survey()
                .addSurveyPage(new SurveyPage()
                        .addSurveyElement(new ClosedQuestion("This is a closed question")
                                .addAnswer(new ClosedQuestionAnswer("this is an answer"))
                                .addAnswer(new ClosedQuestionAnswer("This is another answer"))))
                .addSurveyPage(new SurveyPage()
                        .addSurveyElement(new OpenTextQuestion("this is a open question"))
                        .addSurveyElement(new OpenNumericQuestion("This is a numeric question")))
                .setWorkspace(new Workspace().setId(99L));

        SurveyDto surveyDto = surveyDtoMapper.entityToDto(survey);

        assertThat(survey.getTitle()).isEqualTo(surveyDto.title());
        assertThat(surveyDto.surveyPages().size()).isEqualTo(2);
        assertThat(surveyDto.workspaceId()).isEqualTo("aaa");
        assertThat(surveyDto.surveyPages().get(0).surveyPageElements().get(0).type()).isEqualTo("clq");
        assertThat(surveyDto.surveyPages().get(1).surveyPageElements().get(0).type()).isEqualTo("opq");
        assertThat(surveyDto.surveyPages().get(1).surveyPageElements().get(1).type()).isEqualTo("opnq");
    }
}
