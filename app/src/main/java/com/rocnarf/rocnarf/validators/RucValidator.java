package com.rocnarf.rocnarf.validators;

public class RucValidator {
    public static boolean validarRUC(String ruc) {
        if (ruc == null || ruc.length() != 13) {
            return false;
        }

        // Verificar que contenga solo números
        if (!ruc.matches("\\d+")) {
            return false;
        }

        // Obtener los primeros 10 dígitos y convertirlos a enteros
        int provincia = Integer.parseInt(ruc.substring(0, 2));
        int tercerDigito = Character.getNumericValue(ruc.charAt(2));
        int[] coeficientes;
        int verificador, suma = 0;

        // Validar el código de provincia (01-24 y 30 para extranjeros)
        if ((provincia < 1 || provincia > 24) && provincia != 30) {
            return false;
        }

        // Validar el tipo de RUC
        if (tercerDigito < 0 || tercerDigito > 9) {
            return false;
        }

        // Algoritmo para persona natural (0-5)
        if (tercerDigito >= 0 && tercerDigito <= 5) {
            coeficientes = new int[]{2, 1, 2, 1, 2, 1, 2, 1, 2};
            verificador = Character.getNumericValue(ruc.charAt(9));

            for (int i = 0; i < coeficientes.length; i++) {
                int valor = Character.getNumericValue(ruc.charAt(i)) * coeficientes[i];
                suma += valor >= 10 ? valor - 9 : valor;
            }

            int residuo = suma % 10;
            int digitoCalculado = residuo == 0 ? 0 : 10 - residuo;

            if (digitoCalculado != verificador) {
                return false;
            }
        }
        // Algoritmo para entidades públicas (tercer dígito == 6)
        else if (tercerDigito == 6) {
            coeficientes = new int[]{3, 2, 7, 6, 5, 4, 3, 2};
            verificador = Character.getNumericValue(ruc.charAt(8));

            for (int i = 0; i < coeficientes.length; i++) {
                suma += Character.getNumericValue(ruc.charAt(i)) * coeficientes[i];
            }

            int residuo = suma % 11;
            int digitoCalculado = residuo == 0 ? 0 : 11 - residuo;

            if (digitoCalculado != verificador) {
                return false;
            }
        }
        // Algoritmo para sociedades privadas (tercer dígito == 9)
        else if (tercerDigito == 9) {
            coeficientes = new int[]{4, 3, 2, 7, 6, 5, 4, 3, 2};
            verificador = Character.getNumericValue(ruc.charAt(9));

            for (int i = 0; i < coeficientes.length; i++) {
                suma += Character.getNumericValue(ruc.charAt(i)) * coeficientes[i];
            }

            int residuo = suma % 11;
            int digitoCalculado = residuo == 0 ? 0 : 11 - residuo;

            if (digitoCalculado != verificador) {
                return false;
            }
        } else {
            return false;
        }

        // Validar que los últimos 3 dígitos sean 001
        return ruc.endsWith("001");
    }
}
