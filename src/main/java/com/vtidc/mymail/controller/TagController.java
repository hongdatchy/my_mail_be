package com.vtidc.mymail.controller;

import com.vtidc.mymail.dto.RequestCreateTagDto;
import com.vtidc.mymail.service.TagService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tag")
@AllArgsConstructor
public class TagController {

    private final TagService tagService;

    @GetMapping
    public ResponseEntity<Object> findAll() {
        return ResponseEntity.ok(tagService.findAll());
    }

    @PostMapping
    public ResponseEntity<Object> createTag(@RequestBody RequestCreateTagDto requestCreateTagDto) {
        return ResponseEntity.ok(tagService.createTag(requestCreateTagDto.getName()));
    }


}
