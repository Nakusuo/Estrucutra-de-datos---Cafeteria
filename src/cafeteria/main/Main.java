package cafeteria.main;

import cafeteria.controlador.PrincipalController;
import cafeteria.vista.Login;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        // Creamos el controlador principal para gestionar la lógica de la aplicación
        PrincipalController controller = new PrincipalController();
        // Cargamos datos de prueba para simular información inicial (clientes, baristas, pedidos, etc.)
        controller.cargarDatosPrueba();
        
        // Iniciamos la interfaz gráfica en el hilo de despacho de eventos de Swing
        SwingUtilities.invokeLater(() -> {
            // Creamos la ventana de login, pasando el controlador para conectar vista y lógica
            Login login = new Login(controller);
            // Hacemos visible el formulario de login para que el usuario pueda iniciar sesión
            login.setVisible(true);
        });
    }
}

