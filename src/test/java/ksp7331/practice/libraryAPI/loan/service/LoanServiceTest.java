package ksp7331.practice.libraryAPI.loan.service;

import ksp7331.practice.libraryAPI.book.domain.Book;
import ksp7331.practice.libraryAPI.book.domain.LibraryBook;
import ksp7331.practice.libraryAPI.book.service.LibraryBookService;
import ksp7331.practice.libraryAPI.exception.BusinessLogicException;
import ksp7331.practice.libraryAPI.exception.ExceptionCode;
import ksp7331.practice.libraryAPI.library.domain.Library;
import ksp7331.practice.libraryAPI.loan.domain.Loan;
import ksp7331.practice.libraryAPI.loan.domain.LoanBook;
import ksp7331.practice.libraryAPI.loan.domain.LoanState;
import ksp7331.practice.libraryAPI.loan.dto.CreateLoan;
import ksp7331.practice.libraryAPI.loan.dto.ReturnBook;
import ksp7331.practice.libraryAPI.loan.service.port.LoanRepository;
import ksp7331.practice.libraryAPI.member.domain.LibraryMember;
import ksp7331.practice.libraryAPI.member.service.LibraryMemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class LoanServiceTest {
    @InjectMocks
    private LoanService loanService;
    @Mock
    private LoanRepository loanRepository;
    @Mock
    private LibraryMemberService libraryMemberService;
    @Mock
    private LibraryBookService libraryBookService;

    @Test
    @DisplayName("Loan 엔티티 생성")
    void createLoan() {
        // given
        long libraryMemberId = 1L;
        long libraryId = 1L;
        long loanId = 3L;
        List<Long> bookIds = List.of(1L);

        CreateLoan createLoan = CreateLoan.builder().bookIds(bookIds).libraryMemberId(libraryMemberId).build();
        LibraryMember libraryMember = LibraryMember.builder().id(libraryMemberId).library(Library.builder().id(libraryId).build()).build();
        List<LibraryBook> libraryBooks = List.of(LibraryBook.builder().build());

        Loan loan = Loan.builder().id(loanId).libraryMember(libraryMember).build();

        BDDMockito.given(libraryMemberService.getById(libraryMemberId)).willReturn(libraryMember);
        BDDMockito.given(loanRepository.findAllNotReturned(Mockito.anyLong())).willReturn(List.of());
        BDDMockito.given(libraryBookService.findExistBookInLibrary(libraryId, bookIds)).willReturn(libraryBooks);
        BDDMockito.given(loanRepository.create(Mockito.any(Loan.class))).willReturn(loan);

        // when
        Long result = loanService.createLoan(createLoan);

        // then
        assertThat(result).isEqualTo(loanId);
    }

    @Test
    @DisplayName("Loan 엔티티 생성 시 한도 이상의 책을 대출하는 경우")
    void createLoanWithExceededBooks() {
        // given
        long libraryMemberId = 1L;
        long libraryId = 1L;
        int loanedBookCount = 2;
        List<Long> bookIds = List.of(1L, 2L, 3L, 4L);

        CreateLoan createLoan = CreateLoan.builder().bookIds(bookIds).libraryMemberId(libraryMemberId).build();

        LibraryMember libraryMember = LibraryMember.builder().id(libraryMemberId).library(Library.builder().id(libraryId).build()).build();
        libraryMember.addLoanBooksCount(loanedBookCount);

        List<LibraryBook> libraryBooks = new ArrayList<>();
        IntStream.rangeClosed(1, bookIds.size()).forEach((i) -> libraryBooks.add(LibraryBook.builder().build()));

        BDDMockito.given(libraryMemberService.getById(libraryMemberId)).willReturn(libraryMember);
        BDDMockito.given(loanRepository.findAllNotReturned(Mockito.anyLong())).willReturn(List.of());
        BDDMockito.given(libraryBookService.findExistBookInLibrary(libraryId, bookIds)).willReturn(libraryBooks);


        // when // then
        BusinessLogicException exception = assertThrows(BusinessLogicException.class, () -> loanService.createLoan(createLoan));
        assertThat(exception.getExceptionCode()).isEqualTo(ExceptionCode.LOAN_EXCEEDED);
    }

    @Test
    @DisplayName("연체중인 상황에서 Loan 엔티티를 생성하는 경우")
    void createLoanWhenOverbooked() {
        // given
        long libraryMemberId = 1L;
        long libraryId = 1L;

        CreateLoan createLoan = CreateLoan.builder().libraryMemberId(libraryMemberId).build();

        LibraryMember libraryMember = LibraryMember.builder().id(libraryMemberId).library(Library.builder().id(libraryId).build()).build();

        Loan loan = Loan.builder().libraryMember(libraryMember).createdDate(LocalDateTime.now().minusDays(20)).build();


        BDDMockito.given(libraryMemberService.getById(libraryMemberId)).willReturn(libraryMember);
        BDDMockito.given(loanRepository.findAllNotReturned(Mockito.anyLong())).willReturn(List.of(loan));


        // when // then
        BusinessLogicException exception = assertThrows(BusinessLogicException.class, () -> loanService.createLoan(createLoan));
        assertThat(exception.getExceptionCode()).isEqualTo(ExceptionCode.LOAN_RESTRICTED);
    }

    @Test
    @DisplayName("연체 후 반납으로 인해 설정된 대출 가능 일자 전에 Loan 엔티티를 생성한 경우")
    void createLoanBeforeLoanableDay() {
        // given
        long libraryMemberId = 1L;
        long libraryId = 1L;

        CreateLoan createLoan = CreateLoan.builder().libraryMemberId(libraryMemberId).build();
        LibraryMember libraryMember = LibraryMember.builder().id(libraryMemberId).library(Library.builder().id(libraryId).build()).build();
        libraryMember.setLoanAvailableDay(LocalDate.now().plusDays(3));
        BDDMockito.given(libraryMemberService.getById(libraryMemberId)).willReturn(libraryMember);

        // when // then
        BusinessLogicException exception = assertThrows(BusinessLogicException.class, () -> loanService.createLoan(createLoan));
        assertThat(exception.getExceptionCode()).isEqualTo(ExceptionCode.LOAN_RESTRICTED);
    }

    @Test
    @DisplayName("Loan 엔티티에 있는 도서중 일부를 반납")
    void returnBook(){
        // given
        Long loanId = 1L;
        long bookId = 1L;
        List<Long> bookIds = List.of(bookId);

        ReturnBook returnBook = ReturnBook.builder().loanId(loanId).bookIds(bookIds).build();

        LoanBook loanBook = LoanBook.builder()
                .libraryBook(LibraryBook.builder().book(Book.builder()
                                .id(bookId)
                                .build())
                        .build())
                .state(LoanState.LOANED)
                .build();

        Loan loan = Loan.builder()
                .id(loanId)
                .libraryMember(LibraryMember.builder()
                        .build())
                .loanBooks(List.of(loanBook))
                .createdDate(LocalDateTime.now())
                .build();
        BDDMockito.given(loanRepository.findById(loanId)).willReturn(Optional.ofNullable(loan));
        BDDMockito.given(loanRepository.update(Mockito.any(Loan.class))).willReturn(loan);

        // when
        Loan result = loanService.returnBook(returnBook);

        // then
        assertThat(result).isEqualTo(loan);
    }

    @Test
    @DisplayName("Loan 엔티티 조회")
    void findLoan() {
        // given
        Long loanId = 1L;
        LoanBook loanBook = LoanBook.builder()
                .libraryBook(LibraryBook.builder().book(Book.builder()
                                .build())
                        .build())
                .state(LoanState.LOANED)
                .build();

        Loan loan = Loan.builder()
                .id(loanId)
                .libraryMember(LibraryMember.builder()
                        .build())
                .loanBooks(List.of(loanBook))
                .createdDate(LocalDateTime.now())
                .build();

        BDDMockito.given(loanRepository.findById(Mockito.anyLong())).willReturn(Optional.of(loan));


        // when
        Loan result = loanService.getById(loanId);

        // then
        assertThat(result).isEqualTo(loan);
    }
}