package ksp7331.practice.libraryAPI.config;

import ksp7331.practice.libraryAPI.book.infrastructure.entity.BookEntity;
import ksp7331.practice.libraryAPI.book.infrastructure.entity.LibraryBookEntity;
import ksp7331.practice.libraryAPI.library.infrastructure.entity.LibraryEntity;
import ksp7331.practice.libraryAPI.loan.infrastructure.entity.LoanBookEntity;
import ksp7331.practice.libraryAPI.loan.infrastructure.entity.LoanEntity;
import ksp7331.practice.libraryAPI.member.infrastructure.entity.LibraryMemberEntity;
import ksp7331.practice.libraryAPI.member.infrastructure.entity.MemberEntity;
import ksp7331.practice.libraryAPI.member.infrastructure.entity.PhoneEntity;
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
    public void saveBooks(List<BookEntity> books) {
        saveEntities(books);
    }

    @Override
    public void saveLibraries(List<LibraryEntity> libraries) {
        saveEntities(libraries);
    }

    @Override
    public void saveLibraryBooks(List<LibraryBookEntity> libraryBooks) {
        saveEntities(libraryBooks);
    }

    @Override
    public void saveMembers(List<MemberEntity> members) {
        saveEntities(members);
    }
    @Override
    public void saveLibraryMembers(List<LibraryMemberEntity> libraryMembers) {
        saveEntities(libraryMembers);
    }
    @Override
    public void savePhones(List<PhoneEntity> phones) {
        saveEntities(phones);
    }

    @Override
    public void saveLoans(List<LoanEntity> loans) {
        saveEntities(loans);
    }

    @Override
    public void saveLoanBooks(List<LoanBookEntity> loanBooks) {
        saveEntities(loanBooks);
    }

    private <T> void saveEntities(List<T> entities) {
        for (T entity : entities) {
            em.persist(entity);
        }
    }

}
