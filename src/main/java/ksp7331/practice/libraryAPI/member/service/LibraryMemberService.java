package ksp7331.practice.libraryAPI.member.service;

import ksp7331.practice.libraryAPI.exception.BusinessLogicException;
import ksp7331.practice.libraryAPI.exception.ExceptionCode;
import ksp7331.practice.libraryAPI.library.domain.Library;
import ksp7331.practice.libraryAPI.library.service.LibraryService;
import ksp7331.practice.libraryAPI.member.domain.LibraryMember;
import ksp7331.practice.libraryAPI.member.domain.Member;
import ksp7331.practice.libraryAPI.member.dto.CreateMember;
import ksp7331.practice.libraryAPI.member.service.port.LibraryMemberRepository;
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

    public Long createLibraryMember(CreateMember createMember) {
        Member member = getMember(createMember.getLibraryMemberId(), createMember.getName());
        Library library = getLibrary(createMember.getLibraryId());
        LibraryMember libraryMember = LibraryMember.from(member, library, createMember.getPhoneNumber());
        return libraryMemberRepository.create(libraryMember).getId();
    }

    public void deleteLibraryMembers(Long id) {
        LibraryMember libraryMember = getById(id);
        libraryMemberRepository.delete(libraryMember);
        memberService.deleteMember(libraryMember.getMember());
    }

    public LibraryMember getById(Long id) {
        Optional<LibraryMember> optionalMember = libraryMemberRepository.findById(id);
        return optionalMember.orElseThrow(() -> new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND));
    }

    private Member getMember(Long id, String name) {
        return id == null ? memberService.createMember(name) : getById(id).getMember();
    }

    private Library getLibrary(Long id) {
        return libraryService.getById(id);
    }
}
