package com.uniessa.agendamento.controller;
import com.uniessa.agendamento.dto.*;
import com.uniessa.agendamento.service.AgendamentoService;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequestMapping("/agendamento")
public class AgendamentoController {
    private final AgendamentoService service;
    public AgendamentoController(AgendamentoService service) { this.service=service; }
    @GetMapping
    public List<AgendamentoResposta> listar() { return service.listarTodos(); }
    @PostMapping
    public ResponseEntity<AgendamentoResposta> criar(@Valid @RequestBody CriarAgendamento entrada) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.salvarAgendamento(entrada));
    }
}
