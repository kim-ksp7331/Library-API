package ksp7331.practice.libraryAPI.member.mapper;

import ksp7331.practice.libraryAPI.member.dto.MemberControllerDTO;
import ksp7331.practice.libraryAPI.member.dto.MemberServiceDTO;
import org.springframework.stereotype.Component;

@Component
public class MemberMapper {
    public MemberServiceDTO.CreateParam postToCreateParam(MemberControllerDTO.Post post) {
        return MemberServiceDTO.CreateParam.builder()
                .libraryMemberId(post.getLibraryMemberId())
                .name(post.getName())
                .phoneNumber(post.getPhoneNumber())
                .libraryId(post.getLibraryId())
                .build();
    }
}
