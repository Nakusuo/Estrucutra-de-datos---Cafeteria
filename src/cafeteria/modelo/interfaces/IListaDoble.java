package cafeteria.modelo.interfaces;

import cafeteria.modelo.Pedido;

public interface IListaDoble {
    // Agrega un pedido al final de la lista doble
    void agregarAlFinal(Pedido pedido);

    // Elimina y devuelve el último pedido de la lista
    Pedido eliminarAlFinal();

    // Obtiene el pedido en la posición indicada 
    Pedido getPedido(int indice);

    // Calcula el número de pedidos (nodos) en la lista
    int getTamaño();
}
