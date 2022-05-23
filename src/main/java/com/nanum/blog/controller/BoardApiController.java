package com.nanum.blog.controller;

import com.nanum.blog.config.auth.PrincipalDetails;
import com.nanum.blog.dto.ResponseDto;
import com.nanum.blog.model.Board;
import com.nanum.blog.service.BoardService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
public class BoardApiController {

    private final BoardService boardService;

    public BoardApiController(BoardService boardService) {
        this.boardService = boardService;
    }

    @PostMapping("/api/board")
    public ResponseDto<Integer> save(@RequestBody Board board, Authentication authentication){
        // 로그인 사용자
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();

        boardService.saveBoard(board,principalDetails.getUser());
       return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
    }

    @DeleteMapping("/api/board/{id}")
    public ResponseDto<Integer> deleteById(@PathVariable int id){
        boardService.deleteById(id);

        return new ResponseDto<>(HttpStatus.OK.value(), 1);
    }
}
