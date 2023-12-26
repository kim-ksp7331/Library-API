package ksp7331.practice.libraryAPI.config;

import ksp7331.practice.libraryAPI.book.infrastructure.entity.Book;
import ksp7331.practice.libraryAPI.book.infrastructure.entity.LibraryBook;
import ksp7331.practice.libraryAPI.library.entity.Library;
import ksp7331.practice.libraryAPI.loan.infrastructure.entity.Loan;
import ksp7331.practice.libraryAPI.loan.infrastructure.entity.LoanBook;
import ksp7331.practice.libraryAPI.member.entity.LibraryMember;
import ksp7331.practice.libraryAPI.member.entity.Member;
import ksp7331.practice.libraryAPI.member.entity.Phone;

import java.util.List;

public interface TestRepository {
    void saveBooks(List<Book> books);

    void saveLibraries(List<Library> libraries);

    void saveLibraryBooks(List<LibraryBook> libraryBooks);

    void saveMembers(List<Member> members);

    void saveLibraryMembers(List<LibraryMember> libraryMembers);

    void savePhones(List<Phone> phones);

    void saveLoans(List<Loan> loans);

    void saveLoanBooks(List<LoanBook> loanBooks);
}
