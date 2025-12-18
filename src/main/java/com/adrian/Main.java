package com.adrian;

import java.util.Scanner;

import com.adrian.dao_jdbc.ClienteJdbcDao;
import com.adrian.dao_jdbc.EmpleadoJdbcDao;
import com.adrian.dao_jdbc.PagoJdbcDao;
import com.adrian.dao_jdbc.PrestamoJdbcDao;
import com.adrian.datasource.DbClienteDatasource;
import com.adrian.datasource.LocalClienteDatasource;
import com.adrian.model.Cliente;
import com.adrian.model.Empleado;
import com.adrian.model.EstadoPrestamo;
import com.adrian.model.Pago;
import com.adrian.model.Prestamo;
import com.adrian.repostory.ClienteRepository;
import com.adrian.repostory.ClienteRepositoryImpl;
import com.adrian.util.Validator;

public class Main {

    public static void main(String[] args) throws Exception {
        
        ClienteRepository repo = new ClienteRepositoryImpl(new LocalClienteDatasource("/"), new DbClienteDatasource());
        repo.guardar(null);
        ClienteJdbcDao cdao = new ClienteJdbcDao();
        EmpleadoJdbcDao edao = new EmpleadoJdbcDao();
        PrestamoJdbcDao pdao = new PrestamoJdbcDao();
        PagoJdbcDao pagoDao = new PagoJdbcDao();

        try (Scanner sc = new Scanner(System.in)) {
            while (true) {
                System.out.println("--- CrediYa - Menú ---");
                System.out.println("""
                        1) Registrar empleado
                        2) Listar empleados
                        3) Registrar cliente
                        4) Listar clientes
                        5) Registrar préstamo
                        6) Listar préstamos
                        7) Registrar pago
                        8) Ver reportes (préstamos pendientes)
                        0) Salir
                        """);

                System.out.print("\nOpción: ");
                String opt = sc.nextLine();

                switch (opt) {

                    case "1": {
                        String nombre;
                        do {
                            System.out.print("Nombre: ");
                            nombre = sc.nextLine();
                        } while (!Validator.nombreValido(nombre));

                        String doc;
                        do {
                            System.out.print("Documento: ");
                            doc = sc.nextLine();
                        } while (!Validator.documentoValido(doc));

                        System.out.print("Rol: ");
                        String rol = sc.nextLine();

                        String correo;
                        do {
                            System.out.print("Correo: ");
                            correo = sc.nextLine();
                        } while (!Validator.correoValido(correo));

                        Double salario;
                        while (true) {
                            System.out.print("Salario: ");
                            try {
                                salario = Double.valueOf(sc.nextLine());
                                if (Validator.numeroPositivo(salario)) break;
                            } catch (Exception e) {}
                        }

                        Empleado e = new Empleado(null, nombre, doc, rol, correo, salario);
                        edao.guardar(e);

                        System.out.println("Empleado guardado: " + e);
                        break;
                    }

                    case "2": {
                        edao.listar().forEach(System.out::println);
                        break;
                    }

                    case "3": {
                        String nombre;
                        do {
                            System.out.print("Nombre cliente: ");
                            nombre = sc.nextLine();
                        } while (!Validator.nombreValido(nombre));

                        String doc;
                        do {
                            System.out.print("Documento: ");
                            doc = sc.nextLine();
                        } while (!Validator.documentoValido(doc));

                        String correo;
                        do {
                            System.out.print("Correo: ");
                            correo = sc.nextLine();
                        } while (!Validator.correoValido(correo));

                        String tel;
                        do {
                            System.out.print("Telefono: ");
                            tel = sc.nextLine();
                        } while (!Validator.telefonoValido(tel));

                        Cliente c = new Cliente(null, nombre, doc, correo, tel);
                        cdao.guardar(c);

                        System.out.println("Cliente guardado: " + c);
                        break;
                    }

                    case "4": {
                        cdao.listar().forEach(System.out::println);
                        break;
                    }

                    case "5": {
                        System.out.print("ID cliente: ");
                        Integer cid = Integer.valueOf(sc.nextLine());

                        System.out.print("ID empleado: ");
                        Integer eid = Integer.valueOf(sc.nextLine());

                        System.out.print("Monto: ");
                        Double monto = Double.valueOf(sc.nextLine());

                        System.out.print("Interes (%): ");
                        Double interes = Double.valueOf(sc.nextLine());

                        System.out.print("Cuotas: ");
                        Integer cuotas = Integer.valueOf(sc.nextLine());

                        Cliente cliente = cdao.listar().stream()
                                .filter(c -> c.getId().equals(cid))
                                .findFirst()
                                .orElse(null);

                        Empleado empleado = edao.listar().stream()
                                .filter(e -> e.getId().equals(eid))
                                .findFirst()
                                .orElse(null);

                        if (cliente == null || empleado == null) {
                            System.out.println("❌ Cliente o empleado no encontrado.");
                            break;
                        }

                        Prestamo p = new Prestamo(null, cliente, empleado, monto, interes, cuotas);
                        pdao.guardar(p);

                        System.out.println("Préstamo guardado: " + p);
                        break;
                    }

                    case "6": {
                        pdao.listar().forEach(System.out::println);
                        break;
                    }

                    case "7": {
                        System.out.print("ID préstamo: ");
                        Integer pid = Integer.valueOf(sc.nextLine());

                        System.out.print("Monto pago: ");
                        Double montoPago = Double.valueOf(sc.nextLine());

                        Prestamo prestamo = pdao.listar().stream()
                                .filter(p -> p.getId().equals(pid))
                                .findFirst()
                                .orElse(null);

                        if (prestamo == null) {
                            System.out.println("❌ Préstamo no encontrado.");
                            break;
                        }

                        prestamo.aplicarPago(montoPago);
                        pdao.actualizar(prestamo);

                        Pago pago = new Pago(null, pid, montoPago);
                        pagoDao.guardar(pago);

                        System.out.println("✅ Pago aplicado correctamente.");
                        System.out.println(prestamo);
                        break;
                    }

                    case "8": {
                        pdao.listar().stream()
                                .filter(p -> EstadoPrestamo.PENDIENTE.equals(p.getEstado()))
                                .forEach(System.out::println);
                        break;
                    }

                    case "0":
                        System.exit(0);
                        break;

                    default:
                        System.out.println("❌ Opción inválida.");
                }
            }
        }
    }
}
