package com.uniessa.agendamento.dto;
import jakarta.validation.constraints.*;
import java.time.*;
public record CriarAgendamento(@NotNull LocalDate data, @NotNull LocalDateTime horario,
    @NotNull @Positive Long clienteId, @NotNull @Positive Long profissionalId) {}
