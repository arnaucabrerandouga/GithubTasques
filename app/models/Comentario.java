package models;
import play.db.jpa.Model;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Comentario extends Model{

    @Id // PK: La clave primaria de la tabla Comentario
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne // FK: Referencia a la tabla Tarea
    @JoinColumn(name = "tarea_id")
    private Tarea tarea;

    @ManyToOne // FK: Referencia a la tabla Usuario
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    private String contenido;
    private Date dataComentario;

    // Constructor
    public Comentario(Tarea tarea, Usuario usuario, String contenido, Date dataComentario) {
        this.tarea = tarea;
        this.usuario = usuario;
        this.contenido = contenido;
        this.dataComentario = dataComentario;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public Tarea getTarea() {
        return tarea;
    }

    public void setTarea(Tarea tarea) {
        this.tarea = tarea;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public Date getDataComentario() {
        return dataComentario;
    }

    public void setDataComentario(Date dataComentario) {
        this.dataComentario = dataComentario;
    }
}
