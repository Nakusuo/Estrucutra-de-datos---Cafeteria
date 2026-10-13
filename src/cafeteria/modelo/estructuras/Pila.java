package cafeteria.modelo.estructuras;

import cafeteria.modelo.interfaces.IPila;
import cafeteria.modelo.nodos.NodoPila;

public class Pila implements IPila {
    private NodoPila tope; // nodo que representa la cima de la pila

    @Override
    public void push(Object dato) {
        // creamos un nuevo nodo con el dato recibido
        NodoPila nuevo = new NodoPila(dato);
        // enlazamos el nuevo nodo al tope actual de la pila
        nuevo.setSiguiente(tope);
        // actualizamos el tope para que apunte al nuevo nodo
        tope = nuevo;
    }

    @Override
    public Object pop() {
        // si la pila está vacía, no hay nada que desapilar
        if (tope == null) return null;
        
        // obtenemos el dato del nodo en la cima
        Object dato = tope.getDato();
        // movemos el tope al siguiente nodo
        tope = tope.getSiguiente();
        // devolvemos el dato desapilado
        return dato;
    }

    @Override
    public Object peek() {
        // devuelve el elemento en la cima sin modificar la pila
        return tope != null ? tope.getDato() : null;
    }

    @Override
    public boolean estaVacia() {
        // verifica si la pila no tiene nodos
        return tope == null;
    }

    @Override
    public Object[] toArray() {
        // contar elementos para saber el tamaño del arreglo
        int count = 0;
        NodoPila actual = tope;
        while (actual != null) {
            count++;
            actual = actual.getSiguiente();
        }
        
        // crear arreglo con la longitud exacta
        Object[] array = new Object[count];
        actual = tope;
        for (int i = 0; i < count; i++) {
            array[i] = actual.getDato(); // asignar dato al arreglo
            actual = actual.getSiguiente();
        }
        return array; // devolver la representacion de la pila como arreglo
    }
}
