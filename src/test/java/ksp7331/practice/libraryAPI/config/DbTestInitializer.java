package ksp7331.practice.libraryAPI.config;

import ksp7331.practice.libraryAPI.book.entity.Book;
import ksp7331.practice.libraryAPI.book.entity.LibraryBook;
import ksp7331.practice.libraryAPI.library.entity.Library;
import ksp7331.practice.libraryAPI.loan.entity.Loan;
import ksp7331.practice.libraryAPI.member.entity.LibraryMember;
import ksp7331.practice.libraryAPI.member.entity.Member;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;


@Component
public class DbTestInitializer {
    private final TestRepository testRepository;
    private List<Book> books = List.of(
            Book.builder().name("Effective Java").author("Joshua Bloch").build(),
            Book.builder().name("자바 ORM 표준 JPA 프로그래밍").author("김영한").build(),
            Book.builder().name("Clean Code").author("Robert C. Martin").build(),
            Book.builder().name("혼자 공부하는 컴퓨터 구조+운영체제").author("강민철").build(),
            Book.builder().name("Java의 정석").author("남궁성").build(),
            Book.builder().name("토비의 스프링").author("토비").build()
    );
    private List<Library> libraries = List.of(
            Library.builder().name("서울 도서관").build(),
            Library.builder().name("부산 도서관").build()
    );
    private List<LibraryBook> libraryBooks = List.of(
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
    private List<Member> members = List.of(
            Member.builder().name("kim").build(),
            Member.builder().name("park").build()
    );
    private List<LibraryMember> libraryMembers = List.of(
            TestEntity.newLibraryMember(1L, 1L, "010-0000-0000"),
            TestEntity.newLibraryMember(1L, 2L, "010-0000-0000"),
            TestEntity.newLibraryMember(2L, 2L, "010-0000-1111")
    );
    private List<Loan> loans = List.of(
            Loan.builder().libraryMember(libraryMembers.get(1)).libraryBooks(List.of(
                    libraryBooks.get(1),
                    libraryBooks.get(3)
            )).build(),
            TestEntity.oneBookReturnedLoan(libraryMembers.get(1), List.of(libraryBooks.get(5), libraryBooks.get(7)))
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

    public List<Book> getBooks() {
        return books;
    }

    public List<Library> getLibraries() {
        return libraries;
    }

    public List<LibraryBook> getLibraryBooks() {
        return libraryBooks;
    }

    public List<Member> getMembers() {
        return members;
    }

    public List<LibraryMember> getLibraryMembers() {
        return libraryMembers;
    }

    public List<Loan> getLoans() {
        return loans;
    }



    private static class TestEntity {
        public static LibraryBook newLibraryBook(Long bookId, Long libraryId) {
            Book b = Book.builder().id(bookId).build();
            Library l = Library.builder().id(libraryId).build();
            return new LibraryBook(b, l);
        }

        public static LibraryMember newLibraryMember(Long memberId, Long libraryId, String phone) {
            Member member = Member.builder().id(memberId).build();
            Library library = Library.builder().id(libraryId).build();
            return new LibraryMember(null, member, library, phone);
        }

        public static Loan oneBookReturnedLoan(LibraryMember libraryMember, List<LibraryBook> libraryBooks) {
            Loan loan = Loan.builder().libraryMember(libraryMember).libraryBooks(libraryBooks).build();
            loan.getLoanBooks().get(0).returnBook();
            return loan;
        }
    }
}
