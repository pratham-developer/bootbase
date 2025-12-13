package com.pratham.bootbase.service;

import com.pratham.bootbase.dto.Request.PostRequestDto;
import com.pratham.bootbase.dto.Response.PostResponseDto;
import com.pratham.bootbase.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

@Service
public class JsonPlaceHolderClientImpl implements JsonPlaceHolderClient{

    @Qualifier("jsonplaceholderRestClient")
    private final RestClient restClient;

    public JsonPlaceHolderClientImpl(RestClient restClient) {
        this.restClient = restClient;
    }

    @Override
    public List<PostResponseDto> getAllPosts() {
        try{
            //parametrized type reference is when response body is generic
            //otherwise we use the actual class we want to wrap in

            return restClient.get()
                    .uri("posts")
                    .retrieve()
                    .body(new ParameterizedTypeReference<>() {
                    });

        }catch (Exception e){
            System.out.println(e.getMessage());
            return null;
        }
    }

    @Override
    public PostResponseDto getPostById(Long id) {

        try {
            return restClient.get()
                    .uri("posts/{id}",id)
                    .retrieve()
                    .onStatus(HttpStatusCode::is4xxClientError,((request, response) -> {
                        System.out.println("status code = "+response.getStatusCode());
                        throw new ResourceNotFoundException("Post not found with id: "+id);
                    }))
                    .body(PostResponseDto.class);

        }catch (Exception e){
            System.out.println(e.getMessage());
            return null;
        }

    }

    @Override
    public PostResponseDto createPost(PostRequestDto postRequestDto) {
        try{
            return restClient.post()
                    .uri("posts")
                    .body(postRequestDto)
                    .retrieve()
                    .body(PostResponseDto.class);
        }catch (Exception e){
            System.out.println(e.getMessage());
            return null;
        }
    }
}
