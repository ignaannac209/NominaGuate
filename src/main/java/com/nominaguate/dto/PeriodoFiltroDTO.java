package com.nominaguate.dto;

/**
 * Opción del ComboBox de filtro por periodo en el Dashboard. toString()
 * define lo que ve el usuario dentro del ComboBox.
 */
public record PeriodoFiltroDTO(Integer idPeriodo, String nombrePeriodo) {

    @Override
    public String toString() {
        return nombrePeriodo;
    }
}
