package com.nanum.blog.controller;

import com.nanum.blog.config.auth.Annotation.AuthUser;
import com.nanum.blog.config.auth.PrincipalDetails;
import com.nanum.blog.model.Board;
import com.nanum.blog.service.BoardService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class BoardController {

    private final BoardService boardService;

    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }

    @GetMapping({"", "/"})
    public String index(@AuthUser PrincipalDetails principalDetails, Model model, @PageableDefault(size = 3, sort = "id", direction = Sort.Direction.DESC)Pageable pageable){


        Page<Board>  boards = boardService.list(pageable);

        model.addAttribute("principal", principalDetails);

        model.addAttribute("boards", boards.getContent());
        model.addAttribute("previous", pageable.previousOrFirst().getPageNumber());
        model.addAttribute("next", pageable.next().getPageNumber());
        model.addAttribute("firstCheck", boards.hasPrevious());
        model.addAttribute("nextCheck", boards.hasNext());
        return "index";
    }

    @GetMapping("/board/{id}")
    public String findById(@PathVariable int id, Model model, @AuthUser PrincipalDetails principalDetails){
      Board board = boardService.findById(id);
      model.addAttribute("principal", principalDetails);
      model.addAttribute("board", boardService.findById(id));
      return "board/detail";
    }


    // User 권한 필요
    @GetMapping("/board/saveForm")
    public String saveForm(){

        return "board/saveForm";
    }


}
