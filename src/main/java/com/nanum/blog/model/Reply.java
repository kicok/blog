package com.nanum.blog.model;

import com.nanum.blog.model.common.TimeEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Reply extends TimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, length = 200)
    private String content;

    @ManyToOne // 여러 댓글이 달릴수 있다 하나의 글안에 (many => Reply, one => Board)
    @JoinColumn(name="boardId")
    private Board board;

    @ManyToOne // 여러댓글을 달수있다 하나의 유저가.. (many => Reply, one => User)
    @JoinColumn(name="userId")
    private User user;
}
