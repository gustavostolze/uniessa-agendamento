package com.uniessa.agendamento.service;

import com.uniessa.agendamento.model.Agendamento;
import com.uniessa.agendamento.model.StatusAgendamento;
import com.uniessa.agendamento.repository.AgendamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AgendamentoService {

    @Autowired
    private AgendamentoRepository agendamentoRepository;

    public List<Agendamento> listarTodos() {
        return agendamentoRepository.findAll();
    }

    public Agendamento salvarAgendamento(Agendamento agendamento) {
        if (agendamento.getStatus() == null) {
            agendamento.setStatus(StatusAgendamento.PENDENTE);
        }

        return agendamentoRepository.save(agendamento);
    }
}