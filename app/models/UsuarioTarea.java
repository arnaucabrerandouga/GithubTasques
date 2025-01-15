package models;
import play.db.jpa.Model;

import javax.persistence.*;
import java.util.Date;

@Entity
public class UsuarioTarea extends Model{

    //@Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    //private Long id; // PK: Clave primaria de la tabla UsuarioTarea

    @ManyToOne
    @JoinColumn(name = "usuario_id") // FK: Clave foránea a la tabla Usuario
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "tarea_id") // FK: Clave foránea a la tabla Tarea
    private Tarea tarea;

    private Date dataCreacio; // Fecha en que se asignó la tarea al usuario
    private Date dataLimit;   // Fecha límite en que el usuario debe completar la tarea

    // Constructor
    public UsuarioTarea(Usuario usuario, Tarea tarea, Date dataCreacio, Date dataLimit) {
        this.usuario = usuario;
        this.tarea = tarea;
        this.dataCreacio = dataCreacio;
        this.dataLimit = dataLimit;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Tarea getTarea() {
        return tarea;
    }

    public void setTarea(Tarea tarea) {
        this.tarea = tarea;
    }

    public Date getDataCreacio() {
        return dataCreacio;
    }

    public void setDataCreacio(Date dataCreacio) {
        this.dataCreacio = dataCreacio;
    }

    public Date getDataLimit() {
        return dataLimit;
    }

    public void setDataLimit(Date dataLimit) {
        this.dataLimit = dataLimit;
    }
}
