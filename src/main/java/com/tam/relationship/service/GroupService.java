package com.tam.relationship.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tam.relationship.dto.request.GroupRequest;
import com.tam.relationship.dto.response.GroupResponse;
import com.tam.relationship.entity.Group;
import com.tam.relationship.exception.AppException;
import com.tam.relationship.exception.ErrorCode;
import com.tam.relationship.mapper.GroupMapper;
import com.tam.relationship.repository.GroupRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class GroupService {

    private final GroupRepository groupRepository;
    private final GroupMapper groupMapper;

    public GroupResponse createGroup(GroupRequest request) {
        Group group = groupMapper.toEntity(request);
        group.setIsActive(true);
        Group savedGroup = groupRepository.save(group);
        return groupMapper.toResponse(savedGroup);
    }

    public GroupResponse updateGroup(String id, GroupRequest request) {
        Group group =
                groupRepository.findByIdAndActive(id).orElseThrow(() -> new AppException(ErrorCode.GROUP_NOT_FOUND));

        groupMapper.updateEntity(group, request);
        Group updatedGroup = groupRepository.save(group);
        return groupMapper.toResponse(updatedGroup);
    }

    public void deleteGroup(String id) {
        Group group =
                groupRepository.findByIdAndActive(id).orElseThrow(() -> new AppException(ErrorCode.GROUP_NOT_FOUND));

        group.setIsActive(false);
        groupRepository.save(group);
    }

    @Transactional(readOnly = true)
    public GroupResponse getGroupById(String id) {
        Group group =
                groupRepository.findByIdAndActive(id).orElseThrow(() -> new AppException(ErrorCode.GROUP_NOT_FOUND));

        return groupMapper.toResponse(group);
    }

    @Transactional(readOnly = true)
    public List<GroupResponse> getAllGroups() {
        List<Group> groups = groupRepository.findAllActive();
        return groups.stream().map(groupMapper::toResponse).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Page<GroupResponse> getAllGroupsPage(Pageable pageable) {
        Page<Group> groupPage = groupRepository.findAllActive(pageable);
        return groupPage.map(groupMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public Page<GroupResponse> searchGroups(String name, Pageable pageable) {
        Page<Group> groupPage = groupRepository.findByNameContainingAndActive(name, pageable);
        return groupPage.map(groupMapper::toResponse);
    }
}
