package com.study.datajpa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.Optional;
import java.util.UUID;

@EnableJpaAuditing
@SpringBootApplication
@EnableJpaRepositories
public class DataJpaApplication {

	public static void main(String[] args) {
		SpringApplication.run(DataJpaApplication.class, args);
	}


	@Bean
	public AuditorAware<String> auditorAware(){
		/* BaseEntitiy에 등록자, 수정자에 사용자가 누군지 리턴
		* 보통은 세션 등에서 사용자 정보를 추출해서 전달,*/
		return ()-> Optional.of(UUID.randomUUID().toString());
	}


}
