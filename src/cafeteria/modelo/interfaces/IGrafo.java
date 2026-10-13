package cafeteria.modelo.interfaces;

import cafeteria.modelo.Barista;
import cafeteria.modelo.Cliente;

public interface IGrafo {
    // Establece una relación entre un barista y un cliente
    void agregarRelacion(Barista barista, Cliente cliente);

    // Devuelve una representación en texto del grafo completo barista-cliente
    String imprimirGrafo();
}
