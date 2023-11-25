package ksp7331.practice.libraryAPI.loan.repository;

import ksp7331.practice.libraryAPI.loan.entity.Loan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoanRepository extends JpaRepository<Loan, Long>, LoanCustomRepository {
}
