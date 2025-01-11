package ar.utn.frba.dds.modelos.entidades;

import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;

public class MainExample implements WithSimplePersistenceUnit {
    public static void main(String[] args) {
        MainExample instance = new MainExample();
        instance.impactar();
    }
    private void impactar(){
        withTransaction(() -> {
        });
    }
}

