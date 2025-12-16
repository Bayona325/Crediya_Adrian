package com.adrian.dao_jdbc;

import com.adrian.dao.GenericDao;
import com.adrian.model.Pago;
import com.adrian.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;

import java.util.ArrayList;
import java.util.List;

public class PagoJdbcDao implements GenericDao<Pago> {

    @Override
    public List<Pago> listar() throws Exception {
        List<Pago> pagos = new ArrayList<>();

        String sql = """
            SELECT id, prestamo_id, fecha_pago, monto
            FROM pagos
            """;

        try (
            Connection conn = DBUtil.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()
        ) {
            while (rs.next()) {
                Pago p = new Pago();
                p.setId(rs.getInt("id"));
                p.setPrestamoId(rs.getInt("prestamo_id"));
                p.setFechaPago(rs.getDate("fecha_pago").toLocalDate());
                p.setMonto(rs.getDouble("monto"));

                pagos.add(p);
            }
        } catch (SQLException ex) {
            throw new Exception("Error listando pagos desde MySQL", ex);
        }

        return pagos;
    }

    @Override
    public void guardar(Pago p) throws Exception {

        String sql = """
            INSERT INTO pagos (prestamo_id, fecha_pago, monto)
            VALUES (?, ?, ?)
            """;

        try (
            Connection conn = DBUtil.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setInt(1, p.getPrestamoId());
            ps.setDate(2, Date.valueOf(p.getFechaPago()));
            ps.setDouble(3, p.getMonto());

            ps.executeUpdate();
        } catch (SQLException ex) {
            throw new Exception("Error guardando pago en MySQL", ex);
        }
    }
}
