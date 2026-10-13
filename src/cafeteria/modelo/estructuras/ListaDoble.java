package cafeteria.modelo.estructuras;

import cafeteria.modelo.Pedido;
import cafeteria.modelo.interfaces.IListaDoble;
import cafeteria.modelo.nodos.NodoListaDoble;

public class ListaDoble implements IListaDoble {
    private NodoListaDoble cabeza; // primer nodo de la lista doble
    private NodoListaDoble cola;   // último nodo de la lista doble

    @Override
    public void agregarAlFinal(Pedido pedido) {
        // creamos un nuevo nodo con el pedido a agregar
        NodoListaDoble nuevo = new NodoListaDoble(pedido);
        if (cabeza == null) {
            // si la lista está vacía, cabeza y cola apuntan al nuevo nodo
            cabeza = cola = nuevo;
        } else {
            // enlazamos el nuevo nodo al final de la lista
            cola.setSiguiente(nuevo);
            nuevo.setAnterior(cola);
            cola = nuevo; // actualizamos cola al nuevo nodo
        }
    }

    @Override
    public Pedido eliminarAlFinal() {
        // si la lista está vacía, no hay nada que eliminar
        if (cola == null) return null;
        
        // obtenemos el pedido del último nodo
        Pedido pedido = cola.getDato();
        if (cabeza == cola) {
            // si sólo hay un nodo, vaciamos la lista
            cabeza = cola = null;
        } else {
            // movemos cola al nodo anterior y desconectamos el último
            cola = cola.getAnterior();
            cola.setSiguiente(null);
        }
        return pedido; // devolvemos el pedido eliminado
    }

    @Override
    public Pedido getPedido(int indice) {
        // recorremos la lista hasta llegar al índice deseado
        NodoListaDoble actual = cabeza;
        for (int i = 0; i < indice && actual != null; i++) {
            actual = actual.getSiguiente();
        }
        // si encontramos el nodo, devolvemos el pedido; si no, retornamos null
        return actual != null ? actual.getDato() : null;
    }

    @Override
    public int getTamaño() {
        // contamos el número de nodos en la lista
        int count = 0;
        NodoListaDoble actual = cabeza;
        while (actual != null) {
            count++;
            actual = actual.getSiguiente();
        }
        return count; // devolvemos el tamaño total
    }
}
