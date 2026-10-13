package cafeteria.controlador;

import cafeteria.modelo.Barista;
import java.util.Arrays;

public class BaristaController {
    // Arreglo inicial para almacenar los baristas registrados en la cafetería
    private Barista[] baristas = new Barista[50];
    // Contador de baristas actualmente almacenados
    private int contBaristas = 0;

    // Método para agregar un nuevo barista al sistema
    // Si el arreglo está lleno, duplicamos su tamaño para más espacio
    public void agregarBarista(String nombre, String apellido, String especialidad, String telefono) {
        // Verificar si aún hay espacio disponible
        if (contBaristas < baristas.length) {
            // Crear y almacenar la nueva instancia de Barista
            baristas[contBaristas] = new Barista(nombre, apellido, especialidad, telefono);
            contBaristas++;
        } else {
            // Duplicar la capacidad del arreglo
            baristas = Arrays.copyOf(baristas, baristas.length * 2);
            // Agregar el barista después de ampliar el arreglo
            baristas[contBaristas] = new Barista(nombre, apellido, especialidad, telefono);
            contBaristas++;
        }
    }

    // Buscar un barista por su ID único
    public Barista buscarBarista(int id) {
        // Recorrer los baristas registrados hasta la cantidad actual
        for (int i = 0; i < contBaristas; i++) {
            // Verificar coincidencia de ID
            if (baristas[i] != null && baristas[i].getId() == id) {
                return baristas[i];
            }
        }
        // Retornar null si no se encuentra el barista
        return null;
    }

    // Buscar un barista por su nombre completo 
    public Barista buscarBaristaPorNombreCompleto(String nombreCompleto) {
        // Recorrer los baristas almacenados
        for (int i = 0; i < contBaristas; i++) {
            if (baristas[i] != null && baristas[i].getNombreCompleto().equalsIgnoreCase(nombreCompleto)) {
                return baristas[i];
            }
        }
        // Devolver null si no hay coincidencias
        return null;
    }

    // Actualizar la información de un barista existente
    public void actualizarBarista(int id, String nombre, String apellido, String especialidad, String telefono) {
        // Buscar el barista por ID
        Barista b = buscarBarista(id);
        if (b != null) {
            // Asignar los nuevos valores a los atributos
            b.setNombre(nombre);
            b.setApellido(apellido);
            b.setEspecialidad(especialidad);
            b.setTelefono(telefono);
        }
    }

    // Eliminar un barista del sistema desplazando elementos del arreglo
    public void eliminarBarista(int id) {
        // Buscar índice del barista a eliminar
        for (int i = 0; i < contBaristas; i++) {
            if (baristas[i] != null && baristas[i].getId() == id) {
                // Desplazar todos los elementos posteriores una posición atrás
                for (int j = i; j < contBaristas - 1; j++) {
                    baristas[j] = baristas[j + 1];
                }
                // Limpiar la última posición y actualizar contador
                baristas[contBaristas - 1] = null;
                contBaristas--;
                break;
            }
        }
    }

    // Obtener la lista de baristas actuales sin espacios nulos
    public Barista[] getBaristas() {
        // Filtrar valores nulos y devolver solo instancias válidas
        return Arrays.stream(baristas, 0, contBaristas)
                     .filter(b -> b != null)
                     .toArray(Barista[]::new);
    }
}
