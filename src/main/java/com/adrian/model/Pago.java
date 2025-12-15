package com.adrian.model;

import java.io.Serializable;
import java.time.LocalDate;

public class Pago implements Serializable {

    private Integer id;
    private Integer prestamoId;
    private LocalDate fechaPago;
    private Double monto;

    public Pago() {}

    public Pago(Integer id, Integer prestamoId, Double monto) {
        this.id = id;
        this.prestamoId = prestamoId;
        this.monto = monto;
        this.fechaPago = LocalDate.now();
    }

    public Pago(Integer id, Integer prestamoId, LocalDate fechaPago, Double monto) {
        this.id = id;
        this.prestamoId = prestamoId;
        this.monto = monto;

        // Si viene null desde archivo o constructor → hoy
        this.fechaPago = (fechaPago != null) ? fechaPago : LocalDate.now();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPrestamoId() {
        return prestamoId;
    }

    public void setPrestamoId(Integer prestamoId) {
        this.prestamoId = prestamoId;
    }

    public LocalDate getFechaPago() {
        return fechaPago;
    }

    public void setFechaPago(LocalDate fechaPago) {
        this.fechaPago = fechaPago;
    }

    public Double getMonto() {
        return monto;
    }

    public void setMonto(Double monto) {
        this.monto = monto;
    }

    @Override
    public String toString() {
        return String.format(
            "Pago[id=%d, prestamoId=%d, fecha=%s, monto=%.2f]",
            id, prestamoId, fechaPago, monto
        );
    }
}
