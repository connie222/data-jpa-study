package com.study.datajpa.repository;

import com.study.datajpa.dto.MemberDto;
import com.study.datajpa.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import javax.persistence.Entity;
import javax.persistence.QueryHint;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom{

     List<Member> findByUsernameAndAgeGreaterThan(String username, int age);

     List<Member> findTop3HelloBy();

     //@Query(name="Member.findByUsername")
     // 1.namedQuery를 먼저 찾음 2. 상단 <Member>type과 메소드 합쳐서-> 즉 Member.findByUsername
     // 실제 업무에서 거의 사용하지 않으나 앱 로드시점에 파싱하여 오류를 발견할 수 있다.
     List<Member> findByUsername(@Param("username") String username);

     //이름이 없는 namedQuery라서 동일하게 로딩시점에 파싱(정적쿼리)하여 오류를 발견한다.
     @Query("select m from Member m where m.username=:username and m.age=:age")
     List<Member> findUser(@Param("username") String username, @Param("age") int age);

     @Query("select m.username from Member m")
     List<String> findUserNameList();

     //new Operation이라는 jpql이 제공해주는
     @Query("select new com.study.datajpa.dto.MemberDto(m.id, m.username, t.name) from Member m join m.team t ")
     List<MemberDto> findMemberDto();

     @Query("select m.username from Member m where m.username in :names ")
     List<Member> findByNames(@Param("names") Collection<String> names);

     List<Member> findListByUsername(String username);
     Member findMemberByUsername(String username);
     Optional<Member> findOptionalByUsername(String username);

     Page<Member> findByAge(int age, Pageable pageable);
     //Slice<Member> findByAge(int age, Pageable pageable); findByAge-가 JPA에서 제공하는 예약어? 라인이라서

     @Query(value = "select m from Member m left join m.team" //join시 별도의 COUNT쿼리 생성
             ,countQuery = "select count(m) from Member m")
     Page<Member> findByAgeJoin(int age, Pageable pageable);

     @Modifying(clearAutomatically = true)
     @Query("update Member m set m.age=m.age+1 where m.age >= :age")
     int bulkAgePlus(@Param("age") int age);

     @Query("select m from Member m left join fetch m.team")
     List<Member> findMemberFetchJoin();

     @Override
     @EntityGraph(attributePaths = {"team"})
     List<Member> findAll();

     @EntityGraph(attributePaths = {"team"})
     @Query("select m from Member m")
     List<Member> findMemberEntityGraph();

     @EntityGraph(attributePaths = {"team"})
     List<Member> findEntityGraphByUsername(@Param("username") String username);


     //query hint
     @QueryHints(value=@QueryHint(name="org.hibernate.readOnly", value="true"))
     Member findReadOnlyByUsername(String username);

}