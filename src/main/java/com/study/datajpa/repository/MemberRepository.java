package com.study.datajpa.repository;

import com.study.datajpa.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {

     List<Member> findByUsernameAndAgeGreaterThan(String username, int age);
     List<Member> findTop3HelloBy();
}