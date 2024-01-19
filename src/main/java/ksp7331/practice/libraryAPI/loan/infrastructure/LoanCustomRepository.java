package ksp7331.practice.libraryAPI.loan.infrastructure;

import ksp7331.practice.libraryAPI.loan.infrastructure.entity.LoanEntity;

import java.util.List;
import java.util.Optional;

public interface LoanCustomRepository {
    Optional<LoanEntity> findByIdFetchJoin(Long id);


    List<LoanEntity> findAllNotReturned(Long libraryMemberId);

    List<LoanEntity> findByLibraryMemberIdAndMonth(Long libraryMemberId, int year, int month);
}
