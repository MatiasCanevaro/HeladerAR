package ar.utn.frba.dds.modelos.entidades.accesoAlimentario;

import ar.utn.frba.dds.modelos.entidades.formasDeColaboracion.GeneradorDeCodigoAlfanumerico;
import ar.utn.frba.dds.modelos.repositorios.RepositorioTarjeta;
import org.junit.jupiter.api.Test;

public class GeneradorDeCodigoAlfanumericoTest {

    @Test
    public void generadorOriginal() {
        RepositorioTarjeta repositorioTarjeta = new RepositorioTarjeta();
        for (int i = 0; i < 10000; i++) {
            System.out.println(GeneradorDeCodigoAlfanumerico.generarCodigoOriginal(repositorioTarjeta));
        }
    }
    @Test
    public void generador2() {
        RepositorioTarjeta repositorioTarjeta = new RepositorioTarjeta();
        for (int i = 0; i < 10000; i++) {
            System.out.println(GeneradorDeCodigoAlfanumerico.generarCodigo(repositorioTarjeta));
        }
    }
}
