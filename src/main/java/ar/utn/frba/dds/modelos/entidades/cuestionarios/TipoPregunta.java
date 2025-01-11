package ar.utn.frba.dds.modelos.entidades.cuestionarios;

public enum TipoPregunta {
    UNICA,
    MULTIPLE,
    ABIERTA;
    @Override
    public String toString() {
        return this.name();
    }
}
