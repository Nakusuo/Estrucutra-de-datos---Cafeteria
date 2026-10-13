package cafeteria.modelo.interfaces;

public interface IPila {
    // Inserta un elemento en la cima de la pila
    void push(Object dato);

    // Retira y devuelve el elemento en la cima de la pila
    Object pop();

    // Muestra el elemento en la cima sin removerlo
    Object peek();

    // Verifica si la pila no contiene elementos
    boolean estaVacia();

    // Convierte la pila en un arreglo de objetos
    Object[] toArray();
}
