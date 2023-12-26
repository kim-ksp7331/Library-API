package ksp7331.practice.libraryAPI.loan.service;

import ksp7331.practice.libraryAPI.book.domain.LibraryBook;
import ksp7331.practice.libraryAPI.book.service.LibraryBookService;
import ksp7331.practice.libraryAPI.exception.BusinessLogicException;
import ksp7331.practice.libraryAPI.exception.ExceptionCode;
import ksp7331.practice.libraryAPI.loan.domain.Loan;
import ksp7331.practice.libraryAPI.loan.dto.CreateLoan;
import ksp7331.practice.libraryAPI.loan.dto.ReturnBook;
import ksp7331.practice.libraryAPI.loan.service.port.LoanRepository;
import ksp7331.practice.libraryAPI.member.entity.LibraryMember;
import ksp7331.practice.libraryAPI.member.service.LibraryMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class LoanService {
    private final LoanRepository loanRepository;
    private final LibraryMemberService libraryMemberService;
    private final LibraryBookService libraryBookService;
    public Long createLoan(CreateLoan createLoan) {
        LibraryMember libraryMember = libraryMemberService.findVerifiedLibraryMember(createLoan.getLibraryMemberId());
        checkLoanable(libraryMember);

        List<LibraryBook> libraryBooks = libraryBookService.findExistBookInLibrary(libraryMember.getLibrary().getId(), createLoan.getBookIds());
        // 빌릴 도서가 존재하지 않는 경우 예외 처리
        if(createLoan.getBookIds().size() != libraryBooks.size()) throw new BusinessLogicException(ExceptionCode.BOOK_NOT_FOUND);

        // 정상 흐름
        Loan loan = Loan.from(libraryMember, libraryBooks);
        return loanRepository.create(loan).getId();
    }

    public Loan returnBook(ReturnBook returnBook) {
        Loan loan = getByIdInternal(returnBook.getLoanId());
        loan.returnBooks(returnBook.getBookIds());
        return loanRepository.update(loan);
    }

    public Loan getById(Long loanId) {
        return getByIdInternal(loanId);
    }

    public List<Loan> findByLibraryMemberIdAndMonth(Long libraryMemberId, int year, int month) {
        List<Loan> loans = loanRepository.findByLibraryMemberIdAndMonth(libraryMemberId, year, month);
        return loans;
    }

    private Loan getByIdInternal(Long loanId) {
        Optional<Loan> optionalLoan = loanRepository.findById(loanId);
        return optionalLoan.orElseThrow(() -> new BusinessLogicException(ExceptionCode.LOAN_NOT_FOUND));
    }

    private void checkLoanable(LibraryMember libraryMember) {
        if(LocalDate.now().isBefore(libraryMember.getLoanAvailableDay())) throw new BusinessLogicException(ExceptionCode.LOAN_RESTRICTED);
        List<Loan> loans = loanRepository.findAllNotReturned(libraryMember.getId());
        loans.forEach(Loan::checkOverDue);
    }
}
