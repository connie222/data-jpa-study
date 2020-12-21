package com.study.datajpa.repository;

import com.study.datajpa.entity.Member;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
public class MemberJpaRepository {

    @PersistenceContext
    private EntityManager em;

    public Member save(Member member){
        em.persist(member);
        return member;
    }

    public void delete(Member member){
        em.remove(member);
    }

    public List<Member> findAll() {
        //jpql -> sql로 번역되어 실제 디비에서 조회후 반환
        return   em.createQuery("select m from Member m", Member.class).getResultList();
    }

    public Optional<Member> findById(Long id){
        Member member = em.find(Member.class, id);
        return Optional.ofNullable(member); //null일수도 아닐수도
    }

    public long count() {
        return em.createQuery("select count(m) from Member m", Long.class).getSingleResult(); //반환 데이터가 하나일 경우 single
    }

    public Member find(long id){
        return em.find(Member.class,id);
    }

    public List<Member> findByUsernameAndAgeGreaterThen(String username, int age){
        return em.createQuery("select m from Member m where m.username=:username and m.age>:age")
                .setParameter("username",username)
                .setParameter("age",age)
                .getResultList();
    }
}