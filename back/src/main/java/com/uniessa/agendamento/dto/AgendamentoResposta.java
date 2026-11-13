package com.uniessa.agendamento.dto;
import com.uniessa.agendamento.model.*;
import java.time.*;
public record AgendamentoResposta(Long id, LocalDate data, LocalDateTime horario,
    StatusAgendamento status, Long clienteId, Long profissionalId) {
    public static AgendamentoResposta de(Agendamento a) {
        return new AgendamentoResposta(a.getId(), a.getData(), a.getHorario(), a.getStatus(),
            a.getCliente().getId(), a.getProfissional().getId());
    }
}
