package com.study.datajpa.repository;

import com.study.datajpa.dto.MemberDto;
import com.study.datajpa.entity.Member;
import com.study.datajpa.entity.Team;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SpringBootTest
@Transactional
@Rollback(false)
public class MemberRepositoryTest {

    @PersistenceContext
    private EntityManager em;

    @Autowired MemberRepository memberRepository;
    @Autowired TeamRepository teamRepository;


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

    @Test
    public void testFindUserNameList(){
        Member m1 = new Member("AAA", 10,null);
        Member m2 = new Member("BBB", 20, null);

        memberRepository.save(m1);
        memberRepository.save(m2);

        List<String> result = memberRepository.findUserNameList();
        for(String s: result){
            System.out.println("s = "+s);
        }
    }

    @Test
    public void testFindMemberDto(){
        Team team = new Team("teamA");
        teamRepository.save(team);

        Member m1 = new Member("AAA", 10, team);
        memberRepository.save(m1);

        List<MemberDto> result = memberRepository.findMemberDto();
        for(MemberDto dto: result){
            System.out.println("s = "+ dto);
        }
    }


    @Test
    public void testFindBynames(){
        Member m1 = new Member("AAA", 10,null);
        Member m2 = new Member("BBB", 20, null);

        memberRepository.save(m1);
        memberRepository.save(m2);

        List<String> list = new ArrayList<>();
        list.add("AAA");
        list.add("BBB");

        List<Member> result = memberRepository.findByNames(list);//Arrays.asList("AAA","BBB")가 되지 안흔다.. ;
        for(Member dto: result){
            System.out.println("s = "+ dto);
        }
    }

    @Test
    public void testReturnType(){
        Member m1 = new Member("AAA", 10,null);
        Member m2 = new Member("BBB", 20, null);

        memberRepository.save(m1);
        memberRepository.save(m2);

        /*
        1. returnType List
        List<Member> result = memberRepository.findListByUsername("AAA");
        for(Member dto: result){
            System.out.println("s = "+ dto);
        }*/
        //아무것도 조회되지 않을 경우 빈(empty)객체가 리턴
        List<Member> result = memberRepository.findListByUsername("dfdfd");
        System.out.println("result "+result.size());

        //spring jpas는 noResultException을 감싸서 null로 리ㅓㄴ
        Member member = memberRepository.findMemberByUsername(("dfdfd"));
        System.out.println("result member "+member);

        //클라이언트에서 NULL에 대한 처리 -> Optional사용
        Optional<Member> optionalMember = memberRepository.findOptionalByUsername("AAA");
        System.out.println("result optionalMember "+optionalMember);

    }

    @Test
    public void paging() {
        memberRepository.save(new Member("AAA", 10, null));
        memberRepository.save(new Member("BBB", 10, null));
        memberRepository.save(new Member("CCC", 10, null));
        memberRepository.save(new Member("DDD", 10, null));
        memberRepository.save(new Member("EEE", 20, null));

        int age = 10;
        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC,"username"));
        /*
        * page index는 1이 아닌 0부터 시작
        * */

        Page<Member> page = memberRepository.findByAge(age, pageRequest);
        /*
        Page클래스는 totalCount, Pages 등 페이징 처리와 관련된 메소드를 제공
        따라서 만약 관련한 정보가 필요하지 않는다면 List<Member>처럼 returnType을 변경하면 된다.
        만약 3건만 추출시 findTopByAge를 활용해도 된다.
        */

        Page<MemberDto> toMap = page.map(m -> new MemberDto(m.getId(), m.getUsername(), m.getTeam().getName()));
        /*
        * Entity는 무조건 내부에서만 사용해야한다 -> entity가 변경되면 데이터가 변경되지 때문
        * 따라서 entity내용은 dto로 감싸서 사용자에게 제공되어야 함
        * */
        
        List<Member> list = page.getContent();
        long totalCount = page.getTotalElements(); //토탈 건수 가 필요하기 때문에 Page에서 count(*)쿼리를 호출.

        Assertions.assertThat(list.size()).isEqualTo(3);
        Assertions.assertThat(totalCount).isEqualTo(4);
        Assertions.assertThat(page.getNumber()).isEqualTo(0);//현재 페이지
        Assertions.assertThat(page.getTotalPages()).isEqualTo(2);//총 페이지 건수
        Assertions.assertThat(page.isFirst()).isTrue(); // 현재 페이지가 첫 페이지인지 여부
        Assertions.assertThat(page.hasNext()).isTrue();//다음 페이지 여부
    }

    @Test
    public void pagingBySlice() {
        memberRepository.save(new Member("AAA", 10, null));
        memberRepository.save(new Member("BBB", 10, null));
        memberRepository.save(new Member("CCC", 10, null));
        memberRepository.save(new Member("DDD", 10, null));
        memberRepository.save(new Member("EEE", 10, null));

        int age = 10;
        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC,"username"));

        Slice<Member> page = memberRepository.findByAge(age, pageRequest);
        /*
        위의 pageing() 테스트 케이스와 동일.
        * slice는 더보기 형태로 페이징 토탈카운트가 필요하지 않는 페이징을 처리할 때 사용한다.
        * 따라서 totalCount와 관련한 내부 로직은 정의되어있지 않다
        * size를 3으로 요청하였으나 내부 로직에는 하나 더 추가해 4로 추출해서 반환하게 된다.
        * Slice는  Page객체를 상속받아 사용한다.
        * */

        List<Member> list = page.getContent();

        Assertions.assertThat(list.size()).isEqualTo(3);
        Assertions.assertThat(page.getNumber()).isEqualTo(0);//현재 페이지
        Assertions.assertThat(page.isFirst()).isTrue(); // 현재 페이지가 첫 페이지인지 여부
        Assertions.assertThat(page.hasNext()).isTrue();//다음 페이지 여부
    }

    @Test
    public void bulkUpdate() throws Exception {
        //given
        memberRepository.save(new Member("member1", 10, null));
        memberRepository.save(new Member("member2", 19, null));
        memberRepository.save(new Member("member3", 20, null));
        memberRepository.save(new Member("member4", 21, null));
        memberRepository.save(new Member("member5", 40, null));
        //when
        int resultCount = memberRepository.bulkAgePlus(20);
        /*
        * bulk edit의 경우 영속성 컨텍스트에 의해 DB는 변경되나, 영속성 컨텍스트(Entity Manager)는 변경되지 않는다.
        * 따라서 영속성 컨텍스트의 데이터를 모두 flush 시킨다면 DB를 다시 조회하여 변경되 데이터를 맞는다.
        *
        * 기본적으로 save와 같은 경우 insert(DML)쿼리를 호출한 뒤 내부적으로 flush시키기 때문에
        * 영속성 컨텍스트의 데이터와 db데이터가 동기화될 수 있는 것이다.
        * */

        List<Member> list = memberRepository.findByUsername("member5");
        Member m = list.get(0);
        System.out.println("flush data : "+ m.getAge());

        //then
        Assertions.assertThat(resultCount).isEqualTo(3);
    }

    @Test
    public void findMemberLazy() throws Exception {
        //given
        //member1 -> teamA
        //member2 -> teamB
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        teamRepository.save(teamA);
        teamRepository.save(teamB);
        memberRepository.save(new Member("member1", 10, teamA));
        memberRepository.save(new Member("member2", 20, teamB));
        em.flush();
        em.clear();
        //when

        /*
          List<Member> members = memberRepository.findAll();

        * lazyLoading은 실제 데이터 값을 조회하지 않는다면 가짜 객체(Team$HibernateProxy$~)를 생성해서 반환한다.
        * 따라서 Member내의 teamName을 획득하려고 하지 않는다면 가짜 객체인 Team을 반환한다.
        * 그러나, teamName을 조회하려고 한다면 하나의 조회 쿼리(Member)에 따른 결과값들 만큼 또 다른 조회 쿼리(Team)를 수행하며
        * N+1이라는 문제가 발생한다.
        * -> 이를 해결하기 위해서 fetch라는 옵션을 두어 DB쿼리를 조회시 한번에 join하여 데이터를 가져오도록 할 수 있다.
        * */
        //List<Member> members = memberRepository.findMemberFetchJoin();

        List<Member> members = memberRepository.findEntityGraphByUsername("member1");


        //then
        for (Member member : members) {
            System.out.println("member name: "+member.getUsername());
            System.out.println("member teamClass: "+member.getTeam().getClass());
            System.out.println("member real teamName: "+member.getTeam().getName());
        }
    }


    @Test
    public void queryHint() throws Exception {
        //given
        memberRepository.save(new Member("member1", 10, null));
        em.flush();
        em.clear();
        //when
        Member member = memberRepository.findReadOnlyByUsername("member1");
        member.setUsername("member2");
        em.flush(); //Update Query 실행X
    }

    @Test
    public void jpaEventBaseEntity() throws Exception {
        //given
        Member member = new Member("member1");
        memberRepository.save(member); //@PrePersist

        Thread.sleep(100);

        member.setUsername("member2");
        em.flush(); //@PreUpdate
        em.clear();

        //when
        Member findMember = memberRepository.findById(member.getId()).get();

        //then
        System.out.println(" createdAt : "+findMember.getCreatedAt());
        System.out.println(" updatedAt : "+findMember.getUpdatedAt());

    }

}
