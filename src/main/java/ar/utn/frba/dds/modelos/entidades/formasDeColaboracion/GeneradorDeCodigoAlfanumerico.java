package ar.utn.frba.dds.modelos.entidades.formasDeColaboracion;

import ar.utn.frba.dds.modelos.repositorios.RepositorioTarjeta;

import java.util.UUID;

public class GeneradorDeCodigoAlfanumerico {

    public synchronized static String generarCodigo(RepositorioTarjeta repositorioTarjeta) {
        UUID uuid = UUID.randomUUID();
        String uuidString = uuid.toString();
        String primeros11Caracteres = uuidString.substring(0, 10);
        while(repositorioTarjeta.buscarPorId(primeros11Caracteres, "Tarjeta") != null){
            uuid = UUID.randomUUID();
            uuidString = uuid.toString();
            primeros11Caracteres = uuidString.substring(0, 10);
        }
        return primeros11Caracteres;
    }
    public synchronized static String generarCodigoOriginal(RepositorioTarjeta repositorioTarjeta) {
        UUID uuid = UUID.randomUUID();
        String uuidString = uuid.toString();
        String primeros11Caracteres = uuidString.substring(0, 10);
        while(repositorioTarjeta.buscar(primeros11Caracteres)!=null){
            uuid = UUID.randomUUID();
            uuidString = uuid.toString();
            primeros11Caracteres = uuidString.substring(0, 10);
        }
        return primeros11Caracteres;
    }
}

