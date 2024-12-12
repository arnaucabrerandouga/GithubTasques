package controllers;
import java.util.Calendar;  // Para el manejo de fechas
import java.util.List;      // Si necesitas manejar listas en algún punto
import play.mvc.*;
import models.*;
import play.test.Fixtures;
import java.util.Date;
import java.util.ArrayList;  // Importa ArrayList

public class Application extends Controller {

    public static void index() {
        render();
    }

    public static void register() {
        renderTemplate("Application/register.html");
    }

    public static void registrarUsuario(String nombre, String correoElectronico, String contraseña, String perfil, String rol) {
        // Verificar si ya existe un usuario con el mismo correo electrónico
        Usuario usuarioExistente = Usuario.find("byCorreoElectronico", correoElectronico).first();

        if (usuarioExistente == null) {
            try {
                // Verificar si el valor del rol es válido (PADRE o HIJO)
                if (rol == null || rol.isEmpty()) {
                    renderText("Rol no puede estar vacío.");
                    return;
                }

                // Convertir el string 'rol' a un valor del enum 'Rol'
                Rol rolUsuario = Rol.valueOf(rol.toUpperCase()); // Convierte el valor de 'rol' a Rol.PADRE o Rol.HIJO

                // Crear un nuevo usuario con nombre, correo, contraseña y rol
                Usuario nuevoUsuario = new Usuario(nombre, correoElectronico, contraseña, rolUsuario);

                // Si se proporcionó un perfil, se asigna
                if (perfil != null && !perfil.isEmpty()) {
                    nuevoUsuario.setPerfil(perfil);
                }

                // Guardar el usuario en la base de datos
                nuevoUsuario.save();

                // Mensaje de éxito
                renderText("Usuario registrado con éxito.");
            } catch (IllegalArgumentException e) {
                // Si el rol es incorrecto, devolver un mensaje de error
                renderText("Rol inválido. Debe ser 'PADRE' o 'HIJO'.");
            }
        } else {
            // Si el usuario ya existe, mostrar un mensaje de error
            renderText("El usuario ya existe.");
        }
    }

    public static void login(String correoElectronico, String contraseña) {
        // Buscar el usuario en la base de datos
        Usuario usuarioExistente = Usuario.find("byCorreoElectronicoAndContraseña", correoElectronico, contraseña).first();

        if (usuarioExistente != null) {
            // Verificar que el rol del usuario no sea nulo o incorrecto
            if (usuarioExistente.getRol() == null) {
                renderText("Error: El usuario no tiene un rol asignado.");
                return;
            }

            // Comprobar el rol del usuario y redirigir según el tipo de rol
            if (usuarioExistente.getRol() == Rol.PADRE) {
                // Obtener los hijos del usuario padre (suponiendo que puedes hacer esta consulta)
                List<Usuario> hijos = Usuario.find("byRol", Rol.HIJO).fetch();

                // Obtener las tareas asociadas a los hijos
                List<Tarea> tareasHijos = new ArrayList<>();
                for (Usuario hijo : hijos) {
                    List<UsuarioTarea> usuarioTareas = UsuarioTarea.find("byUsuario", hijo).fetch();
                    for (UsuarioTarea usuarioTarea : usuarioTareas) {
                        tareasHijos.add(usuarioTarea.getTarea());
                    }
                }

                // Pasar los hijos y sus tareas a la vista
                renderArgs.put("usuario", usuarioExistente);
                renderArgs.put("hijos", hijos);
                renderArgs.put("tareasHijos", tareasHijos);

                // Redirigir a la página para padres
                renderTemplate("Application/PadreDashboard.html");
            } else if (usuarioExistente.getRol() == Rol.HIJO) {
                // Pasar el usuario hijo a la vista
                renderArgs.put("usuario", usuarioExistente);

                // Redirigir a la página para hijos
                renderTemplate("Application/HijoDashboard.html");
            } else {
                // Si no se encuentra un rol válido (aunque esto no debería ocurrir)
                renderText("Error: Rol no válido.");
            }
        } else {
            // Mostrar mensaje de error si las credenciales son incorrectas
            renderText("Error: Correo electrónico o contraseña incorrectos.");
        }
    }

    public static void logout() {
        session.clear(); // Limpia la sesión actual
        index(); // Redirige al método index()
    }


/*
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
*/
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