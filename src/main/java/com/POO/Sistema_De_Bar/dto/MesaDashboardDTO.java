package com.POO.Sistema_De_Bar.dto;

import com.POO.Sistema_De_Bar.model.StatusMesa;

public record MesaDashboardDTO(
        Long id,
        Integer numero,
        StatusMesa status,
        Long comandaId
) {}