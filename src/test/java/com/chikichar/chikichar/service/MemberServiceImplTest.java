package com.chikichar.chikichar.service;

import com.chikichar.chikichar.member.domain.Member;
import com.chikichar.chikichar.member.dto.MemberRequestDto;
import com.chikichar.chikichar.member.repository.MemberRepository;
import com.chikichar.chikichar.model.Address;
import com.chikichar.chikichar.model.Brand;
import com.chikichar.chikichar.model.MemberRole;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;



@SpringBootTest
class MemberServiceImplTest {

    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepository memberRepository;

    @Test
    @DisplayName("회원가입, 탈퇴 테스트")
    void joinWithdrawalTest(){
        MemberRequestDto memberRequestDto = MemberRequestDto.builder()
                .address(new Address("a","a","a"))
                .brand(Brand.AUDI)
                .memberRole(MemberRole.USER)
                .email("aaa@naver.com")
                .nickname("aa")
                .password("aaaa")
                .phone("101010100")
                .name("han")
                .build();

        Long saveMemberId = memberService.joinAccount(memberRequestDto);
        Optional<Member> saveMember = memberRepository.findById(saveMemberId);
        //가입 테스트
        Assertions.assertThat(saveMember.isPresent()).isEqualTo(true);
        memberService.deleteAccount(saveMemberId);
        Optional<Member> deleteMember = memberRepository.findById(saveMemberId);
        //탈퇴 테스트
        Assertions.assertThat(deleteMember.isPresent()).isEqualTo(false);
    }
    @Test
    @DisplayName("회원 정보 수정 테스트")
    void modifyTest()  {
        MemberRequestDto memberRequestDto = MemberRequestDto.builder()
                .address(new Address("a","a","a"))
                .brand(Brand.AUDI)
                .memberRole(MemberRole.USER)
                .email("bbb@naver.com")
                .nickname("bb")
                .password("bbbb")
                .phone("01044443333")
                .name("han")
                .build();

        Long saveMemberId = memberService.joinAccount(memberRequestDto);

        MemberRequestDto modifyMember = MemberRequestDto.builder()
                .address(new Address("b","b","b"))
                .brand(Brand.BMW)
                .nickname("aa")
                .password("aaaa")
                .phone("01033334444")
                .build();

        System.out.println(saveMemberId);
        memberService.modifyInfo(saveMemberId,modifyMember);

        Member member = memberRepository.findById(saveMemberId).orElseThrow();

        Assertions.assertThat(member.getNickname()).isEqualTo("aa");
    }

    @Test
    @DisplayName("이메일 중복학인 테스트")
    void emailDuplicateTest(){
        Member member = Member.builder().email("han@naver.com").build();
        memberRepository.save(member);

        boolean duplicateEmail = memberService.isDuplicateEmail("han@naver.com");

        Assertions.assertThat(duplicateEmail).isEqualTo(true);
    }
}