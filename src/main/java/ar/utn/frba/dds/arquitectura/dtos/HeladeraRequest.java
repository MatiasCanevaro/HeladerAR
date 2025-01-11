package ar.utn.frba.dds.arquitectura.dtos;

public class HeladeraRequest {
    private String calleProfile;
    private String altura;
    private String nombreCiudad;
    private String provinciaNombre;
    private String latitud;
    private String longitud;
    private String modeloHeladera;

    // Getters and Setters
    public String getCalleProfile() {
        return calleProfile;
    }

    public void setCalleProfile(String calleProfile) {
        this.calleProfile = calleProfile;
    }

    public String getAltura() {
        return altura;
    }

    public void setAltura(String altura) {
        this.altura = altura;
    }

    public String getNombreCiudad() {
        return nombreCiudad;
    }

    public void setNombreCiudad(String nombreCiudad) {
        this.nombreCiudad = nombreCiudad;
    }

    public String getProvinciaNombre() {
        return provinciaNombre;
    }

    public void setProvinciaNombre(String provinciaNombre) {
        this.provinciaNombre = provinciaNombre;
    }

    public String getLatitud() {
        return latitud;
    }

    public void setLatitud(String latitud) {
        this.latitud = latitud;
    }

    public String getLongitud() {
        return longitud;
    }

    public void setLongitud(String longitud) {
        this.longitud = longitud;
    }

    public String getModeloHeladera() {
        return modeloHeladera;
    }

    public void setModeloHeladera(String modeloHeladera) {
        this.modeloHeladera = modeloHeladera;
    }
}