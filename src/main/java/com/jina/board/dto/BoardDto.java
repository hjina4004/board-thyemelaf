package com.jina.board.dto;

import java.time.LocalDateTime;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import com.jina.board.model.Board;
import com.jina.board.model.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BoardDto {
    private Long id;

    @Size(min = 2, max = 60, message = "제목은 2~60자로 입력하세요.")
    private String title;

    @NotEmpty(message = "내용을 입력하세요.")
    private String content;

    private Boolean isSecret;
    private String password;
    private User user;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Board toEntity() {
        return Board.builder()
                .id(id)
                .title(title)
                .content(content)
                .password(isSecret ? password : null)
                .user(user)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();
    }
}
