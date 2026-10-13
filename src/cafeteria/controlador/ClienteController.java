package cafeteria.controlador;

import cafeteria.modelo.Cliente;
import java.util.Arrays;

public class ClienteController {
    // Creamos un arreglo inicial con espacio para 50 clientes
    private Cliente[] clientes = new Cliente[50];
    // Contador para llevar registro de cuántos clientes hay
    private int contClientes = 0;

    // Método para agregar un nuevo cliente al sistema
    // Si el arreglo está lleno, duplicamos su capacidad
    public void agregarCliente(String nombre, String apellido, int edad, String telefono) {
        // Comprobar si necesitamos más espacio
        if (contClientes >= clientes.length) {
            // Duplicar el tamaño del arreglo para poder seguir agregando
            clientes = Arrays.copyOf(clientes, clientes.length * 2);
        }
        // Crear el objeto Cliente y añadirlo al arreglo
        clientes[contClientes++] = new Cliente(nombre, apellido, edad, telefono);
    }

    // Buscar un cliente por su ID
    public Cliente buscarCliente(int id) {
        // Recorremos todos los clientes registrados
        for (int i = 0; i < contClientes; i++) {
            Cliente c = clientes[i];
            // Si encontramos el ID, devolvemos el cliente
            if (c != null && c.getId() == id) {
                return c;
            }
        }
        // Si no existe, devolvemos null
        return null;
    }

    // Buscar un cliente usando su nombre completo 
    public Cliente buscarClientePorNombreCompleto(String nombreCompleto) {
        for (int i = 0; i < contClientes; i++) {
            Cliente c = clientes[i];
            // Comprobamos el nombre completo 
            if (c != null && c.getNombreCompleto().equalsIgnoreCase(nombreCompleto)) {
                return c;
            }
        }
        // Si no hay coincidencias, retornamos null
        return null;
    }

    // Actualizar los datos de un cliente existente
    public void actualizarCliente(int id, String nombre, String apellido, int edad, String telefono) {
        // Buscamos el cliente por ID
        Cliente c = buscarCliente(id);
        if (c != null) {
            // Asignamos los nuevos valores
            c.setNombre(nombre);
            c.setApellido(apellido);
            c.setEdad(edad);
            c.setTelefono(telefono);
        }
    }

    // Eliminar un cliente del arreglo
    public void eliminarCliente(int id) {
        // Buscamos el índice del cliente a eliminar
        for (int i = 0; i < contClientes; i++) {
            Cliente c = clientes[i];
            if (c != null && c.getId() == id) {
                // Desplazamos todos los clientes posteriores una posición atrás
                for (int j = i; j < contClientes - 1; j++) {
                    clientes[j] = clientes[j + 1];
                }
                // Reducimos el contador y limpiamos la última posición
                clientes[--contClientes] = null;
                break;
            }
        }
    }

    // Obtener la lista actual de clientes sin espacios vacíos
    public Cliente[] getClientes() {
        // Filtramos los valores nulos y devolvemos solo los clientes válidos
        return Arrays.stream(clientes, 0, contClientes)
                     .filter(c -> c != null)
                     .toArray(Cliente[]::new);
    }
}
