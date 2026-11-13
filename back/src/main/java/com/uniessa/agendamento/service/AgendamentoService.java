package com.uniessa.agendamento.service;
import com.uniessa.agendamento.dto.*;
import com.uniessa.agendamento.model.*;
import com.uniessa.agendamento.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import org.springframework.data.domain.Sort;
import java.util.List;
@Service
public class AgendamentoService {
    private final AgendamentoRepository agendamentos;
    private final ClienteRepository clientes;
    private final ProfissionalRepository profissionais;
    public AgendamentoService(AgendamentoRepository a, ClienteRepository c, ProfissionalRepository p) {
        agendamentos=a; clientes=c; profissionais=p;
    }
    @Transactional(readOnly=true)
    public List<AgendamentoResposta> listarTodos() {
        return agendamentos.findAll(Sort.by("id")).stream().map(AgendamentoResposta::de).toList();
    }
    @Transactional
    public AgendamentoResposta salvarAgendamento(CriarAgendamento entrada) {
        if (!entrada.data().equals(entrada.horario().toLocalDate()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Data e horário devem corresponder.");
        var cliente=clientes.findById(entrada.clienteId()).orElseThrow(() ->
            new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não encontrado."));
        var profissional=profissionais.findById(entrada.profissionalId()).orElseThrow(() ->
            new ResponseStatusException(HttpStatus.NOT_FOUND, "Profissional não encontrado."));
        var a=new Agendamento(null, entrada.data(), entrada.horario(), StatusAgendamento.PENDENTE, profissional, cliente);
        return AgendamentoResposta.de(agendamentos.save(a));
    }
}
