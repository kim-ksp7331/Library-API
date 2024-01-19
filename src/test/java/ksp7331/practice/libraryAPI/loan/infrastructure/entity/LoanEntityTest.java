package ksp7331.practice.libraryAPI.loan.infrastructure.entity;

import ksp7331.practice.libraryAPI.book.domain.Book;
import ksp7331.practice.libraryAPI.book.domain.BookState;
import ksp7331.practice.libraryAPI.book.domain.LibraryBook;
import ksp7331.practice.libraryAPI.book.infrastructure.entity.BookEntity;
import ksp7331.practice.libraryAPI.book.infrastructure.entity.LibraryBookEntity;
import ksp7331.practice.libraryAPI.library.domain.Library;
import ksp7331.practice.libraryAPI.library.infrastructure.entity.LibraryEntity;
import ksp7331.practice.libraryAPI.loan.domain.Loan;
import ksp7331.practice.libraryAPI.loan.domain.LoanBook;
import ksp7331.practice.libraryAPI.loan.domain.LoanState;
import ksp7331.practice.libraryAPI.member.domain.LibraryMember;
import ksp7331.practice.libraryAPI.member.domain.Member;
import ksp7331.practice.libraryAPI.member.infrastructure.entity.LibraryMemberEntity;
import ksp7331.practice.libraryAPI.member.infrastructure.entity.MemberEntity;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

class LoanEntityTest {

    @Test
    void from() {
        // given
        Library library = Library.builder().build();
        LibraryMember libraryMember = LibraryMember.builder()
                .id(3L)
                .library(library)
                .member(Member.builder().id(1L).name("kim").build())
                .build();
        LocalDateTime returnDate = LocalDateTime.now().minusDays(2);
        LibraryBook libraryBook = LibraryBook.builder()
                .id(1L).state(BookState.LOANABLE)
                .library(library)
                .book(Book.builder().id(1L).build())
                .build();
        List<LoanBook> loanBooks = List.of(
                LoanBook.builder()
                        .id(2L)
                        .state(LoanState.RETURN)
                        .returnDate(returnDate)
                        .libraryBook(libraryBook)
                        .build()
        );
        Loan domain = Loan.builder()
                .id(1L)
                .libraryMember(libraryMember)
                .loanBooks(loanBooks)
                .build();

        // when
        LoanEntity loan = LoanEntity.from(domain);

        // then
        assertThat(loan.getLibraryMember().getId()).isEqualTo(libraryMember.getId());
        assertThat(loan.getLoanBooks().get(0).getId()).isEqualTo(2L);
        assertThat(loan.getLoanBooks().get(0).getState()).isEqualTo(LoanState.RETURN);
        assertThat(loan.getLoanBooks().get(0).getReturnDate()).isEqualTo(returnDate);
        assertThat(loan.getLoanBooks().get(0).getLibraryBook().getId()).isEqualTo(libraryBook.getId());
        assertThat(loan.getLoanBooks().get(0).getLoan()).isEqualTo(loan);
    }

    @Test
    void update() {
        // given
        LibraryEntity library = LibraryEntity.builder().id(1L).build();
        MemberEntity member = MemberEntity.builder().id(1L).build();
        LibraryMemberEntity libraryMember = LibraryMemberEntity.builder().id(3L).member(member).library(library).build();
        LibraryBook libraryBook = LibraryBook
                .builder().id(1L).state(BookState.NOT_LOANABLE)
                .library(library.toDomain())
                .book(Book.builder().id(1L).build())
                .build();
        List<LoanBookEntity> loanBooks = List.of(
                LoanBookEntity.builder()
                        .id(1L)
                        .state(LoanState.LOANED)
                        .libraryBook(LibraryBookEntity.from(libraryBook))
                        .build(),
                LoanBookEntity.builder()
                        .id(2L)
                        .state(LoanState.LOANED)
                        .libraryBook(LibraryBookEntity.from(libraryBook))
                        .build()
        );


        LoanEntity loan = LoanEntity.builder().id(1L).libraryMember(libraryMember).loanBooks(loanBooks).build();


        LocalDateTime returnDate = LocalDateTime.now().minusDays(2);
        List<LoanBook> updatedLoanBooks = List.of(
                LoanBook.builder()
                        .id(2L)
                        .state(LoanState.RETURN)
                        .returnDate(returnDate)
                        .libraryBook(libraryBook)
                        .build()
        );

        Loan updatedDomain = Loan.builder()
                .id(1L)
                .libraryMember(libraryMember.toDomain())
                .loanBooks(updatedLoanBooks)
                .build();


        // when
        loan.update(updatedDomain);


        // then
        assertThat(loan.getLoanBooks().get(0).getState()).isEqualTo(LoanState.LOANED);
        assertThat(loan.getLoanBooks().get(0).getReturnDate()).isNull();
        assertThat(loan.getLoanBooks().get(1).getState()).isEqualTo(LoanState.RETURN);
        assertThat(loan.getLoanBooks().get(1).getReturnDate()).isEqualTo(returnDate);
    }

    @Test
    void toDomain() {
        // given
        LibraryEntity library = LibraryEntity.builder().id(1L).build();
        LibraryMemberEntity libraryMember = LibraryMemberEntity.builder()
                .member(MemberEntity.builder().id(1L).build()).id(3L)
                .library(library)
                .build();
        LocalDateTime returnDate = LocalDateTime.now().minusDays(2);
        LibraryBookEntity libraryBook = LibraryBookEntity.builder().library(library).book(BookEntity.builder().id(1L).build()).build();
        List<LoanBookEntity> loanBooks = List.of(
                LoanBookEntity.builder()
                        .id(2L)
                        .state(LoanState.RETURN)
                        .returnDate(returnDate)
                        .libraryBook(libraryBook)
                        .build()
        );

        LoanEntity loan = LoanEntity.builder().id(2L).libraryMember(libraryMember).loanBooks(loanBooks).build();

        // when
        Loan result = loan.toDomain();

        // then
        assertThat(result.getId()).isEqualTo(loan.getId());
        assertThat(result.getLibraryMember().getId()).isEqualTo(libraryMember.getId());
        assertThat(result.getLoanBooks().get(0).getId()).isEqualTo(loan.getLoanBooks().get(0).getId());
        assertThat(result.getLoanBooks().get(0).getState()).isEqualTo(loan.getLoanBooks().get(0).getState());
        assertThat(result.getLoanBooks().get(0).getReturnDate()).isEqualTo(loan.getLoanBooks().get(0).getReturnDate());
        assertThat(result.getLoanBooks().get(0).getLibraryBook().getBook().getId())
                .isEqualTo(loan.getLoanBooks().get(0).getLibraryBook().getBook().getId());
    }
}