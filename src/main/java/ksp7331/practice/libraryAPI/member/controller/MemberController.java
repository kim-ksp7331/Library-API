package ksp7331.practice.libraryAPI.member.controller;

import ksp7331.practice.libraryAPI.member.dto.MemberControllerDTO;
import ksp7331.practice.libraryAPI.member.mapper.MemberMapper;
import ksp7331.practice.libraryAPI.member.service.LibraryMemberService;
import ksp7331.practice.libraryAPI.util.UriCreator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {
    private static String MEMBER_URL_PREFIX = "/members";
    private final LibraryMemberService libraryMemberService;
    private final MemberMapper memberMapper;

    @PostMapping
    public ResponseEntity<Void> postMember(@RequestBody MemberControllerDTO.Post post) {
        Long libraryMemberId = libraryMemberService.createLibraryMember(memberMapper.postToCreateParam(post));
        URI uri = UriCreator.createUri(MEMBER_URL_PREFIX, libraryMemberId);
        return ResponseEntity.created(uri).build();
    }

    @DeleteMapping("/{library-member-id}")
    public ResponseEntity<Void> deleteMember(@PathVariable("library-member-id") Long libraryMemberId) {
        libraryMemberService.deleteLibraryMembers(libraryMemberId);
        return ResponseEntity.noContent().build();
    }
}
