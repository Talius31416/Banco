package Java.Intermedio.app;

import java.util.HashMap;
import java.util.Map;

public class SceneManager {

    public static final String VENTANA_INICIO = "/Java/Intermedio/VentanaInicio.fxml";
    public static final String INICIO_SESION = "/Java/Intermedio/InicioDeSesion.fxml";
    public static final String REGISTRO_USUARIO = "/Java/Intermedio/vista.VistaEmpleado.RegistroController.fxml";
    public static final String REGISTRO_EMPLEADO = "/Java/Intermedio/RegistroEmpleado.fxml";
    public static final String VISTA_USUARIO = "/Java/Intermedio/VistaUsuario.fxml";
    public static final String VISTA_EMPLEADO = "/Java/Intermedio/VistaEmpleado.fxml";
    public static final String GESTION_USUARIOS = "/Java/Intermedio/GestionUsuario.fxml";
    public static final String DEPOSITO = "/Java/Intermedio/Deposito.fxml";
    public static final String RETIRO = "/Java/Intermedio/Retiro.fxml";
    public static final String REPORTE_FINANCIERO = "/Java/Intermedio/ReporteFinanciero.fxml";
    public static final String TRANSACCION = "/Java/Intermedio/Transaccion.fxml";

    private static final Map<String, String> titulos = new HashMap<>();

    static {
        titulos.put(VENTANA_INICIO, "Bienvenido");
        titulos.put(INICIO_SESION, "Inicio de Sesión");
        titulos.put(REGISTRO_USUARIO, "Registro de Usuario");
        titulos.put(REGISTRO_EMPLEADO, "Registro de Empleado");
        titulos.put(VISTA_USUARIO, "Panel del Usuario");
        titulos.put(VISTA_EMPLEADO, "Panel del Empleado");
        titulos.put(GESTION_USUARIOS, "Gestión de Usuarios");
        titulos.put(DEPOSITO, "Depósito");
        titulos.put(RETIRO, "Retiro");
        titulos.put(REPORTE_FINANCIERO, "Reporte Financiero");
        titulos.put(TRANSACCION, "Transferencia");
    }

    public static String getTitulo(String fxmlPath) {
        return titulos.getOrDefault(fxmlPath, "Aplicación Banco");
    }
}