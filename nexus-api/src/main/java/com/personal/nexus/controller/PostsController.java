package com.personal.nexus.controller;


import com.personal.nexus.entity.Section;
import com.personal.nexus.entity.User;
import com.personal.nexus.entity.feed.Post;
import com.personal.nexus.repository.PostRepository;
import com.personal.nexus.security.AuthService;
import com.personal.nexus.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/posts")
public class PostsController {

    @Autowired
    private AuthService authService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private PostRepository postRepository;

    @PostMapping(
            value = "/upload-post",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public String  getAllPost(@RequestParam("file") MultipartFile file) throws IOException {
       User user = authService.getCurrentUser(); //getting the current user
        System.out.println(user.getRole());
        System.out.println(user.getId());
        String sectionName = null;

        if (!user.getRole().name().equals("TEACHER")) {
            Section section = studentService.getSectionOfStudentByStudentId(user.getId());
            if (section != null) {
                sectionName = section.getSectionEnum().name();
            }
        }
        Post post = new Post();
        post.setImageData(file.getBytes());
        post.setRole(user.getRole().name());
        post.setUsername(user.getName());
        post.setSection(sectionName);
        postRepository.save(post);
        return "done";
    }

    @GetMapping("/get-all-post")
    public List<Post> getAllPosts(){
        List<Post> post =  postRepository.findAll();
        System.out.println("hi");
        for (Post item : post) {
            System.out.println(1);
        }
        return post;
    }
}
