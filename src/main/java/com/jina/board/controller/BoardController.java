package com.jina.board.controller;

import javax.validation.Valid;

import com.jina.board.dto.BoardDto;
import com.jina.board.model.Board;
import com.jina.board.service.BoardService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("boards")
@Controller
public class BoardController {
    private final BoardService boardService;

    @GetMapping("")
    public String index(Model model,
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam(value = "search", required = false, defaultValue = "") String searchText) {
        Page<Board> boards = boardService.findByTitleContainingOrContentContaining(searchText, searchText, pageable);
        int startPage = Math.max(1, boards.getPageable().getPageNumber() - 1);
        int endPage = Math.min(boards.getTotalPages(), boards.getPageable().getPageNumber() + 3);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("boards", boards);
        return "boards/index";
    }

    @GetMapping("create")
    public String create(Model model) {
        model.addAttribute("boardDto", new BoardDto());
        return "boards/add_edit";
    }

    @PostMapping("store")
    public String store(@Valid BoardDto boardDto, BindingResult result) {
        if (result.hasErrors()) {
            return "boards/add_edit";
        }
        if (boardDto.getIsSecret() == null)
            boardDto.setIsSecret(false);
        boardService.createOrUpdate(boardDto.toEntity());
        return "redirect:/boards";
    }

    @GetMapping("{id}")
    public String show(Model model, @PathVariable("id") Long id,
            @RequestParam(value = "password", required = false, defaultValue = "") String pw) {
        Board board = boardService.findById(id, pw, true);
        model.addAttribute("boardDto", board.toDto());
        return "boards/show";
    }

    @GetMapping("{id}/edit")
    public String edit(Model model, @PathVariable("id") Long id,
            @RequestParam(value = "password", required = false, defaultValue = "") String pw) {
        Board board = boardService.findById(id, pw, false);
        model.addAttribute("boardDto", board.toDto());
        return "boards/add_edit";
    }

    @DeleteMapping("{id}")
    public String delete(@PathVariable("id") Long id) {
        boardService.deleteById(id, false);
        return "redirect:/boards";
    }

    public static String maskingName(String str) {
        Integer length = str.length();
        String firstCharater = str.substring(0, 1);
        String lastCharater = str.substring(length - 1, length);
        return firstCharater + "***" + lastCharater;
    }
}
