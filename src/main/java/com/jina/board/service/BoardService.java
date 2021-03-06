package com.jina.board.service;

import java.time.LocalDateTime;

import javax.transaction.Transactional;

import com.jina.board.model.Board;
import com.jina.board.model.User;
import com.jina.board.repository.BoardRepository;
import com.jina.board.repository.UserRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;

    @Transactional
    public Board createOrUpdate(Board board) {
        LocalDateTime now = LocalDateTime.now();
        if (board.getId() != null) {
            Board currentBoard = boardRepository.getById(board.getId());
            if (board.getPassword() == "")
                board.setPassword(currentBoard.getPassword());
            board.setUser(currentBoard.getUser());
            board.setCreatedAt(currentBoard.getCreatedAt());
        } else {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            User user = userRepository.findByUsername(auth.getName()).orElseThrow();
            board.setUser(user);
            board.setCreatedAt(now);
        }

        board.setUpdatedAt(now);
        return boardRepository.save(board);
    }

    @Transactional
    public Page<Board> findAll(Pageable pageable) {
        return boardRepository.findAll(pageable);
    }

    @Transactional
    public Page<Board> findByTitleContainingOrContentContaining(String title, String content, Pageable pageable) {
        return boardRepository.findByTitleContainingOrContentContaining(title, content, pageable);
    }

    @Transactional
    public Board findById(Long id, String pw, Boolean skipAuthority) {
        Board board = boardRepository.findById(id).orElseThrow();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        Boolean isError = board.getPassword() != null && !pw.equals(board.getPassword());
        Boolean isAuthority = auth.getName().equals(board.getUser().getUsername());

        if (isError)
            throw new RuntimeException("??????????????? ??????????????????.");
        else if (!skipAuthority && !isAuthority)
            throw new RuntimeException("?????? ????????? ????????????.");

        return boardRepository.findById(id).orElseThrow();
    }

    @Transactional
    public void deleteById(Long id, Boolean skipAuthority) {
        Board board = boardRepository.findById(id).orElseThrow();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Boolean isAuthority = auth.getName().equals(board.getUser().getUsername());
        if (!skipAuthority && !isAuthority)
            throw new RuntimeException("?????? ????????? ????????????.");

        boardRepository.deleteById(id);
    }
}
