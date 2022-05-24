 package com.nanum.blog.service;

 import com.nanum.blog.model.Board;
 import com.nanum.blog.model.User;
 import com.nanum.blog.repository.BoardRepository;
 import org.springframework.data.domain.Page;
 import org.springframework.data.domain.Pageable;
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

    @Transactional
    public void updateBoard(int id, Board requestBoard){
        Board board = boardRepository.findById(id)
                .orElseThrow(()->new RuntimeException("게시글이 존재하지 않습니다."));

        board.setTitle(requestBoard.getTitle());
        board.setContent(requestBoard.getContent());
        //함수가 종료되면 더티체킹으로 자동으로 수정이 된다.
    }

    @Transactional(readOnly = true)
    public Page<Board> list(Pageable pageable){
        return boardRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Board findById(int id){
        return boardRepository.findById(id)
                .orElseThrow(()-> new IllegalArgumentException("존재하지 않는 게시글 입니다 : " + id));
    }

    @Transactional
    public void deleteById(int id){
        boardRepository.deleteById(id);
    }
}
