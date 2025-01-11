package ar.utn.frba.dds.modelos.modulos.validador;

import ar.utn.frba.dds.modulos.validador.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ValidadorTest {
    Validador validador = Validador.crearValidador();
    Longitud longitud = new Longitud();
    ASCIIImprimible asciiImprimible = new ASCIIImprimible();
    ResultadoValidacion resultadoValidacion = new ResultadoValidacion();
    Top10000 top10000 = new Top10000();
    @Test
    public void contraseniasNulasSonInvalidas() {
        longitud.validar(" ",resultadoValidacion);
        assertFalse(resultadoValidacion.getLaContraseniaEsValida());
    }
    @Test
    public void laContraseniaTieneLongitudMayorA8() {
        longitud.validar("12345678",resultadoValidacion);
        assertTrue(resultadoValidacion.getLaContraseniaEsValida());
        longitud.validar("123456789",resultadoValidacion);
        assertTrue(resultadoValidacion.getLaContraseniaEsValida());
    }

    @Test
    public void laContraseniaTieneLongitudMenorA8() {
        longitud.validar("1234567",resultadoValidacion);
        assertFalse(resultadoValidacion.getLaContraseniaEsValida());
    }

    @Test
    public void laContraseniaTieneLongitudMayorA64() {
        String contrasenia = "123456700000000000000000000000000000000000000000000000000000000000000000000000000000" +
                "00000000000000000000000000000000000000000000000000000000000000000000";
        assertTrue(contrasenia.length()>64);
        longitud.validar(contrasenia, resultadoValidacion);
        assertFalse(resultadoValidacion.getLaContraseniaEsValida());
    }

    @Test
    public void todosLosCaracteresSonASCIIImprimibles() {
        asciiImprimible.validar("asdvgh usadjsad+}hvjsadhjavsdjahvs", resultadoValidacion);
        assertTrue(resultadoValidacion.getLaContraseniaEsValida());
    }

    @Test
    public void laEnieNoEsCaracterASCIIImprimible() {
        asciiImprimible.validar("123asdsadsadsadsadsadsad4ñ}ñ567",resultadoValidacion);
        assertFalse(resultadoValidacion.getLaContraseniaEsValida());
    }

    @Test
    public void passwordEstaEnElTop10000() {
        top10000.validar("password",resultadoValidacion);
        assertFalse(resultadoValidacion.getLaContraseniaEsValida());
    }

    @Test
    public void disenioDeSistemasNoEstaEnElTop10000() {
        top10000.validar("disenioDeSistemas",resultadoValidacion);
        assertTrue(resultadoValidacion.getLaContraseniaEsValida());
    }

    @Test
    public void disenioDeSistemasEsUnaConstraseniaValida() {
        ResultadoValidacion resultadoValidacion = validador.esValida("disenioDeSistemas");
        assertTrue(resultadoValidacion.getLaContraseniaEsValida());
    }

    @Test
    public void diseñoNoEsUnaConstraseniaValida() {
        ResultadoValidacion resultadoValidacion = validador.esValida("diseño");
        assertFalse(resultadoValidacion.getLaContraseniaEsValida());
    }
    
    @Test
    public void dragonNoEsUnaConstraseniaValida() {
        ResultadoValidacion resultadoValidacion = validador.esValida("dragon");
        assertFalse(resultadoValidacion.getLaContraseniaEsValida());
    }
}
