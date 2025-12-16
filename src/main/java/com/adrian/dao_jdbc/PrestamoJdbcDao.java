package com.adrian.dao_jdbc;

import com.adrian.dao.GenericDao;
import com.adrian.model.Cliente;
import com.adrian.model.Empleado;
import com.adrian.model.EstadoPrestamo;
import com.adrian.model.Prestamo;
import com.adrian.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PrestamoJdbcDao implements GenericDao<Prestamo> {

    @Override
    public void guardar(Prestamo p) throws Exception {

        String sql = """
            INSERT INTO prestamos
            (cliente_id, empleado_id, monto, interes, cuotas,
             cuotas_pagadas, saldo_pendiente, fecha_inicio, estado)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
            """;

        try (
            Connection conn = DBUtil.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setInt(1, p.getCliente().getId());
            ps.setInt(2, p.getEmpleado().getId());
            ps.setDouble(3, p.getMonto());
            ps.setDouble(4, p.getInteres());
            ps.setInt(5, p.getCuotas());
            ps.setInt(6, p.getCuotasPagadas());
            ps.setDouble(7, p.getSaldoPendiente());
            ps.setDate(8, Date.valueOf(p.getFechaInicio()));
            ps.setString(9, p.getEstado().name());

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new Exception("Error guardando préstamo en MySQL", e);
        }
    }

    @Override
    public List<Prestamo> listar() throws Exception {
        List<Prestamo> prestamos = new ArrayList<>();

        String sql = """
            SELECT p.id,
                   p.monto, p.interes, p.cuotas,
                   p.cuotas_pagadas, p.saldo_pendiente,
                   p.fecha_inicio, p.estado,
                   c.id AS cliente_id, c.nombre, c.documento, c.correo, c.telefono,
                   e.id AS empleado_id, e.nombre, e.documento, e.rol, e.correo, e.salario
            FROM prestamos p
            JOIN clientes c ON p.cliente_id = c.id
            JOIN empleados e ON p.empleado_id = e.id
            """;

        try (
            Connection conn = DBUtil.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()
        ) {
            while (rs.next()) {

                Cliente cliente = new Cliente(
                        rs.getInt("cliente_id"),
                        rs.getString("nombre"),
                        rs.getString("documento"),
                        rs.getString("correo"),
                        rs.getString("telefono")
                );

                Empleado empleado = new Empleado(
                        rs.getInt("empleado_id"),
                        rs.getString("nombre"),
                        rs.getString("documento"),
                        rs.getString("rol"),
                        rs.getString("correo"),
                        rs.getDouble("salario")
                );

                Prestamo p = new Prestamo(
                        rs.getInt("id"),
                        cliente,
                        empleado,
                        rs.getDouble("monto"),
                        rs.getDouble("interes"),
                        rs.getInt("cuotas")
                );

                // Restaurar estado persistido
                setEstadoPersistido(
                        p,
                        rs.getInt("cuotas_pagadas"),
                        rs.getDouble("saldo_pendiente"),
                        rs.getDate("fecha_inicio").toLocalDate(),
                        EstadoPrestamo.valueOf(rs.getString("estado"))
                );

                prestamos.add(p);
            }
        } catch (SQLException e) {
            throw new Exception("Error listando préstamos desde MySQL", e);
        }

        return prestamos;
    }

    // --------------------------------------------------
    // BUSCAR POR ID
    // --------------------------------------------------
    public Prestamo buscarPorId(Integer id) throws Exception {
        return listar().stream()
                .filter(p -> p.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    // --------------------------------------------------
    // ACTUALIZAR (UPDATE SQL)
    // --------------------------------------------------
    public void actualizar(Prestamo p) throws Exception {

        String sql = """
            UPDATE prestamos
            SET cuotas_pagadas = ?,
                saldo_pendiente = ?,
                estado = ?
            WHERE id = ?
            """;

        try (
            Connection conn = DBUtil.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setInt(1, p.getCuotasPagadas());
            ps.setDouble(2, p.getSaldoPendiente());
            ps.setString(3, p.getEstado().name());
            ps.setInt(4, p.getId());

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new Exception("Error actualizando préstamo en MySQL", e);
        }
    }

    // --------------------------------------------------
    // AUXILIAR: restaurar estado interno
    // --------------------------------------------------
    private void setEstadoPersistido(
            Prestamo p,
            int cuotasPagadas,
            double saldoPendiente,
            LocalDate fechaInicio,
            EstadoPrestamo estado
    ) {
        try {
            var f1 = Prestamo.class.getDeclaredField("cuotasPagadas");
            var f2 = Prestamo.class.getDeclaredField("saldoPendiente");
            var f3 = Prestamo.class.getDeclaredField("fechaInicio");
            var f4 = Prestamo.class.getDeclaredField("estado");

            f1.setAccessible(true);
            f2.setAccessible(true);
            f3.setAccessible(true);
            f4.setAccessible(true);

            f1.set(p, cuotasPagadas);
            f2.set(p, saldoPendiente);
            f3.set(p, fechaInicio);
            f4.set(p, estado);

        } catch (Exception e) {
            throw new RuntimeException("Error restaurando préstamo desde MySQL", e);
        }
    }
}
