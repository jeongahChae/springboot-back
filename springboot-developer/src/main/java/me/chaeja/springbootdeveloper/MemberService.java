package me.chaeja.springbootdeveloper;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MemberService {

	@Autowired
	MemberRepository memberRepository;
	
	public void test() {
		
		//create
		memberRepository.save(new Member(1L, "A"));
		
		//read
		Optional<Member> member = memberRepository.findById(1L);	//단건 조회
		List<Member> allMembers = memberRepository.findAll();		//전체 조회
		
		//delete
		memberRepository.deleteById(1L);
	}
}
