package ksp7331.practice.libraryAPI.loan.infrastructure;

import ksp7331.practice.libraryAPI.book.infrastructure.entity.BookEntity;
import ksp7331.practice.libraryAPI.config.DbTestConfig;
import ksp7331.practice.libraryAPI.config.DbTestInitializer;
import ksp7331.practice.libraryAPI.library.infrastructure.entity.LibraryEntity;
import ksp7331.practice.libraryAPI.loan.infrastructure.entity.LoanEntity;
import ksp7331.practice.libraryAPI.member.infrastructure.entity.LibraryMemberEntity;
import ksp7331.practice.libraryAPI.member.infrastructure.entity.MemberEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@Import(DbTestConfig.class)
class LoanJpaRepositoryTest {
    @Autowired
    private LoanJpaRepository loanJpaRepository;
    @Autowired
    private DbTestInitializer dbTestInitializer;

    @Test
    void findByIdFetchJoin() {
        // given
        Long id = 1L;
        MemberEntity member = dbTestInitializer.getMembers().get(0);
        LibraryEntity library = dbTestInitializer.getLibraries().get(1);
        List<BookEntity> books = dbTestInitializer.getBooks();

        // when
        Optional<LoanEntity> optionalLoan = loanJpaRepository.findByIdFetchJoin(id);

        // then
        assertThat(optionalLoan.isPresent()).isTrue();
        LoanEntity result = optionalLoan.get();
        assertThat(result.getLibraryMember().getMember().getName()).isEqualTo(member.getName());
        assertThat(result.getLibraryMember().getLibrary().getName()).isEqualTo(library.getName());
        assertBook(books, result, 0, 0);
        assertBook(books, result, 1, 1);
    }

    private void assertBook(List<BookEntity> books, LoanEntity result, int resultIdx, int bookIdx) {
        BookEntity resultBook = result.getLoanBooks().get(resultIdx).getLibraryBook().getBook();
        BookEntity book = books.get(bookIdx);
        assertThat(resultBook.getName()).isEqualTo(book.getName());
        assertThat(resultBook.getAuthor()).isEqualTo(book.getAuthor());
        assertThat(resultBook.getPublisher()).isEqualTo(book.getPublisher());
    }

    @Test
    void findAllNotReturned() {
        // given
        int loanSize = 2;
        LoanEntity loan = dbTestInitializer.getLoans().get(0);
        Long libraryMemberId = 2L;

        // when
        List<LoanEntity> result = loanJpaRepository.findAllNotReturned(libraryMemberId);

        // then
        assertThat(result).hasSize(loanSize);
        loan.getLoanBooks().forEach(book -> assertThat(result.get(0).getLoanBooks()).anyMatch(r -> r.getId() == book.getId()));
    }

    @Test
    void findByLibraryMemberIdAndMonth() {
        // given
        List<LoanEntity> loans = dbTestInitializer.getLoans().stream().filter(loan -> {
            LibraryMemberEntity libraryMember = loan.getLibraryMember();
            return libraryMember.getMember().getId().equals(1L) && libraryMember.getLibrary().getId().equals(2L);
        }).collect(Collectors.toList());
        Long libraryMemberId = 2L;
        int year = LocalDate.now().getYear();
        int month = LocalDate.now().getMonthValue();

        // when
        List<LoanEntity> result = loanJpaRepository.findByLibraryMemberIdAndMonth(libraryMemberId, year, month);

        // then
        assertThat(result).hasSize(loans.size());

    }
}