
import org.junit.*;
import play.db.jpa.JPA;
import play.test.*;
import javax.persistence.*;


import models.*;

public class BasicTest extends UnitTest {

    @Before
    public void setup() {
        Fixtures.deleteDatabase(); // Elimina los datos de prueba previos (opcional, puedes quitarlo si no quieres que se borren)
    }

    @Test
    public void testCrearDatos() {
        // Crear un usuario (solo con los campos obligatorios)
        Usuario usuario = new Usuario("Arnau", "arnau@gmail.com", "password");
        usuario.setPerfil("admin"); // Establecer el perfil opcionalmente
        JPA.em().persist(usuario);

        // Crear una tarea (sin fechas, ya que ahora se gestionan en la relación UsuarioTarea)
        Tarea tarea = new Tarea("Desarrollar aplicación", "Construir una aplicación de gestión de tareas", "en progreso", "alta");
        JPA.em().persist(tarea);

        // Crear relación usuario-tarea con fechas de creación y límite
        UsuarioTarea usuarioTarea = new UsuarioTarea(usuario, tarea, new java.util.Date(), new java.util.Date());
        JPA.em().persist(usuarioTarea);

        // Crear un comentario en la tarea
        Comentario comentario = new Comentario(tarea, usuario, "Gran progreso!", new java.util.Date());
        tarea.addComentario(comentario);
        JPA.em().persist(comentario);

        // Flushear los datos a la base de datos
        JPA.em().flush();
    }
}
