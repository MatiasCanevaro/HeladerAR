package ar.utn.frba.dds.modelos.entidades;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@MappedSuperclass
@Getter
@Setter
public abstract class Persistente {
    @Id
    protected String id;
    @Column(name = "activo", columnDefinition = "TINYINT")
    private Boolean activo;
    @Column(name = "fecha_y_hora_alta", columnDefinition = "DATETIME")
    private LocalDateTime fechaYHoraAlta;
    @Column(name = "fecha_y_hora_modificado", columnDefinition = "DATETIME")
    private LocalDateTime fechaYHoraModificado;
    public Persistente() {
        this.id = UUID.randomUUID().toString().replace("-", "");
        this.fechaYHoraAlta = LocalDateTime.now();
        this.activo = true;
    }
    @PrePersist
    protected void onInsert() {
        this.fechaYHoraAlta = LocalDateTime.now();
        this.fechaYHoraModificado = LocalDateTime.now();
        this.activo = true;
    }
    @PreUpdate
    protected void onUpdate(){
        this.fechaYHoraModificado = LocalDateTime.now();
    }
}
