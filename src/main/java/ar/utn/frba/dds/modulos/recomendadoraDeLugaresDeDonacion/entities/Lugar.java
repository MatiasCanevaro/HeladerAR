package ar.utn.frba.dds.modulos.recomendadoraDeLugaresDeDonacion.entities;

import lombok.Getter;

@Getter
public class Lugar {
    public Integer id;
    public String nombre;
    public Double lat;
    public Double lon;
    public Double distanciaEnKM;
}
