 package com.nanum.blog.service;

 import com.nanum.blog.model.Board;
 import com.nanum.blog.model.User;
 import com.nanum.blog.repository.BoardRepository;
 import org.springframework.stereotype.Service;
 import org.springframework.transaction.annotation.Transactional;


 @Service
public class BoardService {

    private final BoardRepository boardRepository;

    public BoardService(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    @Transactional
    public void saveBoard(Board board, User user){ // title, content
        board.setCount(0);
        board.setUser(user);
        boardRepository.save(board);
    }
}
