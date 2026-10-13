package cafeteria.controlador;

import cafeteria.modelo.*;
import cafeteria.modelo.estructuras.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import javax.swing.JOptionPane;

// Controlador principal que orquesta todas las operaciones del sistema de la cafetería.
public class PrincipalController {
    // Instancias de los controladores específicos para cada entidad
    private final BaristaController baristaController = new BaristaController();
    private final ClienteController clienteController = new ClienteController();
    private final PedidoController pedidoController = new PedidoController(this);
    private final HorarioController horarioController = new HorarioController();
    private final HistorialController historialController = new HistorialController();

    // ===================== MÉTODOS BARISTAS =====================

    public void agregarBarista(String nombre, String apellido, String especialidad, String telefono) {
        baristaController.agregarBarista(nombre, apellido, especialidad, telefono);
        historialController.agregarRegistro("Barista agregado: " + nombre + " " + apellido);
    }

    public Barista buscarBarista(int id) {
        return baristaController.buscarBarista(id);
    }

    public Barista buscarBaristaPorNombreCompleto(String nombreCompleto) {
        return baristaController.buscarBaristaPorNombreCompleto(nombreCompleto);
    }

    public Barista[] getBaristas() {
        return baristaController.getBaristas();
    }

    public void actualizarBarista(int id, String n, String a, String e, String t) {
        baristaController.actualizarBarista(id, n, a, e, t);
        historialController.agregarRegistro("Barista actualizado: " + n + " " + a);
    }

    public void eliminarBarista(int id) {
        Barista b = baristaController.buscarBarista(id);
        if (b != null) {
            String nc = b.getNombreCompleto();
            baristaController.eliminarBarista(id);
            historialController.agregarRegistro("Barista eliminado: " + nc);
        }
    }

    public ListaCircular getListaCircularBaristas() {
        ListaCircular lc = new ListaCircular();
        for (Barista b : getBaristas()) lc.agregar(b);
        return lc;
    }

    // ===================== MÉTODOS CLIENTES =====================

    public void agregarCliente(String n, String a, int e, String t) {
        clienteController.agregarCliente(n, a, e, t);
        historialController.agregarRegistro("Cliente agregado: " + n + " " + a);
    }

    public Cliente buscarCliente(int id) {
        return clienteController.buscarCliente(id);
    }

    public Cliente buscarClientePorNombreCompleto(String nombreCompleto) {
        return clienteController.buscarClientePorNombreCompleto(nombreCompleto);
    }

    public Cliente[] getClientes() {
        return clienteController.getClientes();
    }

    public List<Cliente> getClientesOrdenadosPorEdad() {
        return Arrays.stream(getClientes())
                     .sorted(Comparator.comparingInt(Cliente::getEdad))
                     .collect(Collectors.toList());
    }

    public void actualizarCliente(int id, String n, String a, int e, String t) {
        clienteController.actualizarCliente(id, n, a, e, t);
        historialController.agregarRegistro("Cliente actualizado: " + n + " " + a);
    }

    public void eliminarCliente(int id) {
        Cliente c = clienteController.buscarCliente(id);
        if (c != null) {
            String nc = c.getNombreCompleto();
            clienteController.eliminarCliente(id);
            historialController.agregarRegistro("Cliente eliminado: " + nc);
        }
    }

    // ===================== MÉTODOS PEDIDOS =====================

    public void agregarPedido(Pedido p) {
        int dia = obtenerDiaSemana(p.getFecha());
        int hora = obtenerIndiceHora(p.getHora());

        if (dia >= 0 && hora >= 0 && !horarioController.estaOcupado(dia, hora)) {
            pedidoController.agregarPedido(p);
            horarioController.asignarOcupacion(dia, hora);
            historialController.agregarRegistro("Pedido agregado: " + p.getCliente().getNombreCompleto());
        } else {
            JOptionPane.showMessageDialog(null,
                "El horario seleccionado no está disponible.",
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    public Pedido buscarPedidoPorId(int id) {
        return pedidoController.buscarPedidoPorId(id);
    }

    public Pedido siguientePedidoPrioritario() {
        return pedidoController.siguientePedidoPrioritario();
    }

    public Pedido[] getPedidos() {
        Object[] arr = pedidoController.getPedidos();
        return Arrays.copyOf(arr, arr.length, Pedido[].class);
    }

    public void eliminarPedido(Pedido p) {
        pedidoController.eliminarPedido(p);
        historialController.agregarRegistro("Pedido eliminado: " + p.getCliente().getNombreCompleto());

        int dia = obtenerDiaSemana(p.getFecha());
        int hora = obtenerIndiceHora(p.getHora());
        if (dia >= 0 && hora >= 0) {
            horarioController.liberarHorario(dia, hora);
        }
    }

    public Object[] getPedidosPrioridad() {
        return pedidoController.getPedidosPrioridad();
    }

    // ===================== MÉTODOS AUXILIARES =====================

    public int obtenerDiaSemana(String fecha) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date date = sdf.parse(fecha);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            int diaSemana = cal.get(Calendar.DAY_OF_WEEK);
            return (diaSemana + 5) % 7;  // Lunes=0, Domingo=6
        } catch (Exception e) {
            return -1;
        }
    }

    public int obtenerIndiceHora(String horaStr) {
        try {
            int hora = Integer.parseInt(horaStr.split(":")[0]);
            if (hora >= 8 && hora <= 15) {
                return hora - 8;
            }
            return -1;
        } catch (Exception e) {
            return -1;
        }
    }

    public boolean estaOcupado(int dia, int hora) {
        return horarioController.estaOcupado(dia, hora);
    }

    // ===================== MÉTODOS HORARIO =====================

    public Date getSemanaActual() {
        return horarioController.getSemanaActual();
    }

    public void setSemanaActual(Date fecha) {
        horarioController.setSemanaActual(fecha);
    }

    public String[] getFechasSemana() {
        return horarioController.getFechasSemana();
    }

    // ===================== MÉTODOS HISTORIAL =====================

    public Object[] getHistorial() {
        return historialController.getHistorial();
    }

    public void limpiarHistorial() {
        historialController.limpiarHistorial();
    }

    public void setHistorial(Object[] historial) {
        historialController.setHistorial(historial);
    }

    // ===================== CARGA DE DATOS DE PRUEBA =====================

    public void cargarDatosPrueba() {
        // Datos de baristas
        String[][] datosBaristas = {
            {"Juan",       "Perez",      "Especialista en Espresso",        "960-123-456"},
            {"Maria",      "Garcia",     "Barista Latte Art",                "961-234-567"},
            {"Carlos",     "Lopez",      "Experto en Café de Origen",       "962-345-678"},
            {"Ana",        "Martinez",   "Especialista en Té",               "963-456-789"},
            {"Luis",       "Rodriguez",  "Barista de Cafés Fríos",           "964-567-890"},
            {"Sofia",      "Hernandez",  "Experta en Pastelería",            "965-678-901"},
            {"Pedro",      "Gonzalez",   "Barista de Café Espresso",         "966-789-012"},
            {"Laura",      "Diaz",       "Especialista en Bebidas",          "967-890-123"},
            {"Jorge",      "Sanchez",    "Barista de Cafés Especiales",     "968-901-234"},
            {"Carmen",     "Romero",     "Experta en Latte Art",             "969-012-345"},
            {"Miguel",     "Angel",      "Barista Senior",                   "970-123-456"},
            {"Elena",      "Vargas",     "Especialista en Café Orgánico",   "971-234-567"},
            {"Ricardo",    "Morales",    "Barista de Café Turco",            "972-345-678"},
            {"Patricia",   "Castro",     "Experta en Café Helado",           "973-456-789"},
            {"Francisco",  "Nuñez",      "Barista en Café de Especialidad", "974-567-890"},
            {"Isabel",     "Jimenez",    "Especialista en Café Descafeinado","975-678-901"},
            {"Jose",       "Ortega",     "Barista de Café Espresso",         "976-789-012"},
            {"Silvia",     "Paredes",    "Experta en Café Orgánico",         "977-890-123"},
            {"Fernando",   "Rios",       "Barista Senior",                   "978-901-234"},
            {"Beatriz",    "Soto",       "Especialista en Café de Origen",  "979-012-345"},
            {"Roberto",    "Mendoza",    "Barista Latte Art",                "980-123-456"},
            {"Adriana",    "Rojas",      "Experta en Café Helado",           "981-234-567"},
            {"Raul",       "Herrera",    "Barista de Café Espresso",         "982-345-678"},
            {"Daniela",    "Mora",       "Especialista en Pastelería",      "983-456-789"},
            {"Gustavo",    "Campos",     "Barista de Café Turco",            "984-567-890"},
            {"Gabriela",   "Solis",      "Experta en Latte Art",             "985-678-901"},
            {"Arturo",     "Valdez",     "Especialista en Café Descafeinado","986-789-012"},
            {"Mariana",    "Cordero",    "Barista Senior",                   "987-890-123"},
            {"Ernesto",    "Vega",       "Experto en Café Orgánico",         "988-901-234"},
            {"Lucia",      "Rios",       "Especialista en Café de Origen",  "989-012-345"}
        };
        for (String[] barista : datosBaristas) {
            agregarBarista(barista[0], barista[1], barista[2], barista[3]);
        }

        // Datos de clientes
        String[][] datosClientes = {
            {"Andres",     "Gómez",     "25", "912-345-678"},
            {"Carolina",   "Ruiz",      "30", "913-456-789"},
            {"Felipe",     "Díaz",      "65", "914-567-890"},
            {"Valeria",    "Mendoza",   "28", "915-678-901"},
            {"Roberto",    "Castro",    "62", "916-789-012"},
            {"Camila",     "Soto",      "15", "917-890-123"},
            {"Diego",      "Herrera",   "61", "918-901-234"},
            {"Natalia",    "Vargas",    "12", "919-012-345"},
            {"Javier",     "Paredes",   "10", "920-123-456"},
            {"Fernanda",   "Rojas",     "33", "921-234-567"},
            {"Manuel",     "Ortega",    "9", "922-345-678"},
            {"Daniela",    "Campos",    "71", "923-456-789"},
            {"Alejandro",  "Solís",     "12", "924-567-890"},
            {"Marcela",    "Valdez",    "75", "925-678-901"},
            {"Sergio",     "Cordero",   "68", "926-789-012"},
            {"Ana",        "Vega",      "27", "927-890-123"},
            {"Oscar",      "Ríos",      "48", "928-901-234"},
            {"Luisa",      "Mora",      "12", "929-012-345"},
            {"Ricardo",    "Jiménez",   "65", "930-123-456"},
            {"Sofia",      "Núñez",     "14", "931-234-567"},
            {"Arturo",     "López",     "67", "932-345-678"},
            {"Patricia",   "Romero",    "41", "933-456-789"},
            {"Gustavo",    "Hernández", "52", "934-567-890"},
            {"Carmen",     "García",    "26", "935-678-901"},
            {"Jose",       "Martínez",  "64", "936-789-012"},
            {"Laura",      "Pérez",     "39", "937-890-123"},
            {"Miguel",     "Sánchez",   "58", "938-901-234"},
            {"Elena",      "González",  "13", "939-012-345"},
            {"Carlos",     "Morales",   "66", "940-123-456"},
            {"Adriana",    "Castro",    "17", "941-234-567"}
        };
        for (String[] cliente : datosClientes) {
            agregarCliente(cliente[0], cliente[1], Integer.parseInt(cliente[2]), cliente[3]);
        }

        // Datos de pedidos: Cliente, Barista, Fecha, Hora, Prioridad, Detalle
        String[][] datosPedidos = {
            {"Andres Gómez", "Juan Perez", "2023-10-01", "09:00", "3", "Café espresso doble"},
            {"Carolina Ruiz", "Maria Garcia", "2023-10-01", "10:00", "2", "Latte grande"},
            {"Felipe Díaz", "Carlos Lopez", "2023-10-02", "11:00", "1", "Café con leche"},
            {"Valeria Mendoza", "Ana Martinez", "2023-10-02", "12:00", "2", "Té verde"},
            {"Roberto Castro", "Luis Rodriguez", "2023-10-03", "13:00", "3", "Capuchino"},
            {"Camila Soto", "Sofia Hernandez", "2023-10-03", "14:00", "2", "Pastel de zanahoria"},
            {"Diego Herrera", "Pedro Gonzalez", "2023-10-04", "15:00", "1", "Café americano"},
            {"Natalia Vargas", "Laura Diaz", "2023-10-04", "08:00", "2", "Té chai"},
            {"Javier Paredes", "Jorge Sanchez", "2023-10-05", "09:00", "3", "Mocha"},
            {"Fernanda Rojas", "Carmen Romero", "2023-10-05", "10:00", "1", "Café filtrado"},
            {"Manuel Ortega", "Miguel Angel", "2023-10-06", "11:00", "2", "Espresso simple"},
            {"Daniela Campos", "Elena Vargas", "2023-10-06", "12:00", "3", "Té negro"},
            {"Alejandro Solis", "Ricardo Morales", "2023-10-07", "13:00", "1", "Café con leche"},
            {"Marcela Valdez", "Patricia Castro", "2023-10-07", "14:00", "2", "Capuchino"},
            {"Sergio Cordero", "Francisco Nuñez", "2023-10-08", "15:00", "3", "Latte"},
            {"Ana Vega", "Isabel Jimenez", "2023-10-08", "08:00", "1", "Espresso doble"},
            {"Oscar Rios", "Jose Ortega", "2023-10-09", "09:00", "2", "Café americano"},
            {"Luisa Mora", "Silvia Paredes", "2023-10-09", "10:00", "3", "Té verde"},
            {"Ricardo Jimenez", "Fernando Rios", "2023-10-10", "11:00", "1", "Café filtrado"},
            {"Sofia Nunez", "Beatriz Soto", "2023-10-10", "12:00", "2", "Latte"},
            {"Arturo Lopez", "Roberto Mendoza", "2023-10-11", "13:00", "3", "Mocha"},
            {"Patricia Romero", "Adriana Rojas", "2023-10-11", "14:00", "1", "Té chai"},
            {"Gustavo Hernandez", "Raul Herrera", "2023-10-12", "15:00", "2", "Capuchino"},
            {"Carmen Garcia", "Daniela Mora", "2023-10-12", "08:00", "3", "Espresso"},
            {"Jose Martinez", "Gustavo Campos", "2023-10-13", "09:00", "1", "Café americano"},
            {"Laura Perez", "Gabriela Solis", "2023-10-13", "10:00", "2", "Latte"},
            {"Miguel Sanchez", "Arturo Valdez", "2023-10-14", "11:00", "3", "Café con leche"},
            {"Elena Gonzalez", "Mariana Cordero", "2023-10-14", "12:00", "1", "Té negro"},
            {"Carlos Morales", "Ernesto Vega", "2023-10-15", "13:00", "2", "Capuchino"},
            {"Adriana Castro", "Lucia Rios", "2023-10-15", "14:00", "3", "Pastel de chocolate"}
        };
        int idPedido = 1; // Contador para asignar ID único a cada pedido
        for (String[] pedido : datosPedidos) {
            Cliente cliente = buscarClientePorNombreCompleto(pedido[0]);
            Barista barista = buscarBaristaPorNombreCompleto(pedido[1]);
            if (cliente != null && barista != null) {
                Pedido nuevoPedido = new Pedido(
                    idPedido++, cliente, barista,
                    pedido[2], pedido[3],
                    Integer.parseInt(pedido[4]), pedido[5]
                );
                agregarPedido(nuevoPedido);
            }
        }
    }
}
