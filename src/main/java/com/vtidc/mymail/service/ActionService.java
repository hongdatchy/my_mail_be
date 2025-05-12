package com.vtidc.mymail.service;

import com.vtidc.mymail.entities.Action;
import com.vtidc.mymail.repo.ActionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ActionService {

    private final ActionRepository actionRepository;

    public List<Action> getAllAction() {
        return actionRepository.findAll();
    }

}
