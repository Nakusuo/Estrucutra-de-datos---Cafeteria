
package cafeteria.modelo.nodos;

public class NodoPila {
    // El objeto que contiene este nodo
    private final Object dato;
    // Referencia al nodo que está debajo de este en la pila
    private NodoPila siguiente;

    // Constructor: inicializa el nodo con el dato proporcionado
    public NodoPila(Object dato) {
        this.dato = dato;
        this.siguiente = null; // Inicialmente no hay nodo debajo
    }

    // Getter para acceder al dato almacenado en el nodo
    public Object getDato() {
        return dato;
    }

    // Getter para obtener la referencia al nodo siguiente
    public NodoPila getSiguiente() {
        return siguiente;
    }

    // Setter para actualizar la referencia al nodo siguiente
    public void setSiguiente(NodoPila siguiente) {
        this.siguiente = siguiente;
    }
}
