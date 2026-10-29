package com.uniessa.agendamento.repository;

import com.uniessa.agendamento.model.Servico;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceRepository extends JpaRepository<Servico, Long> {
}
