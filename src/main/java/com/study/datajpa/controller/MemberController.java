package com.study.datajpa.controller;

import com.study.datajpa.dto.MemberDto;
import com.study.datajpa.entity.Member;
import com.study.datajpa.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;

@RequiredArgsConstructor
@RestController
public class MemberController {

    private final MemberRepository memberRepository;

    @GetMapping("/v1/members/{id}")
    public String findMembersByv1(@PathVariable("id") long id){
        Member member = memberRepository.findById(id).get();
        return member.getUsername();
    }

    @GetMapping("/v2/members/{id}")
    public String findMembersByv2(@PathVariable("id") Member member){
        /*도메인 클래스 컨버터의 경우 조회로만 사용할 것을 권장! */
        return member.getUsername();
    }


    @GetMapping("/v3/members")
    public Page<MemberDto> findMembersByPagingV3(@PageableDefault(size=5, sort="username") Pageable pageable){
        Page<Member> page =  memberRepository.findAll(pageable);
        //entity는 절대로 밖으로 나가면 안된다.
        return page.map(MemberDto::new);
                //.map(member -> new MemberDto(member));
    }

    @GetMapping("/v4/members")
    public Page<MemberDto> findMembersByPagingV4(){
        PageRequest pr = PageRequest.of(1, 5, Sort.by(Sort.Direction.DESC,"id"));
        //page 를 1부터 진행할 경우 pageRequest를 생성해서 사용
        Page<Member> page =  memberRepository.findAll(pr);
        return page.map(MemberDto::new);
    }

    @PostConstruct
    public void init(){

        for(int i=0; i<100; i++){
            memberRepository.save(new Member("member"+i, i, null));
        }
    }

}
