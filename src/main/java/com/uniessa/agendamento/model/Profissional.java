package com.uniessa.agendamento.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "profissional")
@Data


public class Profissional {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String especialidade;
    @Column(nullable = false)
    private String telefone;

}
