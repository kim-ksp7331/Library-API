package ksp7331.practice.libraryAPI.loan.infrastructure;

import ksp7331.practice.libraryAPI.loan.domain.Loan;
import ksp7331.practice.libraryAPI.loan.infrastructure.entity.LoanEntity;
import ksp7331.practice.libraryAPI.loan.service.port.LoanRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class LoanRepositoryImpl implements LoanRepository {
    private final LoanJpaRepository loanJpaRepository;

    public LoanRepositoryImpl(LoanJpaRepository loanJpaRepository) {
        this.loanJpaRepository = loanJpaRepository;
    }

    @Override
    public Loan create(Loan loan) {
        if(loan.getId() != null) throw new IllegalCallerException();
        return loanJpaRepository.save(LoanEntity.from(loan)).toDomain();
    }
    @Override
    public Loan update(Loan loan) {
        loanJpaRepository.save(LoanEntity.from(loan));
        return loan;
    }

    @Override
    public List<Loan> findByLibraryMemberIdAndMonth(Long libraryMemberId, int year, int month) {
        return loanJpaRepository.findByLibraryMemberIdAndMonth(libraryMemberId, year, month)
                .stream().map(LoanEntity::toDomain).collect(Collectors.toList());
    }

    @Override
    public Optional<Loan> findById(Long id) {
        return loanJpaRepository.findByIdFetchJoin(id).map(LoanEntity::toDomain);
    }

    @Override
    public List<Loan> findAllNotReturned(Long libraryMemberId) {
        return loanJpaRepository.findAllNotReturned(libraryMemberId).stream()
                .map(LoanEntity::toDomain).collect(Collectors.toList());
    }
}
