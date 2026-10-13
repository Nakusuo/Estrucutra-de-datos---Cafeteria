package cafeteria.modelo.nodos;

import cafeteria.modelo.Cliente;

public class NodoArbol {
    // El dato de tipo Cliente que contiene este nodo
    private final Cliente dato;
    // Referencia al hijo izquierdo en el árbol
    private NodoArbol izquierdo;
    // Referencia al hijo derecho en el árbol
    private NodoArbol derecho;

    // Constructor: inicializa el nodo con un Cliente
    public NodoArbol(Cliente dato) {
        this.dato = dato;
        this.izquierdo = null; // Sin hijo izquierdo inicialmente
        this.derecho = null;   // Sin hijo derecho inicialmente
    }

    // Getter para obtener el Cliente almacenado
    public Cliente getDato() {
        return dato;
    }

    // Getter para obtener el hijo izquierdo
    public NodoArbol getIzquierdo() {
        return izquierdo;
    }

    // Setter para actualizar el hijo izquierdo
    public void setIzquierdo(NodoArbol izquierdo) {
        this.izquierdo = izquierdo;
    }

    // Getter para obtener el hijo derecho
    public NodoArbol getDerecho() {
        return derecho;
    }

    // Setter para actualizar el hijo derecho
    public void setDerecho(NodoArbol derecho) {
        this.derecho = derecho;
    }
}
