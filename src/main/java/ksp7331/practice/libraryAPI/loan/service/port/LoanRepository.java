package ksp7331.practice.libraryAPI.loan.service.port;

import ksp7331.practice.libraryAPI.loan.domain.Loan;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public interface LoanRepository {
    Loan create(Loan loan);

    Loan update(Loan loan);

    List<Loan> findByLibraryMemberIdAndMonth(Long libraryMemberId, int year, int month);

    Optional<Loan> findById(Long id);
    List<Loan> findAllNotReturned(Long libraryMemberId);
}
