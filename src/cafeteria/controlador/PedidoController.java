package cafeteria.controlador;

import cafeteria.modelo.Pedido;
import cafeteria.modelo.estructuras.ListaEnlazada;
import cafeteria.modelo.estructuras.ColaPrioridad;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import javax.swing.JOptionPane;

public class PedidoController {
    // Lista enlazada para almacenar todos los pedidos
    private final ListaEnlazada listaPedidos = new ListaEnlazada();
    // Cola de prioridad para gestionar pedidos según urgencia
    private final ColaPrioridad colaPrioridad = new ColaPrioridad();
    // Contador para asignar IDs únicos a cada pedido
    private int idCounter = 1;
    // Controlador principal para acceder a funciones de horario y calendario
    private PrincipalController principalController;

    // Constructor que recibe referencia al controlador principal
    public PedidoController(PrincipalController principalController) {
        this.principalController = principalController;
    }

    // Agrega un pedido si el horario está disponible
    public void agregarPedido(Pedido pedido) {
        // Verificar que el barista esté libre en la fecha y hora indicada
        if (!verificarDisponibilidad(pedido)) {
            // Mostrar mensaje de advertencia si hay conflicto
            JOptionPane.showMessageDialog(null,
                "Horario no disponible para el barista",
                "Conflicto de horario",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        // Asignar un ID único al nuevo pedido
        pedido.setId(idCounter++);
        // Si el pedido es de máxima prioridad, intentamos reprogramarlo si es necesario
        if (pedido.getPrioridad() == 3) {
            reprogramarPedidoAltaPrioridad(pedido);
        }
        // Insertar pedido al final de la lista y en la cola de prioridad
        listaPedidos.insertarAlFinal(pedido);
        colaPrioridad.encolar(pedido, pedido.getPrioridad());
    }

    // Revisa si el horario del barista está libre
    private boolean verificarDisponibilidad(Pedido pedido) {
        int dia = principalController.obtenerDiaSemana(pedido.getFecha());
        int hora = principalController.obtenerIndiceHora(pedido.getHora());
        if (dia < 0 || hora < 0) return false;
        return !principalController.estaOcupado(dia, hora);
    }

    // Intentar mover un pedido de alta prioridad a otro horario o día
    private void reprogramarPedidoAltaPrioridad(Pedido pedido) {
        try {
            SimpleDateFormat sdfHora = new SimpleDateFormat("HH:mm");
            Date horaOriginal = sdfHora.parse(pedido.getHora());
            Calendar cal = Calendar.getInstance();
            cal.setTime(horaOriginal);
            // Intentar nuevas franjas en la misma fecha
            for (int i = 0; i < 8; i++) {
                cal.add(Calendar.MINUTE, 0);
                String nuevaHora = sdfHora.format(cal.getTime());
                if (!existeConflictoHorario(pedido, nuevaHora)) {
                    pedido.setHora(nuevaHora);
                    return;
                }
            }
            // Si no encontramos hora libre, pasar el pedido al día siguiente
            SimpleDateFormat sdfFecha = new SimpleDateFormat("yyyy-MM-dd");
            Date fecha = sdfFecha.parse(pedido.getFecha());
            Calendar calFecha = Calendar.getInstance();
            calFecha.setTime(fecha);
            calFecha.add(Calendar.DATE, 1);
            pedido.setFecha(sdfFecha.format(calFecha.getTime()));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // Comprueba si ya existe un pedido del mismo barista en la nueva hora
    private boolean existeConflictoHorario(Pedido pedido, String nuevaHora) {
        Object[] pedidos = listaPedidos.toArray();
        for (Object obj : pedidos) {
            Pedido p = (Pedido) obj;
            if (p.getBarista().equals(pedido.getBarista()) &&
                p.getFecha().equals(pedido.getFecha()) &&
                p.getHora().equals(nuevaHora)) {
                return true;
            }
        }
        return false;
    }

    // Elimina un pedido tanto de la lista como de la cola de prioridad
    public void eliminarPedido(Pedido pedido) {
        listaPedidos.eliminar(pedido);
        eliminarDeColaPrioridad(pedido);
    }

    // Método auxiliar para reconstruir la cola sin el pedido eliminado
    private void eliminarDeColaPrioridad(Pedido pedido) {
        ColaPrioridad temp = new ColaPrioridad();
        while (!colaPrioridad.estaVacia()) {
            Pedido actual = (Pedido) colaPrioridad.desencolar();
            if (actual.getId() != pedido.getId()) {
                temp.encolar(actual, actual.getPrioridad());
            }
        }
        while (!temp.estaVacia()) {
            Pedido ped = (Pedido) temp.desencolar();
            colaPrioridad.encolar(ped, ped.getPrioridad());
        }
    }

    // Devuelve todos los pedidos registrados
    public Object[] getPedidos() {
        return listaPedidos.toArray();
    }

    // Extrae y devuelve el siguiente pedido de mayor prioridad
    public Pedido siguientePedidoPrioritario() {
        return (Pedido) colaPrioridad.desencolar();
    }

    // Devuelve los pedidos que aún están en la cola de prioridad
    public Object[] getPedidosPrioridad() {
        return colaPrioridad.toArray();
    }
    
    // Buscar un pedido por su ID
    public Pedido buscarPedidoPorId(int id) {
        Object[] pedidos = listaPedidos.toArray();
        for (Object obj : pedidos) {
            Pedido pedido = (Pedido) obj;
            if (pedido.getId() == id) {
                return pedido;
            }
        }
        return null;
    }
}
