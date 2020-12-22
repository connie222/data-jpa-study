package com.study.datajpa.repository;

import com.study.datajpa.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {

     List<Member> findByUsernameAndAgeGreaterThan(String username, int age);

     List<Member> findTop3HelloBy();

     //@Query(name="Member.findByUsername")
     // 1.namedQuery를 먼저 찾음 2. 상단 <Member>type과 메소드 합쳐서-> 즉 Member.findByUsername
     // 실제 업무에서 거의 사용하지 않으나 앱 로드시점에 파싱하여 오류를 발견할 수 있다.
     List<Member> findByUsername(@Param("username") String username);

     //이름이 없는 namedQuery라서 동일하게 로딩시점에 파싱(정적쿼리)하여 오류를 발견한다.
     @Query("select m from Member m where m.username=:username and m.age=:age")
     List<Member> findUser(@Param("username") String username, @Param("age") int age);
}