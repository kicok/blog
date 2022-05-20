package com.nanum.blog.controller;

import com.nanum.blog.model.Board;
import com.nanum.blog.service.BoardService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class BoardController {

    private final BoardService boardService;

    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }

    @GetMapping({"", "/"})
    public String index(Model model, @PageableDefault(size = 3, sort = "id", direction = Sort.Direction.DESC)Pageable pageable){


        Page<Board>  boards = boardService.list(pageable);

        model.addAttribute("boards", boards.getContent());
        model.addAttribute("previous", pageable.previousOrFirst().getPageNumber());
        model.addAttribute("next", pageable.next().getPageNumber());
        model.addAttribute("firstCheck", boards.hasPrevious());
        model.addAttribute("nextCheck", boards.hasNext());







        return "index";
    }

    // User 권한 필요
    @GetMapping("/board/saveForm")
    public String saveForm(){

        return "board/saveForm";
    }
}
