package ar.utn.frba.dds.modelos.modulos.lector;

import ar.utn.frba.dds.modulos.lector.Lector;

import java.util.List;

public class LectorTest {
    public static void main(String[] args) {
        Lector lector = new Lector();
        String rutaArchivo = "./src/main/resources/lector/datosDiezMilSinRepetidos.csv";
        List<String[]> registros = lector.leerCSV(rutaArchivo);

        if (!registros.isEmpty()) {
            for (String[] fila : registros) {
                System.out.println(String.join(" | ", fila));
            }
        }
    }
}
