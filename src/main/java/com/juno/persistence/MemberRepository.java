package com.juno.persistence;

import org.springframework.data.repository.CrudRepository;

import com.juno.domain.Member;

public interface MemberRepository extends CrudRepository<Member, String> {

	
	
}
