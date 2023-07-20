package ksp7331.practice.libraryAPI.exception;

import lombok.Getter;

public enum ExceptionCode {
    LOAN_EXCEEDED(403, "Loan Exceeded"),
    PHONE_DUPLICATED(403, "Phone Duplicated"),
    MEMBER_NOT_FOUND(404, "Member Not Found"),
    LIBRARY_NOT_FOUND(404, "library Not Found");
    @Getter
    private int status;
    @Getter
    private String message;

    ExceptionCode(int status, String message) {
        this.status = status;
        this.message = message;
    }
}
