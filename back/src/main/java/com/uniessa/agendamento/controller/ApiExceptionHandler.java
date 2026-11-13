package com.uniessa.agendamento.controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import java.util.*;
import org.slf4j.*;
@RestControllerAdvice
public class ApiExceptionHandler {
    private static final Logger log=LoggerFactory.getLogger(ApiExceptionHandler.class);
    public record Erro(String codigo, String mensagem, Map<String,String> campos) {}
    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<Erro> validacao(MethodArgumentNotValidException e) {
        Map<String,String> campos=new LinkedHashMap<>();
        e.getBindingResult().getFieldErrors().forEach(f -> campos.put(f.getField(), f.getDefaultMessage()));
        return ResponseEntity.badRequest().body(new Erro("VALIDACAO", "Verifique os campos informados.", campos));
    }
    @ExceptionHandler(HttpMessageNotReadableException.class)
    ResponseEntity<Erro> json() { return ResponseEntity.badRequest().body(new Erro("JSON_INVALIDO", "JSON ou formato de campo inválido.", Map.of())); }
    @ExceptionHandler(ResponseStatusException.class)
    ResponseEntity<Erro> negocio(ResponseStatusException e) {
        return ResponseEntity.status(e.getStatusCode()).body(new Erro("REQUISICAO_INVALIDA", e.getReason(), Map.of()));
    }
    @ExceptionHandler(NoResourceFoundException.class)
    ResponseEntity<Erro> ausente() { return ResponseEntity.status(404).body(new Erro("NAO_ENCONTRADO", "Recurso não encontrado.", Map.of())); }
    @ExceptionHandler(Exception.class)
    ResponseEntity<Erro> inesperado(Exception e) {
        log.error("Falha interna no processamento: {}", e.getClass().getSimpleName());
        return ResponseEntity.internalServerError().body(new Erro("ERRO_INTERNO", "Não foi possível concluir a operação.", Map.of()));
    }
}
