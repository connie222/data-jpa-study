package com.study.datajpa.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"id", "username", "age"})
/*
* namedQuery는 어플리케이션 실행 시점에 파싱을 하기 때문에
* 로딩 시점에 에러를 발견할 수 있다.
* */
@NamedQuery(
        name="Member.findByUsername",
        query="select m from Member m where m.username=:username"
)
public class Member extends JpaBaseEntity{

    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;
    private String username;
    private int age;

    @ManyToOne(fetch = FetchType.LAZY)
    //모든 연관관계는 지연로딩, 즉시 로딩일 경우 성능최적화 하기 어렵, 지연로딩이랑? 가짜 객체를 갖고 있다가  실제 값이 올때 그때 DB에서 꺼내옴
    @JoinColumn(name = "team_id")
    private Team team;

    public Member(String username){
        this.username = username;
    }

    public Member(String username, int age, Team team){
        this.username = username;
        this.age = age;
        if(team != null ) {
            changeTeam(team);
        }
    }

    public void changeTeam(Team team){
        this.team = team;
        team.getMembers().add(this); //내꺼 뿐만 아니라 맵핑된 반대쪽에도 정보를 변경해줘야함.
    }
}
