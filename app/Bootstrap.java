import play.test.*;
import play.jobs.*;
import models.*;

import java.util.Calendar;
import java.util.Date;

@OnApplicationStart
public class Bootstrap extends Job {

    public void doJob() {
        // Load default data if the database is empty
        if(UsuarioTarea.count() == 0) {
            // Limpiar base de datos (opcional, útil para pruebas)
            Fixtures.deleteDatabase();

            // Crear algunos usuarios de ejemplo con rol
            Usuario usuario1 = new Usuario("Arnau", "arnau@gmail.com", "password", Rol.PADRE); // Usar Rol.PADRE
            usuario1.save();

            Usuario usuario2 = new Usuario("María", "maria@example.com", "password123", Rol.HIJO); // Usar Rol.HIJO
            usuario2.save();

            // Crear algunas tareas de ejemplo
            Tarea tarea1 = new Tarea("Completar proyecto", "Terminar el proyecto de Java", "en progreso", "alta");
            tarea1.save();

            Tarea tarea2 = new Tarea("Estudiar para examen", "Estudiar para el examen de redes", "pendiente", "media");
            tarea2.save();

            // Establecer la fecha de creación (actual) y la fecha límite (posterior)
            Date fechaCreacion = new Date();

            // Sumar 7 días a la fecha de creación para establecer la fecha límite
            Calendar calendario = Calendar.getInstance();
            calendario.setTime(fechaCreacion);
            calendario.add(Calendar.DAY_OF_MONTH, 7);
            Date fechaLimite = calendario.getTime();

            // Crear relaciones entre usuarios y tareas con fechas de creación y límite diferentes
            UsuarioTarea usuarioTarea1 = new UsuarioTarea(usuario1, tarea1, fechaCreacion, fechaLimite);
            usuarioTarea1.save();

            // Ajustar fecha límite para otra tarea (por ejemplo, 10 días después de la creación)
            calendario.add(Calendar.DAY_OF_MONTH, 3);  // Esto suma 3 días adicionales a la fecha límite anterior
            Date fechaLimiteTarea2 = calendario.getTime();

            UsuarioTarea usuarioTarea2 = new UsuarioTarea(usuario2, tarea2, fechaCreacion, fechaLimiteTarea2);
            usuarioTarea2.save();
        }
    }
}
