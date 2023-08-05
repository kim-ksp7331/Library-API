package ksp7331.practice.libraryAPI.config;

import ksp7331.practice.libraryAPI.book.entity.Book;
import ksp7331.practice.libraryAPI.book.entity.LibraryBook;
import ksp7331.practice.libraryAPI.library.entity.Library;
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
            Book.builder().name("자바 ORM 표준 JPA 프로그래밍").author("김영한").build()
    );
    private List<Library> libraries = List.of(
            Library.builder().name("서울 도서관").build(),
            Library.builder().name("부산 도서관").build()
    );
    private List<LibraryBook> libraryBooks = List.of(
            TestEntity.newLibraryBook(1L, 1L),
            TestEntity.newLibraryBook(1L, 2L),
            TestEntity.newLibraryBook(2L, 1L),
            TestEntity.newLibraryBook(2L, 2L)
    );
    private List<Member> members = List.of(
        Member.builder().name("kim").build()
    );
    private List<LibraryMember> libraryMembers = List.of(
            TestEntity.newLibraryMember(1L, 1L, "010-0000-0000")
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

    private static class TestEntity {
        public static LibraryBook newLibraryBook(Long bookId, Long libraryId) {
            Book b = Book.builder().id(bookId).build();
            Library l = Library.builder().id(libraryId).build();
            return new LibraryBook(null, b, l);
        }

        public static LibraryMember newLibraryMember(Long memberId, Long libraryId, String phone) {
            Member member = Member.builder().id(memberId).build();
            Library library = Library.builder().id(libraryId).build();
            return new LibraryMember(null, member, library, phone);
        }
    }
}
