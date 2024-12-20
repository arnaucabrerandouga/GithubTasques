import play.test.*;
import play.jobs.*;
import models.*;
import org.mindrot.jbcrypt.BCrypt;  // Asegúrate de importar BCrypt
import java.time.LocalDateTime;  // Para trabajar con fechas y horas
import java.time.format.DateTimeFormatter;  // Para formatear fechas
import java.util.Calendar;
import java.util.Date;

@OnApplicationStart
public class Bootstrap extends Job {

    public void doJob() {
        if(UsuarioTarea.count() == 0) {
            Fixtures.deleteDatabase();

            // Crear algunos usuarios de ejemplo con rol
            // Hashear las contraseñas antes de guardarlas
            String hashedPassword1 = BCrypt.hashpw("Password1!", BCrypt.gensalt());
            Usuario usuario1 = new Usuario("Arnau", "arnau@gmail.com", hashedPassword1, Rol.PADRE);
            usuario1.save();

            String hashedPassword2 = BCrypt.hashpw("Password123!", BCrypt.gensalt());
            Usuario usuario2 = new Usuario("María", "maria@example.com", hashedPassword2, Rol.HIJO);
            usuario2.save();

            // Crear algunas tareas de ejemplo
            String fechaCreacion = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            Tarea tarea1 = new Tarea("Completar proyecto", "Terminar el proyecto de Java", "en progreso", "alta", fechaCreacion);
            tarea1.save();

            Tarea tarea2 = new Tarea("Estudiar para examen", "Estudiar para el examen de redes", "pendiente", "media", fechaCreacion);
            tarea2.save();

            // Relacionar tareas con usuarios
            Date fechaLimite = new Date();
            Calendar calendario = Calendar.getInstance();
            calendario.add(Calendar.DAY_OF_MONTH, 7);
            Date fechaLimiteTarea = calendario.getTime();

            UsuarioTarea usuarioTarea1 = new UsuarioTarea(usuario1, tarea1, new Date(), fechaLimiteTarea);
            usuarioTarea1.save();

            calendario.add(Calendar.DAY_OF_MONTH, 3);
            Date fechaLimiteTarea2 = calendario.getTime();

            UsuarioTarea usuarioTarea2 = new UsuarioTarea(usuario2, tarea2, new Date(), fechaLimiteTarea2);
            usuarioTarea2.save();
        }
    }
}
