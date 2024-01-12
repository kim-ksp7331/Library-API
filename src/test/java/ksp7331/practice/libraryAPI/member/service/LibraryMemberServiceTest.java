package ksp7331.practice.libraryAPI.member.service;

import ksp7331.practice.libraryAPI.exception.BusinessLogicException;
import ksp7331.practice.libraryAPI.library.service.LibraryService;
import ksp7331.practice.libraryAPI.member.domain.LibraryMember;
import ksp7331.practice.libraryAPI.member.dto.CreateMember;
import ksp7331.practice.libraryAPI.member.service.port.LibraryMemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;


@ExtendWith(MockitoExtension.class)
class LibraryMemberServiceTest {
    @Mock
    private MemberService memberService;
    @Mock
    private LibraryService libraryService;
    @Mock
    private LibraryMemberRepository libraryMemberRepository;
    @InjectMocks
    private LibraryMemberService libraryMemberService;

    @DisplayName("신규 libraryMember 엔티티 생성 테스트")
    @Test
    void createLibraryMemberFirst() {
        // given
        CreateMember createMember = CreateMember.builder().build();
        long libraryMemberId = 1L;
        LibraryMember libraryMember = LibraryMember.builder().id(libraryMemberId).build();
        BDDMockito.given(libraryMemberRepository.create(Mockito.any(LibraryMember.class))).willReturn(libraryMember);

        // when
        Long result = libraryMemberService.createLibraryMember(createMember);

        // then
        Assertions.assertThat(result).isEqualTo(libraryMemberId);
    }
    @DisplayName("기존 libraryMember에 대해 신규 도서관 등록 테스트")
    @Test
    void createLibraryMember() {
        // given
        long memberId = 1L;
        long libraryMemberId = 1L;
        CreateMember createMember = CreateMember.builder().libraryMemberId(memberId).build();
        LibraryMember libraryMember = LibraryMember.builder().id(libraryMemberId).build();
        BDDMockito.given(libraryMemberRepository.findById(Mockito.anyLong())).willReturn(Optional.ofNullable(LibraryMember.builder().build()));
        BDDMockito.given(libraryMemberRepository.create(Mockito.any(LibraryMember.class))).willReturn(libraryMember);

        // when
        Long result = libraryMemberService.createLibraryMember(createMember);

        // then
        Assertions.assertThat(result).isEqualTo(libraryMemberId);
    }
    @DisplayName("libraryMember 엔티티가 존재할 때 회원 조회")
    @Test
    void findVerifiedMember() {
        // given
        long id = 1L;
        LibraryMember libraryMember = LibraryMember.builder().id(id).build();
        BDDMockito.given(libraryMemberRepository.findById(Mockito.anyLong())).willReturn(Optional.ofNullable(libraryMember));

        // when
        LibraryMember result = libraryMemberService.getById(id);

        // then
        assertThat(result).isEqualTo(libraryMember);
    }
    @DisplayName("libraryMember 엔티티가 존재하지 않을 때 회원 조회")
    @Test
    void findVerifiedMemberWhenNoMember() {
        // given
        long id = 1L;
        given(libraryMemberRepository.findById(Mockito.anyLong())).willReturn(Optional.empty());

        // when // then
        org.junit.jupiter.api.Assertions.assertThrows(BusinessLogicException.class, () -> libraryMemberService.getById(id));
    }
}