package com.roal.survey_engine.domain.survey.dto.workspace;

import com.roal.survey_engine.domain.survey.entity.Workspace;
import com.roal.survey_engine.domain.user.entity.UserEntity;
import com.roal.survey_engine.domain.user.service.UserService;
import org.hashids.Hashids;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class WorkspaceDtoMapper {

    private final UserService userService;
    private final Hashids workspaceHashids;
    private final Hashids surveyHashids;

    public WorkspaceDtoMapper(UserService userService,
                              @Qualifier("workspaceHashids") Hashids workspaceHashids,
                              @Qualifier("surveyHashids") Hashids surveyHashids) {
        this.userService = userService;
        this.workspaceHashids = workspaceHashids;
        this.surveyHashids = surveyHashids;
    }

    public Workspace dtoToEntity(CreateWorkspaceDto workspaceDto) {

        Workspace workspace = new Workspace(workspaceDto.title());

        if (workspaceDto.userIds() != null && !workspaceDto.userIds().isEmpty()) {

            List<UserEntity> users = workspaceDto
                    .userIds()
                    .stream()
                    .map(userService::findById)
                    .toList();

            for (UserEntity user : users) {
                workspace.addUser(user);
            }
        }
        return workspace;
    }

    public WorkspaceDto entityToDto(Workspace workspace) {
        List<String> surveyIds = workspace
                .getSurveys()
                .stream()
                .map(s -> surveyHashids.encode(s.getId()))
                .toList();
        List<String> userIds = workspace
                .getUsers()
                .stream()
                .map(u -> userService.idToHash(u.getId()))
                .toList();

        return new WorkspaceDto(workspaceHashids.encode(workspace.getId()), workspace.getTitle(), userIds, surveyIds);
    }

    private String surveyIdToHash(long id) {
        return surveyHashids.encode(id);
    }

}
