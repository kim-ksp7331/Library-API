package ksp7331.practice.libraryAPI.loan.service;

import ksp7331.practice.libraryAPI.book.entity.LibraryBook;
import ksp7331.practice.libraryAPI.book.service.LibraryBookService;
import ksp7331.practice.libraryAPI.library.entity.Library;
import ksp7331.practice.libraryAPI.loan.dto.LoanServiceDTO;
import ksp7331.practice.libraryAPI.loan.entity.Loan;
import ksp7331.practice.libraryAPI.loan.repository.LoanRepository;
import ksp7331.practice.libraryAPI.member.entity.LibraryMember;
import ksp7331.practice.libraryAPI.member.service.LibraryMemberService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

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
    void createLoan() {
        // given
        long libraryMemberId = 1L;
        long libraryId = 1L;
        long loanId = 3L;
        List<Long> bookIds = List.of(1L);

        LoanServiceDTO.CreateParam param = LoanServiceDTO.CreateParam.builder()
                .libraryMemberId(libraryMemberId).bookIds(bookIds).build();
        LibraryMember libraryMember = LibraryMember.builder().library(Library.builder().id(libraryId).build()).build();
        List<LibraryBook> libraryBooks = List.of(LibraryBook.builder().build());
        Loan loan = Loan.builder().id(loanId).libraryBooks(libraryBooks).build();

        BDDMockito.given(libraryMemberService.findVerifiedLibraryMember(libraryMemberId)).willReturn(libraryMember);
        BDDMockito.given(libraryBookService.findExistBookInLibrary(libraryId, bookIds)).willReturn(libraryBooks);
        BDDMockito.given(loanRepository.save(Mockito.any(Loan.class))).willReturn(loan);

        // when
        Long result = loanService.createLoan(param);

        // then
        assertThat(result).isEqualTo(loanId);
    }
}