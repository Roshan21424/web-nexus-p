package com.personal.nexus.controller;

import com.personal.nexus.entity.repository.ClassRepositoryFile;
import com.personal.nexus.service.ClassRepositoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/class-repository")
public class ClassRepositoryController {

    @Autowired
    private ClassRepositoryService classRepositoryService;

    @GetMapping("/get-class-repository")
    public ResponseEntity<List<ClassRepositoryFile>> getClassRepository(@RequestParam Long sectionId) {
        return ResponseEntity.status(HttpStatus.OK).body(classRepositoryService.getClassRepository(sectionId));
    }
}