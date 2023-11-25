package ksp7331.practice.libraryAPI.loan.controller;

import ksp7331.practice.libraryAPI.loan.dto.LoanControllerDTO;
import ksp7331.practice.libraryAPI.loan.dto.LoanServiceDTO;
import ksp7331.practice.libraryAPI.loan.mapper.LoanMapper;
import ksp7331.practice.libraryAPI.loan.service.LoanService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/members/{library-member-id}/loan")
@RequiredArgsConstructor
public class LoanController {
    private final LoanService loanService;
    private final LoanMapper loanMapper;

    @PostMapping
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
    @PostMapping("/{loan-id}")
    public ResponseEntity<LoanControllerDTO.Response> postReturnBook(@PathVariable("loan-id") Long loanId,
            @RequestBody LoanControllerDTO.ReturnPost dto){
        LoanServiceDTO.ReturnBookParam param = LoanServiceDTO.ReturnBookParam.builder().loanId(loanId).bookIds(dto.getBookIds()).build();
        LoanServiceDTO.Result result = loanService.returnBook(param);
        return ResponseEntity.ok(loanMapper.serviceDTOToControllerDTO(result));
    }


    @GetMapping("/{loan-id}")
    public ResponseEntity<LoanControllerDTO.Response> getLoan(@PathVariable("loan-id") Long loanId) {
        LoanServiceDTO.Result result = loanService.findLoan(loanId);
        return ResponseEntity.ok(loanMapper.serviceDTOToControllerDTO(result));
    }

    @GetMapping
    public ResponseEntity<List<LoanControllerDTO.Response>> getLoanByMonth(@PathVariable("library-member-id") Long libraryMemberId, @RequestParam int year, @RequestParam int month) {
        List<LoanServiceDTO.Result> results = loanService.findLoanByMonth(libraryMemberId, year, month);
        return ResponseEntity.ok(loanMapper.serviceDTOsToControllerDTOs(results));
    }
}
