# CrediYa - Sistema de Cobros de Cartera (Java - Consola)

Proyecto de ejemplo: sistema de consola para gestionar empleados, clientes, préstamos y pagos.

## Estructura
- `src/main/java/com/crediya/...` Código fuente Java (modelo, DAO, util).
- `data/` Archivos generados (empleados.txt, clientes.txt, prestamos.txt, pagos.txt).
- `sql/crediya_db.sql` Script SQL para MySQL.
- `uml/` PlantUML para diagrama de clases.

## Ejecutar (modo rápido, usando archivos)
Requiere Java 11+ y Maven (opcional).

1. Compilar:
   ```
   mvn package
   ```
2. Ejecutar:
   ```
   java -cp target/crediya-1.0-SNAPSHOT.jar com.crediya.Main
   ```

## Base de datos (opcional)
Archivo `sql/crediya_db.sql` contiene la guía para crear las tablas en MySQL. Ajusta credenciales y usa `DBUtil` para conexión JDBC.

## Archivos generados (ejemplos)
- `data/empleados.txt` (tab-separated)
- `data/clientes.txt`
- `data/prestamos.txt`
- `data/pagos.txt`

## UML
Archivo `uml/crediya.puml` (PlantUML). Puedes renderizarlo con PlantUML.

## Notas
- Este proyecto es una base: DAOs para MySQL y validaciones adicionales, manejo de excepciones y tests son tareas siguientes.
- Si quieres, puedo añadir la implementación JDBC, tests unitarios, o generar un repositorio GitHub y CI.
