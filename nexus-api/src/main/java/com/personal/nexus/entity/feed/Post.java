package com.personal.nexus.entity.feed;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="posts")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Post {

    private String username;
    private String role;
    private String section;
    private byte[] imageData;

}
