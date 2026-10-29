package com.uniessa.agendamento.controller;

import com.uniessa.agendamento.model.Agendamento;
import com.uniessa.agendamento.service.AgendamentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/agendamentos")
public class AgendamentoController {

    @Autowired
    private AgendamentoService agendamentoService;

    @GetMapping
    public ResponseEntity<List<Agendamento>> listarAgendamentos() {
        List<Agendamento> agendamentos = agendamentoService.listarTodos();
        return ResponseEntity.ok(agendamentos);
    }

    @PostMapping
    public ResponseEntity<Agendamento> criarAgendamento(@RequestBody Agendamento agendamento) {
        Agendamento novoAgendamento = agendamentoService.salvarAgendamento(agendamento);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoAgendamento);
    }
}