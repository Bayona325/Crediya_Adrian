package com.adrian.dao_jdbc;

import com.adrian.dao.GenericDao;
import com.adrian.model.Empleado;
import com.adrian.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;

public class EmpleadoJdbcDao implements GenericDao<Empleado> {

    @Override
    public List<Empleado> listar() throws Exception {
        List<Empleado> empleados = new ArrayList<>();

        String sql = """
            SELECT id, nombre, documento, rol, correo, salario
            FROM empleados
            """;

        try (
            Connection conn = DBUtil.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()
        ) {
            while (rs.next()) {
                Empleado e = new Empleado(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("documento"),
                        rs.getString("rol"),
                        rs.getString("correo"),
                        rs.getDouble("salario")
                );
                empleados.add(e);
            }
        } catch (SQLException ex) {
            throw new Exception("Error listando empleados desde MySQL", ex);
        }

        return empleados;
    }

    @Override
    public void guardar(Empleado e) throws Exception {

        String sql = """
            INSERT INTO empleados
            (nombre, documento, rol, correo, salario)
            VALUES (?, ?, ?, ?, ?)
            """;

        try (
            Connection conn = DBUtil.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setString(1, e.getNombre());
            ps.setString(2, e.getDocumento());
            ps.setString(3, e.getRol());
            ps.setString(4, e.getCorreo());
            ps.setDouble(5, e.getSalario());

            ps.executeUpdate();
        } catch (SQLException ex) {
            throw new Exception("Error guardando empleado en MySQL", ex);
        }
    }
}
