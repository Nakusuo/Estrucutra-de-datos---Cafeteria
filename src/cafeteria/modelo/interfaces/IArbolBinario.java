package cafeteria.modelo.interfaces;

import cafeteria.modelo.Cliente;

public interface IArbolBinario {

    // Inserta un nuevo cliente en el árbol.
    void insertar(Cliente cliente);

    // Busca un cliente por su identificador único (ID).
    boolean buscar(int id);
}
