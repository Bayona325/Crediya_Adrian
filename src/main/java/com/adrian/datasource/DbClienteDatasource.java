package com.adrian.datasource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.adrian.model.Cliente;
import com.adrian.util.DBUtil;

public class DbClienteDatasource implements ClienteDatasource {

    @Override
    public List<Cliente> listar() {
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
            System.out.println("Error listando clientes desde MySQL");
        }

        return clientes;
    }

    @Override
    public void guardar(Cliente t) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'guardar'");
    }
    
}
