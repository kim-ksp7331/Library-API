package ksp7331.practice.libraryAPI.loan.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateLoan {
    private List<Long> bookIds;
    @JsonIgnore
    @Setter
    private Long libraryMemberId;


}
