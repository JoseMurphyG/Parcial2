package com.umg.edu.gt.progra2.HelloWorld.Dto;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class TipoCambioRequestDTO {

    private LocalDate fechaInicio;
    private LocalDate fechaFin;

    // Constructor
    public TipoCambioRequestDTO() {}

    public TipoCambioRequestDTO(LocalDate fechaInicio, LocalDate fechaFin) {
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
    }

    // Getters y Setters
    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }

    // Método para formatear las fechas en el formato requerido por el servicio SOAP
    public String getFechaInicioFormatted() {
        return fechaInicio.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }

    public String getFechaFinFormatted() {
        return fechaFin.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }
}
