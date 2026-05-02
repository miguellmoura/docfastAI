package com.docfast.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.net.URI;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ValidationException.class)
    public ProblemDetail handleValidation(ValidationException ex) {
        var problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                ex.getMessage());
        problem.setType(URI.create("https://docfast.ai/errors/validation"));
        problem.setTitle("Arquivo inválido");
        return problem;
    }

    @ExceptionHandler(ProcessingException.class)
    public ProblemDetail handleProcessing(ProcessingException ex) {
        var problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.UNPROCESSABLE_ENTITY,
                ex.getMessage());
        problem.setType(URI.create("https://docfast.ai/errors/processing"));
        problem.setTitle("Erro ao processar arquivo");
        return problem;
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ProblemDetail handleMaxUploadSize(MaxUploadSizeExceededException ex) {
        var problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.PAYLOAD_TOO_LARGE,
                "Arquivo muito grande. Envie um arquivo de até 10 MB.");
        problem.setType(URI.create("https://docfast.ai/errors/file-too-large"));
        problem.setTitle("Arquivo muito grande");
        return problem;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        var firstError = ex.getBindingResult().getFieldErrors().stream()
                .findFirst()
                .map(err -> err.getDefaultMessage())
                .orElse("Dados inválidos");

        var problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                firstError);
        problem.setType(URI.create("https://docfast.ai/errors/validation"));
        problem.setTitle("Validação falhou");
        return problem;
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleGeneric(Exception ex) {
        var problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Algo deu errado. Tente novamente em alguns segundos.");
        problem.setType(URI.create("https://docfast.ai/errors/internal"));
        problem.setTitle("Erro interno");
        return problem;
    }
}
