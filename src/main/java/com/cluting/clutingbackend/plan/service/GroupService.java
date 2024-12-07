package com.cluting.clutingbackend.plan.service;

import com.cluting.clutingbackend.plan.repository.GroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GroupService {
    private final GroupRepository groupRepository;
}
