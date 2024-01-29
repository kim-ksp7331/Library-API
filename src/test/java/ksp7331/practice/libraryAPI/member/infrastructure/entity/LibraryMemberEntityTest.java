package ksp7331.practice.libraryAPI.member.infrastructure.entity;

import ksp7331.practice.libraryAPI.book.infrastructure.entity.BookEntity;
import ksp7331.practice.libraryAPI.book.infrastructure.entity.LibraryBookEntity;
import ksp7331.practice.libraryAPI.library.domain.Library;
import ksp7331.practice.libraryAPI.library.infrastructure.entity.LibraryEntity;
import ksp7331.practice.libraryAPI.loan.infrastructure.entity.LoanBookEntity;
import ksp7331.practice.libraryAPI.loan.infrastructure.entity.LoanEntity;
import ksp7331.practice.libraryAPI.member.domain.LibraryMember;
import ksp7331.practice.libraryAPI.member.domain.Member;
import ksp7331.practice.libraryAPI.member.domain.Phone;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class LibraryMemberEntityTest {

    @Test
    void from() {
        // given
        LocalDate localDate = LocalDate.now().minusDays(2);
        List<Phone> phones = List.of(Phone.builder().id(2L).number("010-0000-0000").build());
        LibraryMember domain = LibraryMember.builder()
                .id(2L)
                .member(Member.builder().id(1L).name("kim").build())
                .library(Library.builder().id(3L).name("lib1").build())
                .loanAvailableDay(localDate)
                .loanBooksCount(1)
                .phones(phones)
                .build();

        // when
        LibraryMemberEntity libraryMember = LibraryMemberEntity.from(domain);

        // then
        assertThat(libraryMember.getId()).isEqualTo(2L);
        assertThat(libraryMember.getLoanAvailableDay()).isEqualTo(localDate);
        assertThat(libraryMember.getLoanBooksCount()).isEqualTo(1);
        assertThat(libraryMember.getMember().getId()).isEqualTo(1L);
        assertThat(libraryMember.getMember().getName()).isEqualTo("kim");
        assertThat(libraryMember.getLibrary().getId()).isEqualTo(3L);
        assertThat(libraryMember.getLibrary().getName()).isEqualTo("lib1");
        assertThat(libraryMember.getPhones().get(0).getNumber()).isEqualTo("010-0000-0000");
    }

    @Test
    void toDomain() {
        // given
        LocalDate localDate = LocalDate.now().minusDays(2);
        List<PhoneEntity> phones = List.of(PhoneEntity.builder().id(2L).number("010-0000-0000").build());
        LibraryEntity library = LibraryEntity.builder().id(3L).name("lib1").build();
        LibraryMemberEntity entity = LibraryMemberEntity.builder()
                .id(2L)
                .member(MemberEntity.builder().id(1L).name("kim").build())
                .library(library)
                .loanAvailableDay(localDate)
                .loanBooksCount(1)
                .phones(phones)
                .build();

        LoanEntity.builder().id(5L).libraryMember(entity)
                .loanBooks(List.of(LoanBookEntity.builder()
                        .libraryBook(LibraryBookEntity.builder().library(library).book(BookEntity.builder().build()).build())
                        .build()))
                .build();

        // when
        LibraryMember libraryMember = entity.toDomain();

        // then
        assertThat(libraryMember.getId()).isEqualTo(2L);
        assertThat(libraryMember.getLoanAvailableDay()).isEqualTo(localDate);
        assertThat(libraryMember.getLoanBooksCount()).isEqualTo(1);
        assertThat(libraryMember.getMember().getId()).isEqualTo(1L);
        assertThat(libraryMember.getMember().getName()).isEqualTo("kim");
        assertThat(libraryMember.getLibrary().getId()).isEqualTo(3L);
        assertThat(libraryMember.getLibrary().getName()).isEqualTo("lib1");
        assertThat(libraryMember.getPhones().get(0).getNumber()).isEqualTo("010-0000-0000");
        assertThat(libraryMember.getLoans().get(0).getId()).isEqualTo(5L);
    }
}