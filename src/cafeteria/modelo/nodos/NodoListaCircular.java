package cafeteria.modelo.nodos;

public class NodoListaCircular<T> {
    // El valor genérico que guarda este nodo
    private final T dato;
    // Referencia al siguiente nodo en la lista circular
    private NodoListaCircular<T> siguiente;

    // Constructor: inicializa el nodo con el dato proporcionado
    public NodoListaCircular(T dato) {
        this.dato = dato;
        this.siguiente = null; // Por defecto no apunta a ningún nodo
    }

    // Getter para obtener el dato almacenado
    public T getDato() {
        return dato;
    }

    // Getter para obtener la referencia al siguiente nodo
    public NodoListaCircular<T> getSiguiente() {
        return siguiente;
    }

    // Setter para actualizar la referencia al siguiente nodo
    public void setSiguiente(NodoListaCircular<T> siguiente) {
        this.siguiente = siguiente;
    }
}
