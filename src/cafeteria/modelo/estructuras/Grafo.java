package cafeteria.modelo.estructuras;

import cafeteria.modelo.Barista;
import cafeteria.modelo.Cliente;
import cafeteria.modelo.interfaces.IGrafo;
import java.util.*;

public class Grafo implements IGrafo {
    // Mapea cada barista con su conjunto de clientes
    private final Map<Barista, Set<Cliente>> adyacencia;

    // Constructor: inicializa el grafo con un LinkedHashMap para conservar el orden de inserción
    public Grafo() {
        adyacencia = new LinkedHashMap<>();
    }

    @Override
    public void agregarRelacion(Barista barista, Cliente cliente) {
        // Si el barista no existe en el grafo, lo agregamos con un conjunto vacío
        adyacencia.putIfAbsent(barista, new HashSet<>());
        // Añadimos al cliente al conjunto del barista
        adyacencia.get(barista).add(cliente);
    }

    @Override
    public String imprimirGrafo() {
        // StringBuilder para construir la representación en texto
        StringBuilder sb = new StringBuilder();

        // Encabezado de la tabla de baristas y clientes
        sb.append(" ☕️ BARISTAS").append("\t").append("👤 CLIENTES ATENDIDOS:\n");
        sb.append("------------------").append("\t").append("  ------------------\n");

        // Recorremos cada entrada barista -> conjunto de clientes
        for (Map.Entry<Barista, Set<Cliente>> entry : adyacencia.entrySet()) {
            // Formateamos el nombre del barista con ancho fijo para alineación
            String nombreBarista = String.format("%-20s", entry.getKey().getNombreCompleto());
            sb.append(nombreBarista).append("→    ");

            int count = 0;
            // Listamos los nombres de los clientes separados por comas
            for (Cliente c : entry.getValue()) {
                sb.append(c.getNombreCompleto());
                if (++count < entry.getValue().size()) {
                    sb.append(", "); // separador entre nombres
                }
                if (count % 3 == 0) sb.append("\n").append(String.format("%-25s", "")); // salto de línea cada 3
            }
            sb.append("\n\n"); // espacio extra entre baristas
        }

        // Sección de resumen estadístico
        sb.append("========================\n");
        sb.append(" 📋 RESUMEN ESTADÍSTICO\n");
        sb.append("========================\n");
        sb.append("Total baristas: ").append(adyacencia.size()).append("\n");

        int totalClientes = 0;
        int maxClientes = 0;
        Barista baristaMasOcupado = null;

        // Calculamos totales y encontramos al barista con más clientes
        for (Map.Entry<Barista, Set<Cliente>> entry : adyacencia.entrySet()) {
            int numClientes = entry.getValue().size();
            totalClientes += numClientes;

            if (numClientes > maxClientes) {
                maxClientes = numClientes;
                baristaMasOcupado = entry.getKey(); // actualizamos barista más ocupado
            }
        }

        sb.append("Total clientes únicos: ").append(totalClientes).append("\n");
        if (baristaMasOcupado != null) {
            // Indicamos el barista con mayor carga y cuántos clientes atiende
            sb.append("Barista con más clientes: ")
              .append(baristaMasOcupado.getNombreCompleto())
              .append(" (").append(maxClientes).append(" clientes)\n");
        }

        return sb.toString(); // devolvemos toda la cadena construida
    }
}
