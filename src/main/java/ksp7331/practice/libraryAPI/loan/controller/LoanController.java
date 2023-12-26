package ksp7331.practice.libraryAPI.loan.controller;

import ksp7331.practice.libraryAPI.loan.domain.Loan;
import ksp7331.practice.libraryAPI.loan.dto.*;
import ksp7331.practice.libraryAPI.loan.service.LoanService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/members/{library-member-id}/loan")
@RequiredArgsConstructor
public class LoanController {
    private final LoanService loanService;

    @PostMapping
    public ResponseEntity<Void> postLoan(@PathVariable("library-member-id") Long libraryMemberId,
                                         @RequestBody CreateLoan createLoan) {
        String LOAN_URL_PREFIX = "/members/{library-member-id}/loan";
        createLoan.setLibraryMemberId(libraryMemberId);
        Long id = loanService.createLoan(createLoan);
        URI uri = UriComponentsBuilder.newInstance()
                .path(LOAN_URL_PREFIX + "/{loan-id}")
                .buildAndExpand(libraryMemberId, id)
                .toUri();

        return ResponseEntity.created(uri).build();
    }
    @PostMapping("/{loan-id}")
    public ResponseEntity<Response> postReturnBook(@PathVariable("loan-id") Long loanId,
                                                   @RequestBody ReturnBook returnBook){
        returnBook.setLoanId(loanId);
        Loan loan = loanService.returnBook(returnBook);
        return ResponseEntity.ok(Response.from(loan));
    }


    @GetMapping("/{loan-id}")
    public ResponseEntity<Response> getLoan(@PathVariable("loan-id") Long loanId) {
        Loan loan = loanService.getById(loanId);
        return ResponseEntity.ok(Response.from(loan));
    }

    @GetMapping
    public ResponseEntity<List<Response>> getLoanByMonth(@PathVariable("library-member-id") Long libraryMemberId, @RequestParam int year, @RequestParam int month) {
        List<Loan> loans = loanService.findByLibraryMemberIdAndMonth(libraryMemberId, year, month);
        return ResponseEntity.ok(loans.stream().map(Response::from).collect(Collectors.toList()));
    }
}
