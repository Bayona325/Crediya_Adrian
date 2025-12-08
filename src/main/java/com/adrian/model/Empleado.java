package com.adrian.model;

public class Empleado {
    private Integer id;
    private String nombre;
    private String documento;
    private String rol;
    private String correo;
    private Double salario;

    public Empleado() {}

    public Empleado(Integer id, String nombre, String documento, String rol, String correo, Double salario) {
        this.id = id;
        this.nombre = nombre;
        this.documento = documento;
        this.rol = rol;
        this.correo = correo;
        this.salario = salario;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public Double getSalario() {
        return salario;
    }

    public void setSalario(Double salario) {
        this.salario = salario;
    }

    @Override
    public String toString() {
        return String.format("Empleado[id=%d, nombre=%s, documento=%s, rol=%s, correo=%s, salario=%.2f]", id, nombre, documento, rol, correo, salario);
    }
}
