package models;
import play.db.jpa.Model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
public class Usuario extends Model{

   //@Id // PK: La clave primaria de la tabla Usuario
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
  //  private Long id;

    private String nombre;
    private String correoElectronico;
    private String contraseña;
    private Date dataRegistro;

    @Column(nullable = true)  // Campo opcional
    private String perfil;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL) // Relación con UsuarioTarea
    private List<UsuarioTarea> usuariosTareas = new ArrayList<>();

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL) // Relación con Comentario
    private List<Comentario> comentarios = new ArrayList<>();

    // Constructor
    public Usuario(String nombre, String correoElectronico, String contraseña) {
        this.nombre = nombre;
        this.correoElectronico = correoElectronico;
        this.contraseña = contraseña;
        this.dataRegistro = new Date(); // Se asigna automáticamente la fecha actual
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreoElectronico() {
        return correoElectronico;
    }

    public void setCorreoElectronico(String correoElectronico) {
        this.correoElectronico = correoElectronico;
    }

    public String getContraseña() {
        return contraseña;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }

    public Date getDataRegistro() {
        return dataRegistro;
    }

    public void setDataRegistro(Date dataRegistro) {
        this.dataRegistro = dataRegistro;
    }

    public String getPerfil() {
        return perfil;
    }

    public void setPerfil(String perfil) {
        this.perfil = perfil;
    }

    public List<UsuarioTarea> getUsuariosTareas() {
        return usuariosTareas;
    }

    public List<Comentario> getComentarios() {
        return comentarios;
    }
}
