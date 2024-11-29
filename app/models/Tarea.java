package models;

import play.db.jpa.Model;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Tarea extends Model{

    //@Id aixo ens ho fa la clase model
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    //private Long id; // PK: Clave primaria de la tabla Tarea
    //podem pusar public en comptes de private
    private String titulo;
    private String descripcion;
    private String estado;
    private String prioridad;

    @OneToMany(mappedBy = "tarea", cascade = CascadeType.ALL)
    private List<UsuarioTarea> usuariosTareas = new ArrayList<>(); // Relación con UsuarioTarea

    @OneToMany(mappedBy = "tarea", cascade = CascadeType.ALL)
    private List<Comentario> comentarios = new ArrayList<>(); // Relación con Comentarios

    // Constructor
    public Tarea(String titulo, String descripcion, String estado, String prioridad) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.estado = estado;
        this.prioridad = prioridad;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(String prioridad) {
        this.prioridad = prioridad;
    }

    public List<UsuarioTarea> getUsuariosTareas() {
        return usuariosTareas;
    }

    public List<Comentario> getComentarios() {
        return comentarios;
    }

    // Método para agregar comentarios
    public void addComentario(Comentario comentario) {
        comentarios.add(comentario);
        comentario.setTarea(this); // Establece la relación bidireccional
    }
}
