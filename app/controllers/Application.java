package controllers;
import java.util.Calendar;  // Para el manejo de fechas
import java.util.List;      // Si necesitas manejar listas en algún punto
import play.mvc.*;
import models.*;
import play.test.Fixtures;
import java.util.Date;
import java.util.ArrayList;  // Importa ArrayList
import java.time.LocalDateTime;  // Para trabajar con fechas y horas
import java.time.format.DateTimeFormatter;  // Para formatear fechas
import org.mindrot.jbcrypt.BCrypt;  // Importa la librería de bcrypt
import play.Logger;  // Asegúrate de tener este import
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;



public class Application extends Controller {

    // Función para mostrar un mensaje de bienvenida (test para Android)
    public static void testAndroid() {
        // Mensaje de bienvenida para la app Android
        renderText("¡Bienvenido al sistema!");
    }

    public static void loginAndroid(String correoElectronico, String contraseña) {

        try {

            Usuario usuarioExistente = Usuario.find("byCorreoElectronico", correoElectronico).first();
            Logger.info("Solicitud de login recibida con correo: " + correoElectronico);
            if (usuarioExistente == null || !BCrypt.checkpw(contraseña, usuarioExistente.getContraseña())) {
                enviarRespuestaError("Correo electrónico o contraseña incorrectos.");
                return;
            }

            if (usuarioExistente.getRol() == null) {
                enviarRespuestaError("El usuario no tiene un rol asignado.");
                return;
            }

            // Respuesta de éxito con datos del usuario
            ResponseMessage respuesta = new ResponseMessage(
                    true,
                    "Inicio de sesión exitoso.",
                    usuarioExistente
            );
            renderJSON(respuesta);

        } catch (Exception e) {
            Logger.error("Error en el inicio de sesión: " + e.getMessage(), e);
            enviarRespuestaError("Error interno del servidor.");
        }
    }

    public static void registrarUsuarioAndroid(String nombre, String correoElectronico, String contraseña, String perfil, String rol) {
        try {
            // Validar datos de entrada
            String mensajeError = validarEntrada(nombre, correoElectronico, contraseña, rol);
            if (mensajeError != null) {
                enviarRespuestaError(mensajeError);
                return;
            }

            // Verificar si ya existe un usuario con el correo proporcionado
            if (Usuario.find("byCorreoElectronico", correoElectronico).first() != null) {
                enviarRespuestaError("El usuario ya existe con ese correo electrónico.");
                return;
            }

            // Crear nuevo usuario
            String hashedPassword = BCrypt.hashpw(contraseña, BCrypt.gensalt());
            Usuario nuevoUsuario = new Usuario(nombre, correoElectronico, hashedPassword, Rol.valueOf(rol.toUpperCase()));

            if (perfil != null && !perfil.isEmpty()) {
                nuevoUsuario.setPerfil(perfil);
            }

            nuevoUsuario.save();

            // Respuesta de éxito con datos del usuario
            ResponseMessage respuesta = new ResponseMessage(
                    true,
                    "Usuario registrado exitosamente.",
                    nuevoUsuario
            );
            renderJSON(respuesta);

        } catch (Exception e) {
            Logger.error("Error al registrar el usuario: " + e.getMessage(), e);
            enviarRespuestaError("Error interno al registrar el usuario.");
        }
    }



    public static void index() {
        render();
    }

    public static void register() {
        renderTemplate("Application/register.html");
    }

    public static void registrarUsuario(String nombre, String correoElectronico, String contraseña, String perfil, String rol) {
        // Validaciones comunes
        String mensajeError = validarEntrada(nombre, correoElectronico, contraseña, rol);
        if (mensajeError != null) {
            enviarRespuestaError(mensajeError);
            return;
        }

        // Verificar si ya existe un usuario con el mismo correo electrónico
        if (Usuario.find("byCorreoElectronico", correoElectronico).first() != null) {
            enviarRespuestaError("El usuario ya existe con ese correo electrónico.");
            return;
        }

        try {
            // Cifrar la contraseña con bcrypt
            String hashedPassword = BCrypt.hashpw(contraseña, BCrypt.gensalt());

            // Crear y guardar el nuevo usuario
            Usuario nuevoUsuario = new Usuario(nombre, correoElectronico, hashedPassword, Rol.valueOf(rol.toUpperCase()));
            if (perfil != null && !perfil.isEmpty()) {
                nuevoUsuario.setPerfil(perfil);
            }
            nuevoUsuario.save();

            // Verificar que el usuario se guarda correctamente
            Usuario usuarioGuardado = Usuario.find("byCorreoElectronico", correoElectronico).first();
            if (usuarioGuardado != null) {
                Logger.info("Usuario creado con correo: " + correoElectronico);
                Logger.info("Rol del usuario: " + rol);

                // Guardar el ID del usuario y el rol en la sesión
                session.put("usuarioId", nuevoUsuario.getId());  // Almacena el ID del usuario en la sesión
                session.put("usuarioRol", nuevoUsuario.getRol().name());  // Almacena el rol del usuario en la sesión

                // Redirige al usuario a su dashboard según el rol
                redirigirPorRol(nuevoUsuario);
            } else {
                Logger.error("No se pudo guardar el usuario en la base de datos.");
                enviarRespuestaError("Hubo un error al registrar el usuario.");
            }

        } catch (Exception e) {
            Logger.error("Error al registrar el usuario: " + e.getMessage(), e);
            enviarRespuestaError("Hubo un error al registrar el usuario: " + e.getMessage());
        }
    }

    public static void login(String correoElectronico, String contraseña) {
        Usuario usuarioExistente = Usuario.find("byCorreoElectronico", correoElectronico).first();

        // Validación de usuario
        if (usuarioExistente == null || !BCrypt.checkpw(contraseña, usuarioExistente.getContraseña())) {
            enviarRespuestaError("Error: Correo electrónico o contraseña incorrectos.");
            return;
        }

        // Validación de rol
        if (usuarioExistente.getRol() == null) {
            enviarRespuestaError("Error: El usuario no tiene un rol asignado.");
            return;
        }

        // Guardar el usuario en renderArgs para su uso posterior
        renderArgs.put("usuario", usuarioExistente);

        // Almacena en la sesión el ID del usuario y su rol
        session.put("usuarioId", usuarioExistente.getId());  // Almacena el ID en la sesión
        session.put("usuarioRol", usuarioExistente.getRol().name());  // Almacena el rol en la sesión

        redirigirPorRol(usuarioExistente);  // Redirige al dashboard correspondiente
    }

    private static void redirigirPorRol(Usuario usuario) {
        Logger.info("Redirigiendo usuario con correo: " + usuario.getCorreoElectronico());
        Logger.info("Rol del usuario: " + usuario.getRol());

        // Guardar el usuario en el contexto de la vista
        renderArgs.put("usuario", usuario);

        String redirectUrl = session.get("redirectUrl");
        if (redirectUrl != null) {
            session.remove("redirectUrl");
            Logger.info("Redirigiendo a URL almacenada: " + redirectUrl);
            redirect(redirectUrl);
            return;
        }

        List<UsuarioTarea> usuarioTareas = UsuarioTarea.find("byUsuario", usuario).fetch();

        // Obtener solo las tareas asociadas
        List<Tarea> tareas = new ArrayList<>();
        for (UsuarioTarea usuarioTarea : usuarioTareas) {
            tareas.add(usuarioTarea.getTarea());
        }

        switch (usuario.getRol()) {
            case PADRE:
                Logger.info("Redirigiendo al Dashboard del PADRE.");
                renderTemplate("Application/PadreDashboard.html");
                break;
            case HIJO:
                Logger.info("Redirigiendo al Dashboard del HIJO.");
                renderTemplate("Application/HijoDashboard.html");
                break;
            default:
                Logger.error("Rol no válido: " + usuario.getRol());
                enviarRespuestaError("Error: Rol no válido.");
                break;
        }
    }

    public static void logout() {
        session.remove("usuarioId"); // Solo elimina el id del usuario
        index(); // Redirige al metodo index()
    }

    public static void darDeBajaUsuario(String nombre) {
        Usuario usuarioExistente = Usuario.find("byNombre", nombre).first();

        if (usuarioExistente == null) {
            enviarRespuestaError("Usuario no encontrado: " + nombre);
            return;
        }

        try {
            usuarioExistente.delete();
            enviarRespuestaError("Usuario " + nombre + " y todos sus datos asociados eliminados con éxito.");
        } catch (Exception e) {
            enviarRespuestaError("Error al eliminar el usuario " + nombre + ": " + e.getMessage());
        }
    }
    public static void agregarTarea(String titulo, String descripcion, String estado, String prioridad, String hijoId) {
        Usuario usuarioPadre = obtenerUsuarioPadre();
        if (usuarioPadre == null) return;

        try {
            // Validación de los campos de tarea
            if (titulo == null || titulo.isEmpty()) {
                enviarRespuestaError("El título es obligatorio.");
                return;
            }
            if (descripcion == null || descripcion.isEmpty()) {
                enviarRespuestaError("La descripción es obligatoria.");
                return;
            }
            if (estado == null || estado.isEmpty()) {
                enviarRespuestaError("El estado es obligatorio.");
                return;
            }
            if (prioridad == null || prioridad.isEmpty()) {
                enviarRespuestaError("La prioridad es obligatoria.");
                return;
            }

            // Fecha de creación
            String fechaFormateada = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

            // Crear nueva tarea
            Tarea nuevaTarea = new Tarea(titulo, descripcion, estado, prioridad, fechaFormateada);
            nuevaTarea.save();

            // Relacionar tarea con hijo (si existe hijoId)
            if (hijoId != null && !hijoId.isEmpty()) {
                Usuario hijo = Usuario.findById(Long.parseLong(hijoId));
                if (hijo != null && hijo.getRol() == Rol.HIJO) {
                    UsuarioTarea usuarioTarea = new UsuarioTarea(hijo, nuevaTarea, new Date(), null);
                    usuarioTarea.save();
                } else {
                    enviarRespuestaError("El hijo seleccionado no existe o no tiene rol de HIJO.");
                    return;
                }
            }

            renderJSON(new ResponseMessage(true, "Tarea creada y asignada con éxito."));
        } catch (Exception e) {
            enviarRespuestaError("Error al crear la tarea: " + e.getMessage());
        }
    }

    public static void editarTarea(Long tareaId, String titulo, String descripcion, String estado, String prioridad) {
        if (!esUsuarioPadre()) return; // Validación de rol simplificada

        try {
            Tarea tarea = Tarea.findById(tareaId);
            if (tarea != null) {
                // Actualizar los campos de la tarea
                tarea.setTitulo(titulo);
                tarea.setDescripcion(descripcion);
                tarea.setEstado(estado);
                tarea.setPrioridad(prioridad);
                tarea.save();
                enviarRespuestaError("Tarea actualizada con éxito.");
            } else {
                enviarRespuestaError("Error: Tarea no encontrada.");
            }
        } catch (Exception e) {
            enviarRespuestaError("Error al editar la tarea: " + e.getMessage());
        }
    }

    public static void eliminarTarea(Long tareaId) {
        if (!esUsuarioPadre()) return; // Validación de rol simplificada

        try {
            Tarea tarea = Tarea.findById(tareaId);
            if (tarea != null) {
                tarea.delete();
                enviarRespuestaError("Tarea eliminada con éxito.");
            } else {
                enviarRespuestaError("Error: Tarea no encontrada.");
            }
        } catch (Exception e) {
            enviarRespuestaError("Error al eliminar la tarea: " + e.getMessage());
        }
    }

    public static void verTareasAsignadas() {
        Usuario usuarioPadre = obtenerUsuarioPadre();
        if (usuarioPadre == null) return;

        List<UsuarioTarea> tareasAsignadas = UsuarioTarea.find("select ut from UsuarioTarea ut where ut.usuario.rol = ? and ut.usuario.padre = ?", Rol.HIJO, usuarioPadre).fetch();

        if (tareasAsignadas.isEmpty()) {
            enviarRespuestaError("No hay tareas asignadas a tus hijos.");
            return;
        }

        renderArgs.put("usuario", usuarioPadre);
        renderArgs.put("tareasAsignadas", tareasAsignadas);
        renderTemplate("Application/VerTareasAsignadas.html");
    }

    public static void marcarTareaCompletada(Long tareaId) {
        if (!esUsuarioPadre()) return; // Validación de rol simplificada

        try {
            Tarea tarea = Tarea.findById(tareaId);
            if (tarea != null) {
                tarea.setEstado("completada");
                tarea.save();
                enviarRespuestaError("Tarea marcada como completada.");
            } else {
                enviarRespuestaError("Error: Tarea no encontrada.");
            }
        } catch (Exception e) {
            enviarRespuestaError("Error al marcar la tarea como completada: " + e.getMessage());
        }
    }



    // Validación genérica para entrada
    private static String validarEntrada(String nombre, String correo, String contraseña, String rol) {
        if (!esNombreValido(nombre)) return "El nombre es obligatorio.";
        if (!esCorreoValido(correo)) return "El correo electrónico no tiene un formato válido.";
        if (!esContraseñaValida(contraseña)) return "La contraseña debe tener al menos 6 caracteres.";
        if (rol == null || rol.isEmpty()) return "El rol no puede estar vacío.";

        try {
            Rol.valueOf(rol.toUpperCase()); // Validación del rol
        } catch (IllegalArgumentException e) {
            return "Rol inválido. Debe ser 'PADRE' o 'HIJO'.";
        }

        return null; // Si todo esta bien
    }

    private static Usuario obtenerUsuarioValido(String rolRequerido) {
        Usuario usuario = renderArgs.get("usuario", Usuario.class);
        if (usuario == null || !usuario.getRol().toString().equals(rolRequerido)) {
            enviarRespuestaError("Error: Solo los usuarios con rol " + rolRequerido + " pueden acceder a esta funcionalidad.");
            return null;
        }
        return usuario;
    }

    private static boolean esUsuarioPadre() {
        return obtenerUsuarioValido("PADRE") != null;
    }

    private static Usuario obtenerUsuarioPadre() {
        return obtenerUsuarioValido("PADRE");
    }

    // Clase para respuestas JSON estructuradas
    static class ResponseMessage {
        boolean success;
        String message;
        Usuario usuario; // Usuario asociado a la respuesta (si es necesario)

        ResponseMessage(boolean success, String message) {
            this.success = success;
            this.message = message;
        }

        ResponseMessage(boolean success, String message, Usuario usuario) {
            this.success = success;
            this.message = message;
            this.usuario = usuario;
        }
    }

    // Metodo para enviar respuesta de error en formato JSON
    private static void enviarRespuestaError(String mensaje) {
        renderJSON(new ResponseMessage(false, mensaje));
    }

    // Metodo para validar un correo electrónico
    private static boolean esCorreoValido(String correoElectronico) {
        return correoElectronico != null && correoElectronico.matches("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$");
    }

    // Metodo para validar una contraseña
    private static boolean esContraseñaValida(String contraseña) {
        return contraseña != null && contraseña.length() >= 6 &&
                contraseña.matches(".*[A-Z].*") &&
                contraseña.matches(".*[0-9].*") &&
                contraseña.matches(".*[!@#$%^&*].*");
    }

    // Metodo para validar un nombre
    private static boolean esNombreValido(String nombre) {
        return nombre != null && !nombre.isEmpty();
    }




}



