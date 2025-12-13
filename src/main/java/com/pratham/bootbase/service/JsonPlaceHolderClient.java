package com.pratham.bootbase.service;

import com.pratham.bootbase.dto.Request.PostRequestDto;
import com.pratham.bootbase.dto.Response.PostResponseDto;

import java.util.List;


public interface JsonPlaceHolderClient {

    List<PostResponseDto> getAllPosts();
    PostResponseDto getPostById(Long id);
    PostResponseDto createPost(PostRequestDto postRequestDto);

}
