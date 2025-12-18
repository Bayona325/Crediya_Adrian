package com.adrian.dao;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.adrian.model.Cliente;
import com.adrian.model.Empleado;
import com.adrian.model.EstadoPrestamo;
import com.adrian.model.Prestamo;

public class FactoryReport implements GenericDao<Prestamo> {
    private File file;
    private ClienteFileDao clienteDao;
    private EmpleadoFileDao empleadoDao;

    public FactoryReport(String filePath,
                           ClienteFileDao clienteDao,
                           EmpleadoFileDao empleadoDao) {
        this.file = new File(filePath);
        this.clienteDao = clienteDao;
        this.empleadoDao = empleadoDao;
    }

    // --------------------------------------------------
    // GUARDAR (append)
    // --------------------------------------------------
    @Override
    public void guardar(Prestamo prestamo) throws Exception {
        List<Prestamo> prestamos = listar();

        // Asignar ID autoincremental
        int nextId = prestamos.stream()
                .mapToInt(p -> p.getId())
                .max()
                .orElse(0) + 1;

        prestamo = new Prestamo(
                nextId,
                prestamo.getCliente(),
                prestamo.getEmpleado(),
                prestamo.getMonto(),
                prestamo.getInteres(),
                prestamo.getCuotas()
        );

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, true))) {
            bw.write(formatearLinea(prestamo));
            bw.newLine();
        }
    }

    // --------------------------------------------------
    // LISTAR
    // --------------------------------------------------
    @Override
    public List<Prestamo> listar() throws Exception {
        List<Prestamo> lista = new ArrayList<>();
        if (!file.exists()) return lista;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] p = line.split("\t");

                Integer id = Integer.valueOf(p[0]);
                Integer clienteId = Integer.valueOf(p[1]);
                Integer empleadoId = Integer.valueOf(p[2]);
                Double monto = Double.valueOf(p[3]);
                Double interes = Double.valueOf(p[4]);
                Integer cuotas = Integer.valueOf(p[5]);
                Integer cuotasPagadas = Integer.valueOf(p[6]);
                Double saldoPendiente = Double.valueOf(p[7]);
                LocalDate fechaInicio = LocalDate.parse(p[8]);
                LocalDate fechaLimite = LocalDate.parse(p[9]);
                EstadoPrestamo estado = EstadoPrestamo.valueOf(p[10]);

                Cliente cliente = clienteDao.listar().stream()
                        .filter(c -> c.getId().equals(clienteId))
                        .findFirst().orElse(null);

                Empleado empleado = empleadoDao.listar().stream()
                        .filter(e -> e.getId().equals(empleadoId))
                        .findFirst().orElse(null);

                Prestamo pre = new Prestamo(
                        id, cliente, empleado, monto, interes, cuotas
                );

                // Restaurar estado real
                pre.aplicarPago(0.0); // inicializa internamente
                pre.getClass(); // solo para claridad conceptual

                // Seteo manual de valores persistidos
                pre.aplicarPago(0.0);
                pre.getClass();

                // Forzar valores leídos
                setField(pre, cuotasPagadas, saldoPendiente, fechaInicio, fechaLimite, estado);

                lista.add(pre);
            }
        }
        return lista;
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
    // ACTUALIZAR (sobrescribe archivo)
    // --------------------------------------------------
    public void actualizar(Prestamo prestamo) throws Exception {
        List<Prestamo> prestamos = listar();

        for (int i = 0; i < prestamos.size(); i++) {
            if (prestamos.get(i).getId().equals(prestamo.getId())) {
                prestamos.set(i, prestamo);
                break;
            }
        }
        sobrescribir(prestamos);
    }

    // --------------------------------------------------
    // SOBRESCRIBIR ARCHIVO COMPLETO
    // --------------------------------------------------
    private void sobrescribir(List<Prestamo> prestamos) throws Exception {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
            for (Prestamo p : prestamos) {
                bw.write(formatearLinea(p));
                bw.newLine();
            }
        }
    }

    // --------------------------------------------------
    // FORMATO DE LÍNEA TXT
    // --------------------------------------------------
    private String formatearLinea(Prestamo p) {
        return String.join("\t",
                String.valueOf(p.getId()),
                String.valueOf(p.getCliente().getId()),
                String.valueOf(p.getEmpleado().getId()),
                String.valueOf(p.getMonto()),
                String.valueOf(p.getInteres()),
                String.valueOf(p.getCuotas()),
                String.valueOf(p.getCuotasPagadas()),
                String.valueOf(p.getSaldoPendiente()),
                p.getFechaInicio().toString(),
                p.getFechaLimite().toString(),
                p.getEstado().toString()
        );
    }

    // --------------------------------------------------
    // MÉTODO AUXILIAR PARA RESTAURAR ESTADO (sin setters públicos)
    // --------------------------------------------------
    private void setField(Prestamo p,
                          Integer cuotasPagadas,
                          Double saldoPendiente,
                          LocalDate fechaInicio,
                          LocalDate fechaLimite,
                          EstadoPrestamo estado) {

        try {
            var f1 = Prestamo.class.getDeclaredField("cuotasPagadas");
            var f2 = Prestamo.class.getDeclaredField("saldoPendiente");
            var f3 = Prestamo.class.getDeclaredField("fechaInicio");
            var f4 = Prestamo.class.getDeclaredField("fechaLimite");
            var f5 = Prestamo.class.getDeclaredField("estado");

            f1.setAccessible(true);
            f2.setAccessible(true);
            f3.setAccessible(true);
            f4.setAccessible(true);
            f5.setAccessible(true);

            f1.set(p, cuotasPagadas);
            f2.set(p, saldoPendiente);
            f3.set(p, fechaInicio);
            f4.set(p, fechaLimite);
            f5.set(p, estado);

        } catch (Exception e) {
            throw new RuntimeException("Error restaurando préstamo desde archivo", e);
        }
    }
}
