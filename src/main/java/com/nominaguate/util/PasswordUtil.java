package com.nominaguate.util;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

/**
 * Hash y verificación de contraseñas con PBKDF2WithHmacSHA256.
 * No requiere librerías externas (viene en el JDK), por lo que evita
 * depender de BCrypt si no está agregado al classpath del proyecto.
 *
 * Formato de almacenamiento en la columna clave_hash (una sola cadena):
 *   iteraciones:saltBase64:hashBase64
 */
public final class PasswordUtil {

    private static final String ALGORITMO = "PBKDF2WithHmacSHA256";
    private static final int LONGITUD_SAL_BYTES = 16;
    private static final int LONGITUD_HASH_BITS = 256;
    private static final int ITERACIONES = 120_000; // costo razonable para 2025+

    private PasswordUtil() {
    }

    // Genera el valor a guardar en la BD a partir de la contraseña en texto plano
    public static String hashear(String claveTexto) {
        byte[] sal = new byte[LONGITUD_SAL_BYTES];
        new SecureRandom().nextBytes(sal);
        byte[] hash = generarHash(claveTexto.toCharArray(), sal, ITERACIONES);

        return ITERACIONES + ":" + Base64.getEncoder().encodeToString(sal) + ":" +
                Base64.getEncoder().encodeToString(hash);
    }

    // Compara una contraseña en texto plano contra el valor almacenado
    public static boolean verificar(String claveTexto, String valorAlmacenado) {
        try {
            String[] partes = valorAlmacenado.split(":");
            int iteraciones = Integer.parseInt(partes[0]);
            byte[] sal = Base64.getDecoder().decode(partes[1]);
            byte[] hashEsperado = Base64.getDecoder().decode(partes[2]);

            byte[] hashCalculado = generarHash(claveTexto.toCharArray(), sal, iteraciones);
            return constantTimeEquals(hashEsperado, hashCalculado);
        } catch (Exception e) {
            // Formato inesperado en la BD u otro error de verificación: se trata como no válida
            return false;
        }
    }

    private static byte[] generarHash(char[] claveTexto, byte[] sal, int iteraciones) {
        try {
            PBEKeySpec spec = new PBEKeySpec(claveTexto, sal, iteraciones, LONGITUD_HASH_BITS);
            SecretKeyFactory factory = SecretKeyFactory.getInstance(ALGORITMO);
            return factory.generateSecret(spec).getEncoded();
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new IllegalStateException("No se pudo calcular el hash de la contraseña", e);
        }
    }

    // Comparación en tiempo constante para evitar timing attacks
    private static boolean constantTimeEquals(byte[] a, byte[] b) {
        if (a.length != b.length) return false;
        int resultado = 0;
        for (int i = 0; i < a.length; i++) {
            resultado |= a[i] ^ b[i];
        }
        return resultado == 0;
    }
}
