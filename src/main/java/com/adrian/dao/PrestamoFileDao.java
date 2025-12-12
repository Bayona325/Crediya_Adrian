package com.adrian.dao;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.adrian.model.Cliente;
import com.adrian.model.Empleado;
import com.adrian.model.EstadoPrestamo;
import com.adrian.model.Prestamo;

public class PrestamoFileDao implements GenericDao<Prestamo> {
    private final File file;
    private final ClienteFileDao clienteDao;
    private final EmpleadoFileDao empleadoDao;
    public PrestamoFileDao(String path, ClienteFileDao cdao, EmpleadoFileDao edao) {
        this.file = new File(path);
        this.clienteDao = cdao;
        this.empleadoDao = edao;
    }

    @Override
    public List<Prestamo> listar() throws Exception {
        List<Prestamo> res = new ArrayList<>();
        if (!file.exists()) return res;

        Map<Integer, Cliente> clientes = new HashMap<>();
        for (var c: clienteDao.listar()) clientes.put(c.getId(), c);

        Map<Integer, Empleado> empleados = new HashMap<>();
        for (var e: empleadoDao.listar()) empleados.put(e.getId(), e);

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine())!=null) {
                String[] p = line.split("\t");
                if (p.length<7) continue;
                Integer id = Integer.valueOf(p[0]);
                Integer clienteId = Integer.valueOf(p[1]);
                Integer empleadoId = Integer.valueOf(p[2]);
                Double monto = Double.parseDouble(p[3]);
                Double interes = Double.parseDouble(p[4]);
                Integer cuotas = Integer.valueOf(p[5]);
                LocalDate fecha = LocalDate.parse(p[6]);
                Prestamo pre = new Prestamo(id, clientes.get(clienteId), empleados.get(empleadoId), monto, interes, cuotas);
                pre.setFechaInicio(fecha);
                if (p.length > 7)
                pre.setEstado(EstadoPrestamo.valueOf(p[7]));
                res.add(pre);
            }
        }
        return res;
    }
    @Override
    public void guardar(Prestamo p) throws Exception {
        Integer nextId = 1;
        for (var ex: listar()) if (ex.getId()!=null && ex.getId()>=nextId) nextId = ex.getId()+1;
        p.setId(nextId);
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, true))) {
            bw.write(String.join("\t", String.valueOf(p.getId()), String.valueOf(p.getCliente().getId()), String.valueOf(p.getEmpleado().getId()), String.valueOf(p.getMonto()), String.valueOf(p.getInteres()), String.valueOf(p.getCuotas()), p.getFechaInicio().toString(), p.getEstado().toString()));
            bw.newLine();
        }
    }
}
