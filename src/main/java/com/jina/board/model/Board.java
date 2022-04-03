package com.jina.board.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import com.jina.board.dto.BoardDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "boards")
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(min = 2, max = 60, message = "제목은 2~60자로 입력하세요.")
    private String title;

    @NotEmpty(message = "내용을 입력하세요.")
    @Column(columnDefinition = "LONGTEXT")
    private String content;

    private String password;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;   

    public BoardDto toDto() {
        return BoardDto.builder()
                .id(id)
                .title(title)
                .content(content)
                .isSecret(password != null)
                .user(user)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();
    }
}
