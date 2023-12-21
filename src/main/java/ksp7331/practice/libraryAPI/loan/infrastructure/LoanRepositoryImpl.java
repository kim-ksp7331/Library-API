package ksp7331.practice.libraryAPI.loan.infrastructure;

import ksp7331.practice.libraryAPI.loan.domain.Loan;
import ksp7331.practice.libraryAPI.loan.service.port.LoanRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
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
        return loanJpaRepository.save(ksp7331.practice.libraryAPI.loan.infrastructure.entity.Loan.from(loan)).to();
    }
    @Override
    public Loan update(Long id, Function<Optional<Loan>, Loan> function) {
        Optional<ksp7331.practice.libraryAPI.loan.infrastructure.entity.Loan> optionalLoan = loanJpaRepository.findByIdFetchJoin(id);
        Optional<Loan> optionalDomain = optionalLoan.map(loan -> loan.to());
        Loan loan = function.apply(optionalDomain);
        optionalLoan.get().update(loan);
        return loan;
    }

    @Override
    public List<Loan> findByLibraryMemberIdAndMonth(Long libraryMemberId, int year, int month) {
        return loanJpaRepository.findByLibraryMemberIdAndMonth(libraryMemberId, year, month)
                .stream().map(ksp7331.practice.libraryAPI.loan.infrastructure.entity.Loan::to).collect(Collectors.toList());
    }

    @Override
    public Optional<Loan> findById(Long id) {
        return loanJpaRepository.findByIdFetchJoin(id).map(ksp7331.practice.libraryAPI.loan.infrastructure.entity.Loan::to);
    }

    @Override
    public List<Loan> findAllNotReturned(Long libraryMemberId) {
        return loanJpaRepository.findAllNotReturned(libraryMemberId).stream()
                .map(ksp7331.practice.libraryAPI.loan.infrastructure.entity.Loan::to).collect(Collectors.toList());
    }
}
