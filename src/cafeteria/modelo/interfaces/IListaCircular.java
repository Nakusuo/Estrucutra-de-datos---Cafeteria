package cafeteria.modelo.interfaces;

public interface IListaCircular<T> {
    // Agrega un nuevo elemento al final de la lista circular
    void agregar(T dato);

    // Obtiene el siguiente elemento en la lista circular a partir del actual
    T siguiente(T actual);
}
