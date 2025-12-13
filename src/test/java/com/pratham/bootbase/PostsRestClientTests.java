package com.pratham.bootbase;

import com.pratham.bootbase.dto.Request.PostRequestDto;
import com.pratham.bootbase.dto.Response.PostResponseDto;
import com.pratham.bootbase.service.JsonPlaceHolderClient;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class PostsRestClientTests {

    @Autowired
    private JsonPlaceHolderClient jsonPlaceHolderClient;

    @Test
    void testGetAll(){
        List<PostResponseDto> list = jsonPlaceHolderClient.getAllPosts();
        for(PostResponseDto item: list){
            System.out.println(item.getId());
        }
    }

    @Test
    void testGetById(){
        PostResponseDto postResponseDto = jsonPlaceHolderClient.getPostById(55L);
        System.out.println(postResponseDto);
    }

    @Test
    void testGetByIdFor404(){
        PostResponseDto postResponseDto = jsonPlaceHolderClient.getPostById(99999L);
        System.out.println(postResponseDto);
    }

    @Test
    void createPost(){
        PostRequestDto postRequestDto = PostRequestDto.builder()
                .title("My post")
                .body("First Post By pratham")
                .userId(101L)
                .build();

        PostResponseDto postResponseDto = jsonPlaceHolderClient.createPost(postRequestDto);
        System.out.println(postResponseDto);
    }


}
