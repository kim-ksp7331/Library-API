package ksp7331.practice.libraryAPI.config;

import ksp7331.practice.libraryAPI.book.infrastructure.entity.BookEntity;
import ksp7331.practice.libraryAPI.book.infrastructure.entity.LibraryBookEntity;
import ksp7331.practice.libraryAPI.library.infrastructure.entity.LibraryEntity;
import ksp7331.practice.libraryAPI.loan.infrastructure.entity.LoanEntity;
import ksp7331.practice.libraryAPI.loan.infrastructure.entity.LoanBookEntity;
import ksp7331.practice.libraryAPI.member.infrastructure.entity.LibraryMemberEntity;
import ksp7331.practice.libraryAPI.member.infrastructure.entity.MemberEntity;
import ksp7331.practice.libraryAPI.member.infrastructure.entity.PhoneEntity;

import java.util.List;

public interface TestRepository {
    void saveBooks(List<BookEntity> books);

    void saveLibraries(List<LibraryEntity> libraries);

    void saveLibraryBooks(List<LibraryBookEntity> libraryBooks);

    void saveMembers(List<MemberEntity> members);

    void saveLibraryMembers(List<LibraryMemberEntity> libraryMembers);

    void savePhones(List<PhoneEntity> phones);

    void saveLoans(List<LoanEntity> loans);

    void saveLoanBooks(List<LoanBookEntity> loanBooks);
}
