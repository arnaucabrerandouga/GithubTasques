import org.junit.*;
import play.db.jpa.JPA;
import play.test.*;
import javax.persistence.*;

import models.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class BasicTest extends UnitTest {

    @Before
    public void setup() {
        Fixtures.deleteDatabase(); // Elimina los datos de prueba previos (opcional, puedes quitarlo si no quieres que se borren)
    }

    @Test
    public void testCrearDatos() {
        // Crear un usuario con rol asignado (ahora incluye el rol)
        Usuario usuario = new Usuario("Arnau", "arnau@gmail.com", "password", Rol.PADRE); // Asignando el rol PADRE
        usuario.setPerfil("admin"); // Establecer el perfil opcionalmente
        JPA.em().persist(usuario);

        // Crear algunas tareas de ejemplo
        String fechaCreacion = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        Tarea tarea = new Tarea("Completar proyecto", "Terminar el proyecto de Java", "en progreso", "alta", fechaCreacion);
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

        // Verificar que los datos han sido persistidos correctamente
        Usuario usuarioGuardado = Usuario.find("byCorreoElectronico", "arnau@gmail.com").first();
        assertNotNull(usuarioGuardado);  // Verificar que el usuario fue guardado
        assertEquals(Rol.PADRE, usuarioGuardado.getRol()); // Verificar que el rol es el correcto

        Tarea tareaGuardada = Tarea.find("byTitulo", "Completar proyecto").first();  // Cambio aquí
        assertNotNull(tareaGuardada);  // Verificar que la tarea fue guardada

        // Verificar que la relación UsuarioTarea se ha creado
        UsuarioTarea usuarioTareaGuardada = UsuarioTarea.find("byUsuarioAndTarea", usuarioGuardado, tareaGuardada).first();
        assertNotNull(usuarioTareaGuardada);  // Verificar que la relación fue creada correctamente

        // Verificar que el comentario se ha guardado correctamente
        Comentario comentarioGuardado = Comentario.find("byTareaAndUsuario", tareaGuardada, usuarioGuardado).first();
        assertNotNull(comentarioGuardado);  // Verificar que el comentario fue guardado
    }

}
