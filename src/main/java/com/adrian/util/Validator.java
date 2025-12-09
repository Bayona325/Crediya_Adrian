package com.adrian.util;

import java.util.regex.Pattern;

public class Validator {

    // Regex para correo válido
    private static final Pattern EMAIL_REGEX =
            Pattern.compile("^[\\w._%+-]+@[\\w.-]+\\.[A-Za-z]{2,}$");

    // Regex para nombre (solo letras + espacios)
    private static final Pattern NAME_REGEX =
            Pattern.compile("^[A-Za-zÁÉÍÓÚáéíóúñÑ ]+$");

    // Regex para teléfono (solo números y mínimo 7 dígitos)
    private static final Pattern PHONE_REGEX =
            Pattern.compile("^[0-9]{7,15}$");

    // Regex para documento (solo números)
    private static final Pattern DOCUMENT_REGEX =
            Pattern.compile("^[0-9]+$");

    /** Valida correos */
    public static boolean correoValido(String correo) {
        return correo != null && EMAIL_REGEX.matcher(correo).matches();
    }

    /** Valida nombres */
    public static boolean nombreValido(String nombre) {
        return nombre != null && NAME_REGEX.matcher(nombre).matches();
    }

    /** Valida documentos */
    public static boolean documentoValido(String doc) {
        return doc != null && DOCUMENT_REGEX.matcher(doc).matches();
    }

    /** Valida teléfonos */
    public static boolean telefonoValido(String tel) {
        return tel != null && PHONE_REGEX.matcher(tel).matches();
    }

    /** Valida números positivos */
    public static boolean numeroPositivo(Double n) {
        return n != null && n > 0;
    }

    /** Valida IDs positivos */
    public static boolean idValido(Integer id) {
        return id != null && id > 0;
    }
}
