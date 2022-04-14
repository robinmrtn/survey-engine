package com.roal.survey_engine.domain.survey.service;

import com.roal.survey_engine.domain.survey.dto.workspace.WorkspaceDto;
import com.roal.survey_engine.domain.survey.dto.workspace.WorkspaceDtoMapper;
import com.roal.survey_engine.domain.survey.entity.Workspace;
import com.roal.survey_engine.domain.survey.exception.WorkspaceNotFoundException;
import com.roal.survey_engine.domain.survey.repository.WorkspaceRepository;
import com.roal.survey_engine.domain.user.entity.User;
import com.roal.survey_engine.domain.user.service.UserService;
import org.hashids.Hashids;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class WorkspaceService {

    private final WorkspaceRepository workspaceRepository;
    private final WorkspaceDtoMapper workspaceDtoMapper;
    private final Hashids workspaceHashid;
    private final UserService userService;


    public WorkspaceService(WorkspaceRepository workspaceRepository, WorkspaceDtoMapper workspaceDtoMapper,
                            @Qualifier("workspaceHashids") Hashids workspaceHashid, UserService userService) {
        this.workspaceRepository = workspaceRepository;
        this.workspaceDtoMapper = workspaceDtoMapper;
        this.workspaceHashid = workspaceHashid;
        this.userService = userService;
    }

    @Transactional
    public WorkspaceDto create(WorkspaceDto workspaceDto) {
        Workspace workspace = workspaceDtoMapper.dtoToEntity(workspaceDto);
        Workspace savedWorkspace = workspaceRepository.save(workspace);
        return workspaceDtoMapper.entityToDto(savedWorkspace);
    }

    @Transactional
    public void deleteById(String hashid) {
        long id = hashidToId(hashid);
        workspaceRepository.deleteById(id);
    }

    public Workspace getEntityByHashid(String hashid) {
        long id = hashidToId(hashid);
        return workspaceRepository.findById(id)
                .orElseThrow(() -> new WorkspaceNotFoundException(hashid));
    }

    private long hashidToId(String hashid) {
        long[] hashids = workspaceHashid.decode(hashid);
        if (hashids.length == 0) {
            throw new WorkspaceNotFoundException(hashid);
        }
        return hashids[0];
    }

    public String idToHashid(long id) {
        return workspaceHashid.encode(id);
    }

    @Transactional
    public WorkspaceDto addUser(String hashid, String userId) {
        long id = hashidToId(hashid);
        Workspace workspace = workspaceRepository.findById(id)
                .orElseThrow(() -> new WorkspaceNotFoundException(hashid));
        User user = userService.findById(userId);
        workspace.addUser(user);

        return workspaceDtoMapper.entityToDto(workspace);
    }

    @Transactional
    public void deleteUserFromWorkspace(String hashid, String userId) {
        long id = hashidToId(hashid);
        Workspace workspace = workspaceRepository.findById(id)
                .orElseThrow(() -> new WorkspaceNotFoundException(hashid));
        User user = userService.findById(userId);
        workspace.removeUser(user);
    }
}
