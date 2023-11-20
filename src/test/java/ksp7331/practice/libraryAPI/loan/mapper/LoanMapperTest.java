package ksp7331.practice.libraryAPI.loan.mapper;

import ksp7331.practice.libraryAPI.book.entity.Book;
import ksp7331.practice.libraryAPI.book.entity.LibraryBook;
import ksp7331.practice.libraryAPI.library.entity.Library;
import ksp7331.practice.libraryAPI.loan.dto.LoanControllerDTO;
import ksp7331.practice.libraryAPI.loan.dto.LoanServiceDTO;
import ksp7331.practice.libraryAPI.loan.entity.Loan;
import ksp7331.practice.libraryAPI.member.entity.LibraryMember;
import ksp7331.practice.libraryAPI.member.entity.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class LoanMapperTest {
    private LoanMapper loanMapper = new LoanMapper();

    @Test
    void entitiesToServiceDTOs() {
        // given
        long loanId = 1L;
        long libraryMemberId = 1L;
        String memberName = "kim";
        String libraryName = "lib1";

        Library library = Library.builder().name(libraryName).build();

        LibraryMember libraryMember = LibraryMember.builder()
                .id(libraryMemberId)
                .member(Member.builder().name(memberName).build())
                .library(library)
                .build();

        int repeat = 2;
        String bookName = "book";
        String author = "author";
        String publisher = "publisher";
        List<LibraryBook> libraryBooks = LongStream.rangeClosed(1, repeat).mapToObj(i -> LibraryBook.builder().library(library)
                .book(Book.builder().id(i).name(bookName + i).author(author + i).publisher(publisher + i).build()).build()).collect(Collectors.toList());

        Loan loan = Loan.builder()
                .id(loanId)
                .libraryMember(libraryMember)
                .libraryBooks(libraryBooks)
                .build();

        // when
        LoanServiceDTO.Result result = loanMapper.entitiesToServiceDTOs(loan);

        // then
        assertThat(result.getId()).isEqualTo(loanId);
        assertThat(result.getMemberName()).isEqualTo(memberName);
        assertThat(result.getLibraryName()).isEqualTo(libraryName);
        IntStream.rangeClosed(0, repeat - 1).forEach(i -> {
            LoanServiceDTO.Result.Book book = result.getBooks().get(i);
            assertThat(book.getId()).isEqualTo(i + 1);
            assertThat(book.getName()).startsWith(bookName);
            assertThat(book.getAuthor()).startsWith(author);
            assertThat(book.getPublisher()).startsWith(publisher);
        });
    }

    @Test
    void serviceDTOToControllerDTO() {
        // given
        long loanId = 1L;
        long libraryMemberId = 1L;
        String memberName = "kim";
        String libraryName = "lib1";
        LocalDateTime createdDate = LocalDateTime.now();

        int repeat = 2;
        String bookName = "book";
        String author = "author";
        String publisher = "publisher";
        List<LoanServiceDTO.Result.Book> books = LongStream.rangeClosed(1, repeat).mapToObj(i -> LoanServiceDTO.Result.Book.builder()
                .id(i).name(bookName + i).author(author + i).publisher(publisher + i).build()).collect(Collectors.toList());

        LoanServiceDTO.Result loanDTO = LoanServiceDTO.Result.builder()
                .id(loanId)
                .libraryMemberId(libraryMemberId)
                .memberName(memberName)
                .libraryName(libraryName)
                .createdDate(createdDate)
                .books(books)
                .build();

        // when
        LoanControllerDTO.Response response = loanMapper.serviceDTOToControllerDTO(loanDTO);

        // then
        assertThat(response.getId()).isEqualTo(loanId);
        assertThat(response.getLibraryMemberId()).isEqualTo(libraryMemberId);
        assertThat(response.getMemberName()).isEqualTo(memberName);
        assertThat(response.getLibraryName()).isEqualTo(libraryName);
        assertThat(response.getCreatedDate()).isEqualTo(createdDate);
        IntStream.rangeClosed(0, repeat - 1).forEach(i -> {
            LoanControllerDTO.Response.Book book = response.getBooks().get(i);
            assertThat(book.getId()).isEqualTo(i + 1);
            assertThat(book.getName()).startsWith(bookName);
            assertThat(book.getAuthor()).startsWith(author);
            assertThat(book.getPublisher()).startsWith(publisher);
        });
    }
}