package cafeteria.modelo.nodos;

public class NodoListaEnlazada {
    // El objeto que guarda este nodo
    private final Object dato;
    // Referencia al siguiente nodo en la lista
    private NodoListaEnlazada siguiente;

    // Constructor: inicializa el nodo con el dato proporcionado
    public NodoListaEnlazada(Object dato) {
        this.dato = dato;
        this.siguiente = null; // Al crear, no apunta a ningún otro nodo
    }

    // Getter para obtener el dato almacenado
    public Object getDato() {
        return dato;
    }

    // Getter para obtener la referencia al siguiente nodo
    public NodoListaEnlazada getSiguiente() {
        return siguiente;
    }

    // Setter para actualizar la referencia al siguiente nodo
    public void setSiguiente(NodoListaEnlazada siguiente) {
        this.siguiente = siguiente;
    }
}
