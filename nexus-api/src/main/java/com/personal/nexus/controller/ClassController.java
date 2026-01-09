package com.personal.nexus.controller;



import com.personal.nexus.controller.ClassRoomDTOs.GetClassDTO;
import com.personal.nexus.service.SectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/class")
public class ClassController {

    @Autowired
    private SectionService sectionService;

    @GetMapping("/get-section")
    public ResponseEntity<GetClassDTO> getClassDetails(@RequestParam Long sectionId) {
        return ResponseEntity.status(HttpStatus.OK).body(sectionService.getClass(sectionId));
    }

}
