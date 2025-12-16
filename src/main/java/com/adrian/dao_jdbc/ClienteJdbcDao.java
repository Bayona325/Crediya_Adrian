package com.adrian.dao_jdbc;

import com.adrian.dao.GenericDao;
import com.adrian.model.Cliente;
import com.adrian.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;

public class ClienteJdbcDao implements GenericDao<Cliente> {

    @Override
    public List<Cliente> listar() throws Exception {
        List<Cliente> clientes = new ArrayList<>();

        String sql = "SELECT id, nombre, documento, correo, telefono FROM clientes";

        try (
            Connection conn = DBUtil.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()
        ) {
            while (rs.next()) {
                Cliente c = new Cliente(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("documento"),
                        rs.getString("correo"),
                        rs.getString("telefono")
                );
                clientes.add(c);
            }
        } catch (SQLException e) {
            throw new Exception("Error listando clientes desde MySQL", e);
        }

        return clientes;
    }

    @Override
    public void guardar(Cliente c) throws Exception {

        String sql = """
            INSERT INTO clientes (nombre, documento, correo, telefono)
            VALUES (?, ?, ?, ?)
            """;

        try (
            Connection conn = DBUtil.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setString(1, c.getNombre());
            ps.setString(2, c.getDocumento());
            ps.setString(3, c.getCorreo());
            ps.setString(4, c.getTelefono());

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new Exception("Error guardando cliente en MySQL", e);
        }
    }
}
