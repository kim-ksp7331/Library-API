package ksp7331.practice.libraryAPI.loan.controller;

import ksp7331.practice.libraryAPI.loan.dto.LoanControllerDTO;
import ksp7331.practice.libraryAPI.loan.dto.LoanServiceDTO;
import ksp7331.practice.libraryAPI.loan.service.LoanService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequiredArgsConstructor
public class LoanController {
    private final LoanService loanService;

    @PostMapping("/members/{library-member-id}/loan")
    public ResponseEntity<Void> postLoan(@PathVariable("library-member-id") Long libraryMemberId,
                                         @RequestBody LoanControllerDTO.Post post) {
        String LOAN_URL_PREFIX = "/members/{library-member-id}/loan";

        LoanServiceDTO.CreateParam createParam = LoanServiceDTO.CreateParam.builder()
                .bookIds(post.getBookIds()).libraryMemberId(libraryMemberId).build();
        Long id = loanService.createLoan(createParam);

        URI uri = UriComponentsBuilder.newInstance()
                .path(LOAN_URL_PREFIX + "/{loan-id}")
                .buildAndExpand(libraryMemberId, id)
                .toUri();

        return ResponseEntity.created(uri).build();
    }
}
