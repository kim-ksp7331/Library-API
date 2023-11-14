package ksp7331.practice.libraryAPI.exception;

import lombok.Getter;

public enum ExceptionCode {
    LOAN_EXCEEDED(403, "Loan Exceeded"),
    PHONE_DUPLICATED(403, "Phone Duplicated"),
    BOOK_EXISTS(403, "Book Exists"),
    MEMBER_NOT_FOUND(404, "Member Not Found"),
    LIBRARY_NOT_FOUND(404, "Library Not Found"),
    BOOK_NOT_FOUND(404, "Book Not Found"),
    LOAN_NOT_FOUND(404, "Loan Not Found"),
    LOAN_RESTRICTED(403, "Loan Restricted"),;
    @Getter
    private int status;
    @Getter
    private String message;

    ExceptionCode(int status, String message) {
        this.status = status;
        this.message = message;
    }
}
