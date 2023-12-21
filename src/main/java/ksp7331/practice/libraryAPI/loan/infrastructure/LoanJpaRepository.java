package ksp7331.practice.libraryAPI.loan.infrastructure;

import ksp7331.practice.libraryAPI.loan.infrastructure.entity.Loan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoanJpaRepository extends JpaRepository<Loan, Long>, LoanCustomRepository {
}
