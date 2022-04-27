package com.nanum.blog.model;

import com.nanum.blog.model.common.TimeEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Board  extends TimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, length = 100)
    private String title;

    @Lob // longtext
    private String content; // 섬머노트 라이브러리 <html> 태그 포함

    @ColumnDefault("0")
    private int count;

    // many=>board, one=>user,
    // @ManyToOne(fetch = FetchType.EAGER) : ManyToOne 의 FetchType 은 EAGER 가 default
    // (user 정보가 한건이므로 즉시(EAGER) 가져와도 무리없으므로 default )
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "userId")
    private User user;

    // 하나의 게시글은 여려개의 댓글이 있을수 있음
    // mappedBy : 연관관계의 주인이 아니다
    // 실제 foreign key 컬럼이 만들어지지 않으며 단지 board를 select 할 때  join을 통해서 값을 얻기 위한 용도 일뿐이다.
    @OneToMany(mappedBy = "board", fetch = FetchType.LAZY) //  Reply 클래스의 private Board board 를 가르킨다.
    // @OneToMany 의 FetchType 은 LAZY 가 default : Many 에 해당되는 정보는 수십만 건이 될수 있으므로..
    private List<Reply> reply;
}
