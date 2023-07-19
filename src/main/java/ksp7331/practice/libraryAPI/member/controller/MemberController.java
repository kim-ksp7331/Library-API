package ksp7331.practice.libraryAPI.member.controller;

import ksp7331.practice.libraryAPI.member.dto.MemberControllerDTO;
import ksp7331.practice.libraryAPI.member.mapper.MemberMapper;
import ksp7331.practice.libraryAPI.member.service.LibraryMemberService;
import ksp7331.practice.libraryAPI.util.UriCreator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
        Long id = libraryMemberService.createLibraryMember(memberMapper.postToCreateParam(post));
        URI uri = UriCreator.createUri(MEMBER_URL_PREFIX, id);
        return ResponseEntity.created(uri).build();
    }
}
