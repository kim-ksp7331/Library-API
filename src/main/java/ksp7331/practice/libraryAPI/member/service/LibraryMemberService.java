package ksp7331.practice.libraryAPI.member.service;

import ksp7331.practice.libraryAPI.exception.BusinessLogicException;
import ksp7331.practice.libraryAPI.exception.ExceptionCode;
import ksp7331.practice.libraryAPI.library.entity.Library;
import ksp7331.practice.libraryAPI.library.service.LibraryService;
import ksp7331.practice.libraryAPI.member.dto.MemberServiceDTO;
import ksp7331.practice.libraryAPI.member.entity.LibraryMember;
import ksp7331.practice.libraryAPI.member.entity.Member;
import ksp7331.practice.libraryAPI.member.repository.LibraryMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class LibraryMemberService {
    private final MemberService memberService;
    private final LibraryService libraryService;
    private final LibraryMemberRepository libraryMemberRepository;

    public Long createLibraryMember(MemberServiceDTO.CreateParam createParam) {
        Member member = getMember(createParam.getLibraryMemberId(), createParam.getName());
        Library library = getLibrary(createParam.getLibraryId());
        LibraryMember libraryMember = LibraryMember.builder()
                .member(member)
                .library(library)
                .phone(createParam.getPhoneNumber())
                .build();
        return libraryMemberRepository.save(libraryMember).getId();
    }

    public void deleteLibraryMembers(Long id) {
        LibraryMember libraryMember = findVerifiedLibraryMember(id);
        memberService.deleteMember(libraryMember.getMember());
    }

    public LibraryMember findVerifiedLibraryMember(Long id) {
        Optional<LibraryMember> optionalMember = libraryMemberRepository.findById(id);
        return optionalMember.orElseThrow(() -> new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND));
    }

    private Member getMember(Long id, String name) {
        return id == null ? memberService.createMember(name) : findVerifiedLibraryMember(id).getMember();
    }

    private Library getLibrary(Long id) {
        return libraryService.findVerifiedLibrary(id);
    }
}
