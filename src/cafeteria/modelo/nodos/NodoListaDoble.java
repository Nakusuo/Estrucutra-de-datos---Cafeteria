package cafeteria.modelo.nodos;

import cafeteria.modelo.Pedido;

public class NodoListaDoble {
    // El dato de tipo Pedido que almacena este nodo
    private final Pedido dato;
    // Referencia al nodo anterior en la lista
    private NodoListaDoble anterior;
    // Referencia al siguiente nodo en la lista
    private NodoListaDoble siguiente;

    // Constructor: inicializa el nodo con un Pedido
    public NodoListaDoble(Pedido dato) {
        this.dato = dato;
        this.anterior = null; // Sin enlace previo inicialmente
        this.siguiente = null;  // Sin enlace siguiente inicialmente
    }

    // Getter para obtener el pedido almacenado
    public Pedido getDato() {
        return dato;
    }

    // Getter para obtener el nodo anterior
    public NodoListaDoble getAnterior() {
        return anterior;
    }

    // Setter para actualizar el nodo anterior
    public void setAnterior(NodoListaDoble anterior) {
        this.anterior = anterior;
    }

    // Getter para obtener el nodo siguiente
    public NodoListaDoble getSiguiente() {
        return siguiente;
    }

    // Setter para actualizar el nodo siguiente
    public void setSiguiente(NodoListaDoble siguiente) {
        this.siguiente = siguiente;
    }
}
