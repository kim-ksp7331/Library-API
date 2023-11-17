package ksp7331.practice.libraryAPI.loan.service;

import ksp7331.practice.libraryAPI.book.entity.Book;
import ksp7331.practice.libraryAPI.book.entity.LibraryBook;
import ksp7331.practice.libraryAPI.book.service.LibraryBookService;
import ksp7331.practice.libraryAPI.common.entity.BaseTimeEntity;
import ksp7331.practice.libraryAPI.exception.BusinessLogicException;
import ksp7331.practice.libraryAPI.exception.ExceptionCode;
import ksp7331.practice.libraryAPI.library.entity.Library;
import ksp7331.practice.libraryAPI.loan.dto.LoanServiceDTO;
import ksp7331.practice.libraryAPI.loan.entity.Loan;
import ksp7331.practice.libraryAPI.loan.mapper.LoanMapper;
import ksp7331.practice.libraryAPI.loan.repository.LoanRepository;
import ksp7331.practice.libraryAPI.member.entity.LibraryMember;
import ksp7331.practice.libraryAPI.member.service.LibraryMemberService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
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
    @Mock
    private LoanMapper loanMapper;

    @Test
    @DisplayName("Loan 엔티티 생성")
    void createLoan() {
        // given
        long libraryMemberId = 1L;
        long libraryId = 1L;
        long loanId = 3L;
        List<Long> bookIds = List.of(1L);

        LoanServiceDTO.CreateParam param = LoanServiceDTO.CreateParam.builder()
                .libraryMemberId(libraryMemberId).bookIds(bookIds).build();
        LibraryMember libraryMember = LibraryMember.builder().id(libraryMemberId).library(Library.builder().id(libraryId).build()).build();
        List<LibraryBook> libraryBooks = List.of(LibraryBook.builder().build());
        Loan loan = Loan.builder().id(loanId).libraryMember(libraryMember).libraryBooks(libraryBooks).build();

        BDDMockito.given(libraryMemberService.findVerifiedLibraryMember(libraryMemberId)).willReturn(libraryMember);
        BDDMockito.given(loanRepository.findAllNotReturned(Mockito.anyLong())).willReturn(List.of());
        BDDMockito.given(libraryBookService.findExistBookInLibrary(libraryId, bookIds)).willReturn(libraryBooks);
        BDDMockito.given(loanRepository.save(Mockito.any(Loan.class))).willReturn(loan);

        // when
        Long result = loanService.createLoan(param);

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

        LoanServiceDTO.CreateParam param = LoanServiceDTO.CreateParam.builder()
                .libraryMemberId(libraryMemberId).bookIds(bookIds).build();
        LibraryMember libraryMember = LibraryMember.builder().id(libraryMemberId).library(Library.builder().id(libraryId).build()).build();
        libraryMember.addLoanBooksCount(loanedBookCount);

        List<LibraryBook> libraryBooks = new ArrayList<>();
        IntStream.rangeClosed(1, bookIds.size()).forEach((i) -> libraryBooks.add(LibraryBook.builder().build()));

        BDDMockito.given(libraryMemberService.findVerifiedLibraryMember(libraryMemberId)).willReturn(libraryMember);
        BDDMockito.given(loanRepository.findAllNotReturned(Mockito.anyLong())).willReturn(List.of());
        BDDMockito.given(libraryBookService.findExistBookInLibrary(libraryId, bookIds)).willReturn(libraryBooks);


        // when // then
        BusinessLogicException exception = assertThrows(BusinessLogicException.class, () -> loanService.createLoan(param));
        assertThat(exception.getExceptionCode()).isEqualTo(ExceptionCode.LOAN_EXCEEDED);
    }

    @Test
    @DisplayName("연체중인 상황에서 Loan 엔티티를 생성하는 경우")
    void createLoanWhenOverbooked() throws NoSuchFieldException, IllegalAccessException {
        // given
        long libraryMemberId = 1L;
        long libraryId = 1L;

        LoanServiceDTO.CreateParam param = LoanServiceDTO.CreateParam.builder()
                .libraryMemberId(libraryMemberId).build();
        LibraryMember libraryMember = LibraryMember.builder().id(libraryMemberId).library(Library.builder().id(libraryId).build()).build();
        Loan loan = Loan.builder().build();

        Field field = BaseTimeEntity.class.getDeclaredField("createdDate");
        field.setAccessible(true);
        field.set(loan, LocalDateTime.now().minusDays(20));
        List<Loan> loans = List.of(loan);


        BDDMockito.given(libraryMemberService.findVerifiedLibraryMember(libraryMemberId)).willReturn(libraryMember);
        BDDMockito.given(loanRepository.findAllNotReturned(Mockito.anyLong())).willReturn(loans);


        // when // then
        BusinessLogicException exception = assertThrows(BusinessLogicException.class, () -> loanService.createLoan(param));
        assertThat(exception.getExceptionCode()).isEqualTo(ExceptionCode.LOAN_RESTRICTED);
    }

    @Test
    @DisplayName("연체 후 반납으로 인해 설정된 대출 가능 일자 전에 Loan 엔티티를 생성한 경우")
    void createLoanBeforeLoanableDay() {
        // given
        long libraryMemberId = 1L;
        long libraryId = 1L;

        LoanServiceDTO.CreateParam param = LoanServiceDTO.CreateParam.builder()
                .libraryMemberId(libraryMemberId).build();
        LibraryMember libraryMember = LibraryMember.builder().id(libraryMemberId).library(Library.builder().id(libraryId).build()).build();
        libraryMember.setLoanAvailableDay(LocalDate.now().plusDays(3));
        BDDMockito.given(libraryMemberService.findVerifiedLibraryMember(libraryMemberId)).willReturn(libraryMember);

        // when // then
        BusinessLogicException exception = assertThrows(BusinessLogicException.class, () -> loanService.createLoan(param));
        assertThat(exception.getExceptionCode()).isEqualTo(ExceptionCode.LOAN_RESTRICTED);
    }

    @Test
    @DisplayName("Loan 엔티티에 있는 도서중 일부를 반납")
    void returnBook() throws NoSuchFieldException, IllegalAccessException {
        // given
        Long loanId = 1L;
        long bookId = 1L;
        List<Long> bookIds = List.of(bookId);
        List<LibraryBook> libraryBooks = List.of(LibraryBook.builder().book(Book.builder().id(bookId).build()).build());
        LibraryMember libraryMember = LibraryMember.builder().build();

        LoanServiceDTO.ReturnBookParam param = LoanServiceDTO.ReturnBookParam.builder().loanId(loanId).bookIds(bookIds).build();
        Loan loan = Loan.builder().id(loanId).libraryMember(libraryMember).libraryBooks(libraryBooks).build();
        LoanServiceDTO.Result resultDTO = LoanServiceDTO.Result.builder().build();

        Field field = BaseTimeEntity.class.getDeclaredField("createdDate");
        field.setAccessible(true);
        field.set(loan, LocalDateTime.now());

        BDDMockito.given(loanRepository.findByIdFetchJoin(Mockito.anyLong())).willReturn(Optional.of(loan));
        BDDMockito.given(loanMapper.entitiesToServiceDTOs(loan)).willReturn(resultDTO);

        // when
        LoanServiceDTO.Result result = loanService.returnBook(param);

        // then
        assertThat(result).isEqualTo(resultDTO);
    }

    @Test
    @DisplayName("Loan 엔티티 조회")
    void findLoan() {
        // given
        Long loanId = 1L;
        Loan loan = Loan.builder().build();
        LoanServiceDTO.Result loanDTO = LoanServiceDTO.Result.builder().build();
        BDDMockito.given(loanRepository.findByIdFetchJoin(Mockito.anyLong())).willReturn(Optional.of(loan));
        BDDMockito.given(loanMapper.entitiesToServiceDTOs(loan)).willReturn(loanDTO);

        // when
        LoanServiceDTO.Result result = loanService.findLoan(loanId);

        // then
        assertThat(result).isEqualTo(loanDTO);
    }
}