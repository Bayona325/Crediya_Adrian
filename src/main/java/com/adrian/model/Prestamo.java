package com.adrian.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Prestamo implements Serializable {
    private Integer id;
    private Cliente cliente;
    private Empleado empleado;
    private Double monto;
    private Double interes;
    private Integer cuotas;
    private LocalDate fechaInicio;
    private EstadoPrestamo estado;
    private List<Pago> pagos = new ArrayList<>();

    public Prestamo() {}

    // ✔ Constructor usado por PrestamoFileDao (el que te hacía falta)
    public Prestamo(Integer id, Cliente cliente, Empleado empleado,
                    Double monto, Double interes, Integer cuotas) {
        this.id = id;
        this.cliente = cliente;
        this.empleado = empleado;
        this.monto = monto;
        this.interes = interes;
        this.cuotas = cuotas;
        this.fechaInicio = LocalDate.now();
        this.estado = EstadoPrestamo.PENDIENTE;
    }

    // ✔ Constructor completo correctamente implementado
    public Prestamo(Integer id, Cliente cliente, Empleado empleado,
                    Double monto, Double interes, Integer cuotas,
                    LocalDate fechaInicio, EstadoPrestamo estado) {

        this.id = id;
        this.cliente = cliente;
        this.empleado = empleado;
        this.monto = monto;
        this.interes = interes;
        this.cuotas = cuotas;
        this.fechaInicio = (fechaInicio != null ? fechaInicio : LocalDate.now());
        this.estado = (estado != null ? estado : EstadoPrestamo.PENDIENTE);
    }

    // --- Getters y setters ---
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }

    public Empleado getEmpleado() { return empleado; }
    public void setEmpleado(Empleado empleado) { this.empleado = empleado; }

    public Double getMonto() { return monto; }
    public void setMonto(Double monto) { this.monto = monto; }

    public Double getInteres() { return interes; }
    public void setInteres(Double interes) { this.interes = interes; }

    public Integer getCuotas() { return cuotas; }
    public void setCuotas(Integer cuotas) { this.cuotas = cuotas; }

    public LocalDate getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(LocalDate fechaInicio) { this.fechaInicio = fechaInicio; }

    public EstadoPrestamo getEstado() { return estado; }
    public void setEstado(EstadoPrestamo estado) { this.estado = estado; }

    public List<Pago> getPagos() { return pagos; }
    public void setPagos(List<Pago> pagos) { this.pagos = pagos; }

    public Double montoTotal() {
        return monto + (monto * (interes / 100.0));
    }

    public Double cuotaMensual() {
        return montoTotal() / cuotas;
    }

    public Double saldoPendiente() {
        Double totalPagado = pagos.stream().mapToDouble(Pago::getMonto).sum();
        return montoTotal() - totalPagado;
    }

    public void agregarPago(Pago p) {
        this.pagos.add(p);
        if (saldoPendiente() <= 0.001)
            this.estado = EstadoPrestamo.PAGADO;
    }

    @Override
    public String toString() {
        return String.format("Prestamo[id=%d, cliente=%s, monto=%.2f, interes=%.2f%%, cuotas=%d, estado=%s, saldoPendiente=%.2f]",
                id, cliente.getNombre(), monto, interes, cuotas, estado, saldoPendiente());
    }
}
