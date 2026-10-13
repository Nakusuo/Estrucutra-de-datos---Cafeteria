package cafeteria.modelo.interfaces;

public interface IListaEnlazada {
    // Inserta un nuevo elemento al inicio de la lista enlazada
    void insertarAlInicio(Object dato);

    // Inserta un nuevo elemento al final de la lista enlazada
    void insertarAlFinal(Object dato);

    // Elimina la primera ocurrencia del elemento especificado
    void eliminar(Object dato);

    // Verifica si un elemento existe en la lista enlazada
    boolean buscar(Object dato);

    // Convierte el contenido de la lista en un arreglo de objetos
    Object[] toArray();
}
