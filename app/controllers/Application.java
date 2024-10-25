package controllers;
import java.util.Calendar;  // Para el manejo de fechas
import java.util.List;      // Si necesitas manejar listas en algún punto
import play.mvc.*;
import models.*;
import play.test.Fixtures;
import java.util.Date;

public class Application extends Controller {

    public static void index() {
        render();
    }

    public static void registrarUsuario(String nombre, String correoElectronico, String contraseña, String perfil) {
        Usuario usuarioExistente = Usuario.find("byCorreoElectronico", correoElectronico).first();

        if (usuarioExistente == null) {
            Usuario nuevoUsuario = new Usuario(nombre, correoElectronico, contraseña);
            if (perfil != null && !perfil.isEmpty()) {
                nuevoUsuario.setPerfil(perfil);
            }
            nuevoUsuario.save();
            renderText("Usuario registrado con éxito.");
        } else {
            renderText("El usuario ya existe.");
        }
    }

    public static void login(String correoElectronico, String contraseña) {
        Usuario usuarioExistente = Usuario.find("byCorreoElectronicoAndContraseña", correoElectronico, contraseña).first();

        if (usuarioExistente != null) {
            renderText("Inicio de sesión exitoso. ¡Bienvenido, " + usuarioExistente.getNombre() + "!");
        } else {
            renderText("Correo electrónico o contraseña incorrectos.");
        }
    }

    public static void iniBD() {
        // Limpiar base de datos (opcional, útil para pruebas)
        Fixtures.deleteDatabase();

        // Crear algunos usuarios de ejemplo (els constructors tenen parámetres)
        Usuario usuario1 = new Usuario("Arnau", "arnau@gmail.com", "password");
        usuario1.save();

        Usuario usuario2 = new Usuario("María", "maria@example.com", "password123");
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

        renderText("Base de datos inicializada con datos de ejemplo.");
    }

    public static void darDeBajaUsuario(String nombre) {
        Usuario usuarioExistente = Usuario.find("byNombre", nombre).first();

        if (usuarioExistente != null) {
            try {
                usuarioExistente.delete();
                renderText("Usuario " + nombre + " y todos sus datos asociados eliminados con éxito.");
            } catch (Exception e) {
                renderText("Error al eliminar el usuario " + nombre + ": " + e.getMessage());
            }
        } else {
            renderText("Usuario no encontrado: " + nombre);
        }
    }
}