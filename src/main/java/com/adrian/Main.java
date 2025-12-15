package com.adrian;

import java.util.Scanner;

import com.adrian.dao.ClienteFileDao;
import com.adrian.dao.EmpleadoFileDao;
import com.adrian.dao.PagoFileDao;
import com.adrian.dao.PrestamoFileDao;
import com.adrian.model.Cliente;
import com.adrian.model.Empleado;
import com.adrian.model.Pago;
import com.adrian.model.Prestamo;
import com.adrian.model.EstadoPrestamo;
import com.adrian.util.Validator;

public class Main {

    private static final String DATA_DIR = "data";

    public static void main(String[] args) throws Exception {

        new java.io.File(DATA_DIR).mkdirs();

        EmpleadoFileDao edao = new EmpleadoFileDao(DATA_DIR + "/empleados.txt");
        ClienteFileDao cdao = new ClienteFileDao(DATA_DIR + "/clientes.txt");
        PrestamoFileDao pdao = new PrestamoFileDao(DATA_DIR + "/prestamos.txt", cdao, edao);
        PagoFileDao pagoDao = new PagoFileDao(DATA_DIR + "/pagos.txt");

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
                            if (!Validator.nombreValido(nombre))
                                System.out.println("❌ Nombre inválido.");
                        } while (!Validator.nombreValido(nombre));

                        String doc;
                        do {
                            System.out.print("Documento: ");
                            doc = sc.nextLine();
                            if (!Validator.documentoValido(doc))
                                System.out.println("❌ Documento inválido (solo números).");
                        } while (!Validator.documentoValido(doc));

                        System.out.print("Rol: ");
                        String rol = sc.nextLine();

                        String correo;
                        do {
                            System.out.print("Correo: ");
                            correo = sc.nextLine();
                            if (!Validator.correoValido(correo))
                                System.out.println("❌ Correo inválido.");
                        } while (!Validator.correoValido(correo));

                        Double salario;
                        while (true) {
                            System.out.print("Salario: ");
                            try {
                                salario = Double.valueOf(sc.nextLine());
                                if (Validator.numeroPositivo(salario)) break;
                            } catch (Exception e) {}
                            System.out.println("❌ Salario inválido.");
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
                            if (!Validator.nombreValido(nombre))
                                System.out.println("❌ Nombre inválido.");
                        } while (!Validator.nombreValido(nombre));

                        String doc;
                        do {
                            System.out.print("Documento: ");
                            doc = sc.nextLine();
                            if (!Validator.documentoValido(doc))
                                System.out.println("❌ Documento inválido.");
                        } while (!Validator.documentoValido(doc));

                        String correo;
                        do {
                            System.out.print("Correo: ");
                            correo = sc.nextLine();
                            if (!Validator.correoValido(correo))
                                System.out.println("❌ Correo inválido.");
                        } while (!Validator.correoValido(correo));

                        String tel;
                        do {
                            System.out.print("Telefono: ");
                            tel = sc.nextLine();
                            if (!Validator.telefonoValido(tel))
                                System.out.println("❌ Teléfono inválido (solo números, 7-15 dígitos).");
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
                        Integer cid, eid;
                        Double monto, interes;
                        Integer cuotas;

                        System.out.print("ID cliente: ");
                        cid = Integer.valueOf(sc.nextLine());

                        System.out.print("ID empleado: ");
                        eid = Integer.valueOf(sc.nextLine());

                        System.out.print("Monto: ");
                        monto = Double.valueOf(sc.nextLine());

                        System.out.print("Interes (%): ");
                        interes = Double.valueOf(sc.nextLine());

                        System.out.print("Cuotas: ");
                        cuotas = Integer.valueOf(sc.nextLine());

                        var cliente = cdao.listar().stream()
                                .filter(x -> x.getId().equals(cid)).findFirst().orElse(null);
                        var empleado = edao.listar().stream()
                                .filter(x -> x.getId().equals(eid)).findFirst().orElse(null);

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

                        // 1️⃣ Buscar préstamo
                        Prestamo prestamo = pdao.buscarPorId(pid);
                        if (prestamo == null) {
                            System.out.println("❌ Préstamo no encontrado.");
                            break;
                        }

                        // 2️⃣ Aplicar pago (afecta saldo, cuotas y estado)
                        prestamo.aplicarPago(montoPago);

                        // 3️⃣ Actualizar préstamo en archivo
                        pdao.actualizar(prestamo);

                        // 4️⃣ Registrar pago
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
