package com.roal.survey_engine.domain.survey.controller;

import com.roal.survey_engine.domain.survey.dto.workspace.WorkspaceDto;
import com.roal.survey_engine.domain.survey.service.WorkspaceService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@Tag(name = "Workspace", description = "Workspace API")
@RequestMapping("/api/workspaces")
@RestController
public class WorkspaceController {

    private final WorkspaceService workspaceService;

    public WorkspaceController(WorkspaceService workspaceService) {
        this.workspaceService = workspaceService;
    }

    @PostMapping("/")
    public WorkspaceDto create(@Valid WorkspaceDto workspaceDto) {
        return workspaceService.create(workspaceDto);
    }

    @PostMapping("/{id}/users/{userId}")
    public WorkspaceDto addUser(@PathVariable @NotBlank String id, @PathVariable @NotBlank String userId) {
        return workspaceService.addUser(id, userId);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@NotBlank @PathVariable String id) {
        workspaceService.deleteById(id);
    }

    @DeleteMapping("/{id}/users/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUserFromWorkspace(@NotBlank @PathVariable String id, @PathVariable @NotBlank String userId) {
        workspaceService.deleteUserFromWorkspace(id, userId);
    }
}
