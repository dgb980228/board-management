package com.nhnacademy.jdbc.board.compre.service.impl;

import com.nhnacademy.jdbc.board.compre.domain.Post;
import com.nhnacademy.jdbc.board.compre.domain.PostMainView;
import com.nhnacademy.jdbc.board.compre.dto.CommentDTO;
import com.nhnacademy.jdbc.board.compre.domain.Pagination;
import com.nhnacademy.jdbc.board.compre.dto.PostDTO;
import com.nhnacademy.jdbc.board.compre.mapper.PostMapper;
import com.nhnacademy.jdbc.board.compre.service.CommentService;
import com.nhnacademy.jdbc.board.compre.service.PostService;
import com.nhnacademy.jdbc.board.compre.service.UserService;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class DefaultPostService implements PostService {
    private final PostMapper postMapper;
    private final UserService userService;
    private final CommentService commentService;

    public DefaultPostService(PostMapper postMapper, DefaultUserService userService, DefaultCommentService commentService) {
        this.postMapper = postMapper;
        this.userService = userService;
        this.commentService = commentService;
    }

    @Override
    public Optional<PostDTO> getPost(int id) {
        if (Objects.isNull(postMapper.selectPost(id))) {
            return Optional.empty();
        }
        Post pod = postMapper.selectPost(id).get();
        return Optional.of(new PostDTO(pod.getPostNo(),
            pod.getPostTitle(), (userService.getUserId(pod.getUserNo())),
            pod.getPostContent(), pod.getPostWriteDatetime(), pod.getPostModifyDatetime(),pod.getPostHits(),pod.isPostCheckHide()));
    }

    @Override
    public List<PostDTO> getPosts() { // 지울 것
        List<Post> postDTO = postMapper.selectPosts();
        List<PostDTO> postDTOS = new ArrayList<>();
        for (Post postDto : postDTO) {
            List<CommentDTO> commentDTO = commentService.getComments(postDto.getPostNo());
                postDTOS.add(new PostDTO(postDto.getPostNo(),
                    postDto.getPostTitle(), (userService.getUserId(postDto.getUserNo())),
                    postDto.getPostContent(), postDto.getPostWriteDatetime(), postDto.getPostModifyDatetime(),
                    commentDTO.size(),
                    postDto.isPostCheckHide()));
        }
        return postDTOS;
    }

    @Override
    public void register(PostDTO postDTO, int num) {
        postMapper.postRegister(postDTO, num);
    }

    @Override
    public void update(int id, String title, String content, Date date) {
        postMapper.postUpdate(id, title, content, date);
    }

    @Override
    public void delete(int id) {
        postMapper.postDelete(id);
    }

    @Override
    public void recover(int id) {
        postMapper.postRecover(id);
    }

    @Override
    public int getCount() {
        return this.postMapper.postCount();
    }

    @Override
    public List<PostDTO> getListPage(final Pagination pagination) {
        List<PostMainView> postDtoList = postMapper.getListPage(pagination);
        List<PostDTO> postDTOS = new ArrayList<>();
        for (PostMainView postDto : postDtoList) {
            List<CommentDTO> commentDTO = commentService.getComments(postDto.getPostNo());
            postDTOS.add(new PostDTO(postDto.getPostNo(),
                postDto.getPostTitle(), postDto.getUserId(),
                "", postDto.getPostWriteDatetime(), postDto.getPostModifyDatetime(),
                commentDTO.size(), postDto.isPostCheckHide()
            ));
        }
        return postDTOS;
    }

    @Override
    public List<PostDTO> searchPost(String title) {
        List<Post> postDTO = postMapper.searchPost(title);
        List<PostDTO> postDTOS = new ArrayList<>();
        for (Post postDto : postDTO) {
            List<CommentDTO> commentDTO = commentService.getComments(postDto.getPostNo());
            postDTOS.add(new PostDTO(postDto.getPostNo(),
                postDto.getPostTitle(), (userService.getUserId(postDto.getUserNo())),
                postDto.getPostContent(), postDto.getPostWriteDatetime(), postDto.getPostModifyDatetime(),
                commentDTO.size(),
                postDto.isPostCheckHide()));
        }
        return postDTOS;
    }
}
