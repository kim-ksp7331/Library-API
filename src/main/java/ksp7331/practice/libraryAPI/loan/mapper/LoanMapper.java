package ksp7331.practice.libraryAPI.loan.mapper;

import ksp7331.practice.libraryAPI.loan.dto.LoanControllerDTO;
import ksp7331.practice.libraryAPI.loan.dto.LoanServiceDTO;
import ksp7331.practice.libraryAPI.loan.entity.Loan;
import ksp7331.practice.libraryAPI.member.entity.LibraryMember;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class LoanMapper {
    public LoanServiceDTO.Result entitiesToServiceDTOs(Loan loan) {
        LibraryMember libraryMember = loan.getLibraryMember();
        return LoanServiceDTO.Result.builder()
                .id(loan.getId())
                .libraryMemberId(libraryMember.getId())
                .memberName(libraryMember.getMember().getName())
                .libraryName(libraryMember.getLibrary().getName())
                .createdDate(loan.getCreatedDate())
                .books(loan.getLoanBooks().stream()
                        .map(loanBook -> LoanServiceDTO.Result.Book.builder()
                                .name(loanBook.getLibraryBook().getBook().getName())
                                .author(loanBook.getLibraryBook().getBook().getAuthor())
                                .build()).collect(Collectors.toList()))
                .build();
    }

    public LoanControllerDTO.Response serviceDTOToControllerDTO(LoanServiceDTO.Result result) {
        List<LoanServiceDTO.Result.Book> resultBooks = result.getBooks();
        List<LoanControllerDTO.Response.Book> books = resultBooks != null ? resultBooks.stream()
                .map(book -> LoanControllerDTO.Response.Book.builder()
                        .name(book.getName())
                        .author(book.getAuthor())
                        .build()).collect(Collectors.toList()) : null;

        return LoanControllerDTO.Response.builder()
                .id(result.getId())
                .libraryMemberId(result.getLibraryMemberId())
                .memberName(result.getMemberName())
                .libraryName(result.getLibraryName())
                .createdDate(result.getCreatedDate())
                .books(books)
                .build();
    }

}
