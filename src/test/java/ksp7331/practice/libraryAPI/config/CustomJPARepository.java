package ksp7331.practice.libraryAPI.config;

import ksp7331.practice.libraryAPI.book.infrastructure.entity.Book;
import ksp7331.practice.libraryAPI.book.infrastructure.entity.LibraryBook;
import ksp7331.practice.libraryAPI.library.entity.Library;
import ksp7331.practice.libraryAPI.loan.infrastructure.entity.Loan;
import ksp7331.practice.libraryAPI.loan.infrastructure.entity.LoanBook;
import ksp7331.practice.libraryAPI.member.entity.LibraryMember;
import ksp7331.practice.libraryAPI.member.entity.Member;
import ksp7331.practice.libraryAPI.member.entity.Phone;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@Transactional
public class CustomJPARepository implements TestRepository{
    private final EntityManager em;

    public CustomJPARepository(EntityManager em) {
        this.em = em;
    }

    @Override
    public void saveBooks(List<Book> books) {
        saveEntities(books);
    }

    @Override
    public void saveLibraries(List<Library> libraries) {
        saveEntities(libraries);
    }

    @Override
    public void saveLibraryBooks(List<LibraryBook> libraryBooks) {
        saveEntities(libraryBooks);
    }

    @Override
    public void saveMembers(List<Member> members) {
        saveEntities(members);
    }
    @Override
    public void saveLibraryMembers(List<LibraryMember> libraryMembers) {
        saveEntities(libraryMembers);
    }
    @Override
    public void savePhones(List<Phone> phones) {
        saveEntities(phones);
    }

    @Override
    public void saveLoans(List<Loan> loans) {
        saveEntities(loans);
    }

    @Override
    public void saveLoanBooks(List<LoanBook> loanBooks) {
        saveEntities(loanBooks);
    }

    private <T> void saveEntities(List<T> entities) {
        for (T entity : entities) {
            em.persist(entity);
        }
    }

}
