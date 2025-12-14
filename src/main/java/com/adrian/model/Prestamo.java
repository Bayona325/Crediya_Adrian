package com.adrian.model;

import java.time.LocalDate;

public class Prestamo {

    private Integer id;
    private Cliente cliente;
    private Empleado empleado;
    private Double monto;
    private Double interes;
    private Integer cuotas;
    private Integer cuotasPagadas;
    private Double saldoPendiente;
    private LocalDate fechaInicio;
    private EstadoPrestamo estado;

    public Prestamo() {}

    public Prestamo(Integer id, Cliente cliente, Empleado empleado,
                    Double monto, Double interes, Integer cuotas) {
        this.id = id;
        this.cliente = cliente;
        this.empleado = empleado;
        this.monto = monto;
        this.interes = interes;
        this.cuotas = cuotas;
        this.cuotasPagadas = 0;
        this.saldoPendiente = monto + (monto * interes / 100);
        this.fechaInicio = LocalDate.now();
        this.estado = EstadoPrestamo.PENDIENTE;
    }

    // 🔥 LÓGICA CLAVE
    public void aplicarPago(Double montoPago) {
        if (estado == EstadoPrestamo.PAGADO) return;

        saldoPendiente -= montoPago;
        cuotasPagadas++;

        if (saldoPendiente <= 0 || cuotasPagadas >= cuotas) {
            saldoPendiente = 0.0;
            estado = EstadoPrestamo.PAGADO;
        }
    }

    // Getters y setters
    public Integer getId() { return id; }
    public Cliente getCliente() { return cliente; }
    public Empleado getEmpleado() { return empleado; }
    public Double getMonto() { return monto; }
    public Double getInteres() { return interes; }
    public Integer getCuotas() { return cuotas; }
    public Integer getCuotasPagadas() { return cuotasPagadas; }
    public Double getSaldoPendiente() { return saldoPendiente; }
    public LocalDate getFechaInicio() { return fechaInicio; }
    public EstadoPrestamo getEstado() { return estado; }

    @Override
    public String toString() {
        return "Prestamo[id=" + id +
                ", cliente=" + cliente.getNombre() +
                ", saldo=" + saldoPendiente +
                ", cuotas=" + cuotasPagadas + "/" + cuotas +
                ", estado=" + estado + "]";
    }
}
