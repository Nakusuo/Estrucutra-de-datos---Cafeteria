package cafeteria.modelo.interfaces;

public interface IColaPrioridad {
    // Método para agregar un elemento a la cola con una prioridad asociada
    void encolar(Object dato, int prioridad);

    // Método para extraer y devolver el siguiente elemento según su prioridad
    Object desencolar();

    // Verifica si la cola de prioridad está vacía
    boolean estaVacia();

    // Convierte la cola en un arreglo de objetos
    Object[] toArray();
}
