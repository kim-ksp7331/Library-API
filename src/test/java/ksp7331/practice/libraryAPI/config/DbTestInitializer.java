package ksp7331.practice.libraryAPI.config;

import ksp7331.practice.libraryAPI.book.domain.BookState;
import ksp7331.practice.libraryAPI.book.infrastructure.entity.BookEntity;
import ksp7331.practice.libraryAPI.book.infrastructure.entity.LibraryBookEntity;
import ksp7331.practice.libraryAPI.library.infrastructure.entity.LibraryEntity;
import ksp7331.practice.libraryAPI.loan.domain.LoanState;
import ksp7331.practice.libraryAPI.loan.infrastructure.entity.LoanBookEntity;
import ksp7331.practice.libraryAPI.loan.infrastructure.entity.LoanEntity;
import ksp7331.practice.libraryAPI.member.infrastructure.entity.LibraryMemberEntity;
import ksp7331.practice.libraryAPI.member.infrastructure.entity.MemberEntity;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.stream.Collectors;


@Component
public class DbTestInitializer {
    private final TestRepository testRepository;
    private List<BookEntity> books = List.of(
            BookEntity.builder().name("Effective Java").author("Joshua Bloch").publisher("인사이트(insight)").build(),
            BookEntity.builder().name("자바 ORM 표준 JPA 프로그래밍").author("김영한").publisher("에이콘출판사").build(),
            BookEntity.builder().name("Clean Code").author("Robert C. Martin").publisher("인사이트(insight)").build(),
            BookEntity.builder().name("혼자 공부하는 컴퓨터 구조+운영체제").author("강민철").publisher("한빛미디어").build(),
            BookEntity.builder().name("Java의 정석").author("남궁성").publisher("도우출판").build(),
            BookEntity.builder().name("토비의 스프링").author("토비").publisher("에이콘출판").build()
    );
    private List<LibraryEntity> libraries = List.of(
            LibraryEntity.builder().name("서울 도서관").build(),
            LibraryEntity.builder().name("부산 도서관").build()
    );
    private List<LibraryBookEntity> libraryBooks = List.of(
            TestEntity.newLibraryBook(1L, 1L),
            TestEntity.newLibraryBook(1L, 2L),
            TestEntity.newLibraryBook(2L, 1L),
            TestEntity.newLibraryBook(2L, 2L),
            TestEntity.newLibraryBook(3L, 1L),
            TestEntity.newLibraryBook(3L, 2L),
            TestEntity.newLibraryBook(4L, 1L),
            TestEntity.newLibraryBook(4L, 2L),
            TestEntity.newLibraryBook(5L, 1L),
            TestEntity.newLibraryBook(5L, 2L),
            TestEntity.newLibraryBook(6L, 1L),
            TestEntity.newLibraryBook(6L, 2L)
    );
    private List<MemberEntity> members = List.of(
            MemberEntity.builder().name("kim").build(),
            MemberEntity.builder().name("park").build()
    );
    private List<LibraryMemberEntity> libraryMembers = List.of(
            TestEntity.newLibraryMember(1L, 1L, "010-0000-0000"),
            TestEntity.newLibraryMember(1L, 2L, "010-0000-0000"),
            TestEntity.newLibraryMember(2L, 2L, "010-0000-1111")
    );
    private List<LoanEntity> loans = List.of(
            TestEntity.newLoan(libraryMembers.get(1), List.of(
                    libraryBooks.get(1),
                    libraryBooks.get(3)
            )),
            TestEntity.oneBookReturnedLoan(libraryMembers.get(1), libraryBooks.get(5), libraryBooks.get(7))
    );


    public DbTestInitializer(TestRepository testRepository) {
        this.testRepository = testRepository;
    }
    @PostConstruct
    private void init() {
        testRepository.saveBooks(books);
        testRepository.saveLibraries(libraries);
        testRepository.saveLibraryBooks(libraryBooks);
        testRepository.saveMembers(members);
        testRepository.saveLibraryMembers(libraryMembers);
        testRepository.saveLoans(loans);
    }

    public List<BookEntity> getBooks() {
        return books;
    }

    public List<LibraryEntity> getLibraries() {
        return libraries;
    }

    public List<LibraryBookEntity> getLibraryBooks() {
        return libraryBooks;
    }

    public List<MemberEntity> getMembers() {
        return members;
    }

    public List<LibraryMemberEntity> getLibraryMembers() {
        return libraryMembers;
    }

    public List<LoanEntity> getLoans() {
        return loans;
    }



    private static class TestEntity {
        public static LibraryBookEntity newLibraryBook(Long bookId, Long libraryId) {
            BookEntity b = BookEntity.builder().id(bookId).build();
            LibraryEntity l = LibraryEntity.builder().id(libraryId).build();
            return LibraryBookEntity.builder().book(b).library(l).build();
        }

        public static LibraryMemberEntity newLibraryMember(Long memberId, Long libraryId, String phone) {
            MemberEntity member = MemberEntity.builder().id(memberId).build();
            LibraryEntity library = LibraryEntity.builder().id(libraryId).build();
            return new LibraryMemberEntity(null, member, library, phone);
        }

        public static LoanEntity newLoan(LibraryMemberEntity libraryMember, List<LibraryBookEntity> libraryBooks) {
            List<LoanBookEntity> loanBooks = libraryBooks.stream().map(libraryBook -> {
                        libraryBook.setState(BookState.NOT_LOANABLE);
                        return LoanBookEntity.builder().libraryBook(libraryBook).build();
                    })
                    .collect(Collectors.toList());
            LoanEntity loan = LoanEntity.builder()
                    .libraryMember(libraryMember)
                    .loanBooks(loanBooks)
                    .build();
            return loan;
        }

        public static LoanEntity oneBookReturnedLoan(LibraryMemberEntity libraryMember, LibraryBookEntity returnedBook, LibraryBookEntity book) {
            LoanBookEntity returnedLoanBook = LoanBookEntity.builder().libraryBook(returnedBook).state(LoanState.RETURN).build();
            LoanBookEntity loanBook = LoanBookEntity.builder().libraryBook(book).build();
            LoanEntity loan = LoanEntity.builder()
                    .libraryMember(libraryMember)
                    .loanBooks(List.of(returnedLoanBook, loanBook))
                    .build();
            returnedBook.setState(BookState.LOANABLE);
            return loan;
        }
    }
}
