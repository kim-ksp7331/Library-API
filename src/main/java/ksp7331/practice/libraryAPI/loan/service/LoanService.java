package ksp7331.practice.libraryAPI.loan.service;

import ksp7331.practice.libraryAPI.book.entity.LibraryBook;
import ksp7331.practice.libraryAPI.book.service.LibraryBookService;
import ksp7331.practice.libraryAPI.exception.BusinessLogicException;
import ksp7331.practice.libraryAPI.exception.ExceptionCode;
import ksp7331.practice.libraryAPI.loan.dto.LoanServiceDTO;
import ksp7331.practice.libraryAPI.loan.entity.Loan;
import ksp7331.practice.libraryAPI.loan.mapper.LoanMapper;
import ksp7331.practice.libraryAPI.loan.repository.LoanRepository;
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
    private final LoanMapper loanMapper;
    public Long createLoan(LoanServiceDTO.CreateParam param) {
        LibraryMember libraryMember = libraryMemberService.findVerifiedLibraryMember(param.getLibraryMemberId());
        checkLoanable(libraryMember);

        List<LibraryBook> libraryBooks = libraryBookService.findExistBookInLibrary(libraryMember.getLibrary().getId(), param.getBookIds());
        // 빌릴 도서가 존재하지 않는 경우 예외 처리
        if(param.getBookIds().size() != libraryBooks.size()) throw new BusinessLogicException(ExceptionCode.BOOK_NOT_FOUND);

        // 정상 흐름
        Loan loan = Loan.builder()
                .libraryMember(libraryMember)
                .libraryBooks(libraryBooks)
                .build();

        return loanRepository.save(loan).getId();
    }

    public LoanServiceDTO.Result returnBook(LoanServiceDTO.ReturnBookParam param) {
        Loan loan = findVerifiedLoan(param.getLoanId());
        loan.returnBooks(param.getBookIds());
        return loanMapper.entityToServiceDTO(loan);
    }

    public LoanServiceDTO.Result findLoan(Long loanId) {
        return loanMapper.entityToServiceDTO(findVerifiedLoan(loanId));
    }

    public List<LoanServiceDTO.Result> findLoanByMonth(Long libraryMemberId, int year, int month) {
        List<Loan> loans = loanRepository.findByLibraryMemberIdAndMonth(libraryMemberId, year, month);
        return loanMapper.entitiesToServiceDTO(loans);
    }

    private Loan findVerifiedLoan(Long loanId) {
        Optional<Loan> optionalLoan = loanRepository.findByIdFetchJoin(loanId);
        return optionalLoan.orElseThrow(() -> new BusinessLogicException(ExceptionCode.LOAN_NOT_FOUND));
    }

    private void checkLoanable(LibraryMember libraryMember) {
        if(LocalDate.now().isBefore(libraryMember.getLoanAvailableDay())) throw new BusinessLogicException(ExceptionCode.LOAN_RESTRICTED);
        List<Loan> loans = loanRepository.findAllNotReturned(libraryMember.getId());
        loans.forEach(Loan::checkOverDue);
    }
}
