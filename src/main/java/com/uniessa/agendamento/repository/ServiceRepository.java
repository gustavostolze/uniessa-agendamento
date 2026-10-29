package com.uniessa.agendamento.repository;

import com.uniessa.agendamento.model.Service;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceRepository extends JpaRepository<Service,Long> {
}
