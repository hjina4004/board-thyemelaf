package com.jina.board.repository;

import java.util.List;

import com.jina.board.model.Board;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long> {
    List<Board> findAllByOrderByIdDesc();

    Page<Board> findByTitleContainingOrContentContaining(String title, String content, Pageable pageable);
}
