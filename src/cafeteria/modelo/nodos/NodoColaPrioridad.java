package cafeteria.modelo.nodos;

public class NodoColaPrioridad {
    // El dato que almacena este nodo
    private final Object dato;
    // La prioridad asociada al dato; a menor valor, mayor prioridad 
    private final int prioridad;
    // Referencia al siguiente nodo en la cola de prioridad
    private NodoColaPrioridad siguiente;

    // Constructor: inicializa el dato y su prioridad, el siguiente se establece en null por defecto
    public NodoColaPrioridad(Object dato, int prioridad) {
        this.dato = dato;
        this.prioridad = prioridad;
        this.siguiente = null; // No hay siguiente al crearse
    }

    // Getter para obtener el dato almacenado
    public Object getDato() {
        return dato;
    }

    // Getter para obtener la prioridad del dato
    public int getPrioridad() {
        return prioridad;
    }

    // Getter para obtener la referencia al siguiente nodo
    public NodoColaPrioridad getSiguiente() {
        return siguiente;
    }

    // Setter para actualizar la referencia al siguiente nodo en la cola
    public void setSiguiente(NodoColaPrioridad siguiente) {
        this.siguiente = siguiente;
    }
}
