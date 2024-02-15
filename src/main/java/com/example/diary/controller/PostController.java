package com.example.diary.controller;

import com.example.diary.domain.post.Post;
import com.example.diary.dto.post.PostCreateDto;
import com.example.diary.dto.post.PostDto;
import com.example.diary.dto.post.PostUpdateDto;
import com.example.diary.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;

    /**
     * 내 일기 목록
     */
    @GetMapping("/member/{memberId}")
    public List<PostDto> lists(@PathVariable @Valid Long memberId) {
        List<Post> posts = postService.getList(memberId);
        List<PostDto> postDtos = posts.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return postDtos;
    }

    /**
     * 일기 생성
     */
    @PostMapping("/new")
    public Long create(@RequestBody @Valid PostCreateDto postCreateDto) {
        Long id = postService.create(postCreateDto);
        return id;
    }

    /**
     * 단일 일기 조회
     */
    @GetMapping("/{postId}")
    public PostDto get(@PathVariable @Valid Long postId) {
        Post post = postService.get(postId);
        PostDto postDto = convertToDto(post);
        return postDto;
    }

    /**
     * 일기 수정
     */
    @PostMapping("/{postId}/edit")
    public Long update(@PathVariable @Valid Long postId, @RequestBody @Valid PostUpdateDto postUpdateDto) {
        postService.update(postUpdateDto);
        return postUpdateDto.getId();
    }

    /**
     * 일기 삭제
     */
    @DeleteMapping("/{postId}/remove")
    public Long remove(@PathVariable @Valid Long postId) {
        postService.remove(postId);
        return postId;
    }

    private PostDto convertToDto(Post post) {
        PostDto postDto = new PostDto(
                post.getId(),
                post.getMember().getId(),
                post.getTitle(),
                post.getBody(),
                post.getView(),
                post.getCreatedDate(),
                post.getChangedDate(),
                post.getComments(),
                post.getLikes()
        );
        return postDto;
    }
}
