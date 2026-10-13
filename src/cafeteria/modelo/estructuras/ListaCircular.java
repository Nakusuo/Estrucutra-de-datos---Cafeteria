package cafeteria.modelo.estructuras;

import cafeteria.modelo.interfaces.IListaCircular;
import cafeteria.modelo.nodos.NodoListaCircular;

public class ListaCircular<T> implements IListaCircular<T> {
    private NodoListaCircular<T> cabeza; // primer nodo de la lista circular
    private NodoListaCircular<T> cola;   // último nodo que apunta a la cabeza

    @Override
    public void agregar(T dato) {
        // creamos un nuevo nodo para el dato recibido
        NodoListaCircular<T> nuevo = new NodoListaCircular<>(dato);
        if (cabeza == null) {
            // lista vacía: nuevo nodo es cabeza y cola, apunta a sí mismo
            cabeza = cola = nuevo;
            nuevo.setSiguiente(cabeza);
        } else {
            // añadimos el nuevo nodo después de la cola
            cola.setSiguiente(nuevo);
            cola = nuevo;             // actualizamos cola al nuevo nodo
            cola.setSiguiente(cabeza); // mantenemos la circularidad
        }
    }

    @Override
    public T siguiente(T actual) {
        // si la lista está vacía, no hay siguiente
        if (cabeza == null) return null;

        NodoListaCircular<T> temp = cabeza;
        // recorremos la lista hasta encontrar el dato actual
        do {
            if (temp.getDato().equals(actual)) {
                // devolvemos el dato del siguiente nodo en la lista circular
                return temp.getSiguiente().getDato();
            }
            temp = temp.getSiguiente();
        } while (temp != cabeza);
        // si no encontramos el elemento, retornamos null
        return null;
    }
}
