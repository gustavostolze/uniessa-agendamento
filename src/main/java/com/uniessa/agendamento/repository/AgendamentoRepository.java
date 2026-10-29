package com.uniessa.agendamento.repository;

import com.uniessa.agendamento.model.Agendamento;
import com.uniessa.agendamento.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AgendamentoRepository extends JpaRepository<Agendamento, Long> {
}
