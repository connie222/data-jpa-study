package com.study.datajpa.repository;

import com.study.datajpa.entity.Member;
import com.study.datajpa.entity.Team;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@SpringBootTest
@Transactional
@Rollback(false)
public class MemberRepositoryTest {

    @PersistenceContext
    private EntityManager em;

    @Autowired MemberRepository memberRepository;

    @Test
    public void testEntity(){
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");

        em.persist(teamA); //persist > insert call
        em.persist(teamB);

        Member member1 = new Member("member1", 11, teamA);
        Member member2 = new Member("member2", 12, teamB);
        Member member3 = new Member("member3", 13, teamA);
        Member member4 = new Member("member4", 14, teamB);

        em.persist(member1);
        em.persist(member2);
        em.persist(member3);
        em.persist(member4);

        //초기화
        em.flush();//
        em.clear(); //jpa있는 캐쉬를 날림

        //확인
        List<Member> members = em.createQuery("select m from Member m", Member.class).getResultList();

        for(Member member : members){
            System.out.println("member = " + member);
            System.out.println("-> member.name = " + member.getTeam());

        }
    }

    @Test
    public void basicCRUD() {
        Member member1 = new Member("member1");
        Member member2 = new Member("member2");

        memberRepository.save(member1);
        memberRepository.save(member2);

        //단건 조회 검증
        Member findMember1 = memberRepository.findById(member1.getId()).get();
        Member findMember2 = memberRepository.findById(member2.getId()).get();
        Assertions.assertThat(findMember1).isEqualTo(member1);
        Assertions.assertThat(findMember2).isEqualTo(member2);

        //리스트 조회 검증
        List<Member> all = memberRepository.findAll();
        Assertions.assertThat(all.size()).isEqualTo(2);

        //카운트 검증
        Long count = memberRepository.count();
        Assertions.assertThat(count).isEqualTo(2);

        //삭제 검증
        memberRepository.delete(member1);
        memberRepository.delete(member2);

        Long deletedCount = memberRepository.count();
        Assertions.assertThat(deletedCount).isEqualTo(0);
    }

    @Test
    public void findByUsernameAndAgeGreaterThen() {
        //processed data insert
        Member m1 = new Member("AAA", 10,null);
        Member m2 = new Member("AAA", 20, null);

        memberRepository.save(m1);
        memberRepository.save(m2);

       //Test
        List<Member> result = memberRepository.findByUsernameAndAgeGreaterThan("AAA", 15); //jpa의 경우 예약어이기 때문에 오타나면 스펠링이 틀리면 안됌ㅠ ex) then -> than(O)
        Assertions.assertThat(result.get(0).getUsername()).isEqualTo("AAA");
        Assertions.assertThat(result.get(0).getAge()).isEqualTo(20);
        Assertions.assertThat(result.size()).isEqualTo(1);
    }

    @Test
    public void findTop3HelloBy() {
        memberRepository.findTop3HelloBy();
    }

    @Test
    public void namedQuery(){
        Member m1 = new Member("AAA", 10,null);
        Member m2 = new Member("BBB", 20, null);

        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> result = memberRepository.findByUsername("AAA");
        Assertions.assertThat(result.get(0).getAge()).isEqualTo(10);
    }

    @Test
    public void testFindUser(){
        Member m1 = new Member("AAA", 10,null);
        Member m2 = new Member("BBB", 20, null);

        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> result = memberRepository.findUser("AAA",10);
        Assertions.assertThat(result.get(0)).isEqualTo(m1);
    }


}
