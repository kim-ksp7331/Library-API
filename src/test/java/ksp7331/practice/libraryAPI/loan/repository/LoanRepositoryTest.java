package ksp7331.practice.libraryAPI.loan.repository;

import ksp7331.practice.libraryAPI.book.entity.Book;
import ksp7331.practice.libraryAPI.config.DbTestConfig;
import ksp7331.practice.libraryAPI.config.DbTestInitializer;
import ksp7331.practice.libraryAPI.library.entity.Library;
import ksp7331.practice.libraryAPI.loan.entity.Loan;
import ksp7331.practice.libraryAPI.member.entity.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import(DbTestConfig.class)
class LoanRepositoryTest {
    @Autowired
    private LoanRepository loanRepository;
    @Autowired
    private DbTestInitializer dbTestInitializer;

    @Test
    void findByIdFetchJoin() {
        // given
        Long id = 1L;
        Member member = dbTestInitializer.getMembers().get(0);
        Library library = dbTestInitializer.getLibraries().get(1);
        List<Book> books = dbTestInitializer.getBooks();

        // when
        Optional<Loan> optionalLoan = loanRepository.findByIdFetchJoin(id);

        // then
        assertThat(optionalLoan.isPresent()).isTrue();
        Loan result = optionalLoan.get();
        assertThat(result.getLibraryMember().getMember().getName()).isEqualTo(member.getName());
        assertThat(result.getLibraryMember().getLibrary().getName()).isEqualTo(library.getName());
        assertBook(books, result, 0, 0);
        assertBook(books, result, 1, 1);
    }

    private void assertBook(List<Book> books, Loan result, int resultIdx, int bookIdx) {
        Book resultBook = result.getLoanBooks().get(resultIdx).getLibraryBook().getBook();
        Book book = books.get(bookIdx);
        assertThat(resultBook.getName()).isEqualTo(book.getName());
        assertThat(resultBook.getAuthor()).isEqualTo(book.getAuthor());
    }

    @Test
    void findAllNotReturned() {
        // given
        int loanSize = 2;
        Loan loan = dbTestInitializer.getLoans().get(0);
        Long libraryMemberId = 2L;

        // when
        List<Loan> result = loanRepository.findAllNotReturned(libraryMemberId);

        // then
        assertThat(result).hasSize(loanSize);
        loan.getLoanBooks().forEach(book -> assertThat(result.get(0).getLoanBooks()).anyMatch(r -> r.getId() == book.getId()));
    }
}