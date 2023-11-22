package ksp7331.practice.libraryAPI.loan.repository;

import ksp7331.practice.libraryAPI.loan.entity.Loan;

import java.util.List;
import java.util.Optional;

public interface LoanCustomRepository {
    Optional<Loan> findByIdFetchJoin(Long id);


    List<Loan> findAllNotReturned(Long libraryMemberId);

    List<Loan> findByLibraryMemberIdAndMonth(Long libraryMemberId, int year, int month);
}
