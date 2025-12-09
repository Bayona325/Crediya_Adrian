package com.adrian;

import java.util.Scanner;

import com.adrian.dao.ClienteFileDao;
import com.adrian.dao.EmpleadoFileDao;
import com.adrian.dao.PagoFileDao;
import com.adrian.dao.PrestamoFileDao;
import com.adrian.model.Cliente;
import com.adrian.model.Empleado;
import com.adrian.model.EstadoPrestamo;
import com.adrian.model.Pago;
import com.adrian.model.Prestamo;

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
                        System.out.print("Nombre: "); String nombre = sc.nextLine();
                        System.out.print("Documento: "); String doc = sc.nextLine();
                        System.out.print("Rol: "); String rol = sc.nextLine();
                        System.out.print("Correo: "); String correo = sc.nextLine();
                        System.out.print("Salario: "); Double salario = Double.valueOf(sc.nextLine());
                        Empleado e = new Empleado(null, nombre, doc, rol, correo, salario);
                        edao.guardar(e);
                        System.out.println("Empleado guardado: " + e);
                        break;
                    }
                    case "2": {
                        var list = edao.listar();
                        list.forEach(System.out::println);
                        break;
                    }
                    case "3": {
                        System.out.print("Nombre cliente: "); String n = sc.nextLine();
                        System.out.print("Documento: "); String d = sc.nextLine();
                        System.out.print("Correo: "); String co = sc.nextLine();
                        System.out.print("Telefono: "); String t = sc.nextLine();
                        Cliente c = new Cliente(null, n, d, co, t);
                        cdao.guardar(c);
                        System.out.println("Cliente guardado: " + c);
                        break;
                    }
                    case "4": {
                        cdao.listar().forEach(System.out::println);
                        break;
                    }
                    case "5": {
                        System.out.print("ID cliente: "); Integer cid = Integer.valueOf(sc.nextLine());
                        System.out.print("ID empleado: "); Integer eid = Integer.valueOf(sc.nextLine());
                        System.out.print("Monto: "); Double monto = Double.valueOf(sc.nextLine());
                        System.out.print("Interes (porcentaje): "); Double interes = Double.valueOf(sc.nextLine());
                        System.out.print("Cuotas: "); Integer cuotas = Integer.valueOf(sc.nextLine());
                        var clientes = cdao.listar();
                        var empleados = edao.listar();
                        Cliente cliente = clientes.stream().filter(x->x.getId().equals(cid)).findFirst().orElse(null);
                        Empleado empleado = empleados.stream().filter(x->x.getId().equals(eid)).findFirst().orElse(null);
                        if (cliente==null || empleado==null) { System.out.println("Cliente o empleado no encontrado"); break; }
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
                        System.out.print("ID préstamo: "); Integer pid = Integer.valueOf(sc.nextLine());
                        System.out.print("Monto pago: "); Double montoPago = Double.valueOf(sc.nextLine());
                        Pago pago = new Pago();
                        pago.setPrestamoId(pid);
                        pago.setMonto(montoPago);
                        pagoDao.guardar(pago);
                        System.out.println("Pago registrado: " + pago);
                        break;
                    }
                    case "8": {
                        pdao.listar().stream().filter(p -> (EstadoPrestamo.PENDIENTE).equals(p.getEstado())).forEach(System.out::println);
                        break;
                    }
                    case "0": {
                        System.exit(0);
                    }
                    default:
                        System.out.println("Opción inválida");
                }
            }
        }
    }
}