package ksp7331.practice.libraryAPI.loan.service;

import ksp7331.practice.libraryAPI.book.entity.LibraryBook;
import ksp7331.practice.libraryAPI.book.service.LibraryBookService;
import ksp7331.practice.libraryAPI.exception.BusinessLogicException;
import ksp7331.practice.libraryAPI.exception.ExceptionCode;
import ksp7331.practice.libraryAPI.loan.dto.LoanServiceDTO;
import ksp7331.practice.libraryAPI.loan.entity.Loan;
import ksp7331.practice.libraryAPI.loan.repository.LoanRepository;
import ksp7331.practice.libraryAPI.member.entity.LibraryMember;
import ksp7331.practice.libraryAPI.member.service.LibraryMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class LoanService {
    private final LoanRepository loanRepository;
    private final LibraryMemberService libraryMemberService;
    private final LibraryBookService libraryBookService;
    public Long createLoan(LoanServiceDTO.CreateParam param) {
        LibraryMember libraryMember = libraryMemberService.findVerifiedLibraryMember(param.getLibraryMemberId());
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
}
