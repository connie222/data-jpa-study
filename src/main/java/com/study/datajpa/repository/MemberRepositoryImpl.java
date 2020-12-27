package com.study.datajpa.repository;

import com.study.datajpa.entity.Member;
import lombok.RequiredArgsConstructor;

import javax.persistence.EntityManager;

@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepositoryCustom{
/*MemberRepository 클래스와 이름을 맞추어야 함 -> XXXImpl 네이밍 규칙 준수!  */

    private final EntityManager em;

    @Override
    public Member findMemberCustom() {
        return null;
    }
}
