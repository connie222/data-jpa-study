package com.study.datajpa.dto;

import com.study.datajpa.entity.Member;
import javassist.bytecode.annotation.MemberValue;
import lombok.Data;

@Data
public class MemberDto {

    private Long id;
    private String username;
    private String teamname;

    public MemberDto(Long id, String username, String teamname){
        this.id = id;
        this.username = username;
        this.teamname = teamname;
    }

    public MemberDto(Member member){
        this.id = member.getId();
        this.username = member.getUsername();
    }
}
