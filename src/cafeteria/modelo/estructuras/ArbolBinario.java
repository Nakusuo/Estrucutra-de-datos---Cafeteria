package cafeteria.modelo.estructuras;

import cafeteria.modelo.interfaces.IArbolBinario;
import cafeteria.modelo.Cliente;
import cafeteria.modelo.nodos.NodoArbol;

public class ArbolBinario implements IArbolBinario {
    private NodoArbol raiz; // raíz del árbol binario de clientes

    @Override
    public void insertar(Cliente cliente) {
        // iniciamos inserción recursiva desde la raíz
        raiz = insertarRec(raiz, cliente);
    }

    private NodoArbol insertarRec(NodoArbol nodo, Cliente cliente) {
        // si no hay nodo, creamos uno nuevo para el cliente
        if (nodo == null) {
            return new NodoArbol(cliente); // cliente agregado como hoja
        }
        // comparamos IDs para decidir subárbol
        if (cliente.getId() < nodo.getDato().getId()) {
            // id menor: vamos al subárbol izquierdo
            nodo.setIzquierdo(insertarRec(nodo.getIzquierdo(), cliente));
        } else if (cliente.getId() > nodo.getDato().getId()) {
            // id mayor: vamos al subárbol derecho
            nodo.setDerecho(insertarRec(nodo.getDerecho(), cliente));
        }
        return nodo; // devolvemos el nodo actual para mantener enlaces
    }

    @Override
    public boolean buscar(int id) {
        // iniciamos búsqueda recursiva a partir de la raíz
        return buscarRec(raiz, id);
    }

    private boolean buscarRec(NodoArbol nodo, int id) {
        // si llegamos a un nodo nulo, no encontramos al cliente
        if (nodo == null) return false;
        // si el id coincide, cliente encontrado
        if (nodo.getDato().getId() == id) return true;
        // decidimos dirección de la búsqueda
        if (id < nodo.getDato().getId()) {
            // buscamos en el subárbol izquierdo
            return buscarRec(nodo.getIzquierdo(), id);
        }
        // buscamos en el subárbol derecho
        return buscarRec(nodo.getDerecho(), id);
    }
}
