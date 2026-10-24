package co.edu.sena.politecnico.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class ClaveUtilTest {

    @Test
    void generarHash_devuelveHashSha256ConocidoParaAbc() {
        String hashEsperado = "ba7816bf8f01cfea414140de5dae2223b00361a396177a9cb410ff61f20015ad";
        assertEquals(hashEsperado, ClaveUtil.generarHash("abc"));
    }

    @Test
    void generarHash_tiene64CaracteresHexadecimales() {
        assertEquals(64, ClaveUtil.generarHash("Clave123*").length());
    }

    @Test
    void generarHash_mismaClaveGeneraMismoHash() {
        assertEquals(ClaveUtil.generarHash("Clave123*"), ClaveUtil.generarHash("Clave123*"));
    }

    @Test
    void generarHash_clavesDistintasGeneranHashDistinto() {
        assertNotEquals(ClaveUtil.generarHash("Clave123*"), ClaveUtil.generarHash("clave123*"));
    }
}
