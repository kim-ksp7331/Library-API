package ksp7331.practice.libraryAPI.loan.repository;

import ksp7331.practice.libraryAPI.loan.entity.Loan;

import java.util.Optional;

public interface LoanCustomRepository {
    Optional<Loan> findByIdFetchJoin(Long id);
}
