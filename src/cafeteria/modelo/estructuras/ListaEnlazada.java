package cafeteria.modelo.estructuras;

import cafeteria.modelo.interfaces.IListaEnlazada;
import cafeteria.modelo.nodos.NodoListaEnlazada;

public class ListaEnlazada implements IListaEnlazada {
    private NodoListaEnlazada cabeza; // primer nodo de la lista enlazada

    @Override
    public void insertarAlInicio(Object dato) {
        // creamos un nuevo nodo con el dato proporcionado
        NodoListaEnlazada nuevo = new NodoListaEnlazada(dato);
        // apuntamos el nuevo nodo al inicio actual de la lista
        nuevo.setSiguiente(cabeza);
        // actualizamos cabeza para que sea el nuevo nodo
        cabeza = nuevo;
    }

    @Override
    public void insertarAlFinal(Object dato) {
        // creamos un nuevo nodo con el dato
        NodoListaEnlazada nuevo = new NodoListaEnlazada(dato);
        // si la lista está vacía, el nuevo nodo es la cabeza
        if (cabeza == null) {
            cabeza = nuevo;
            return;
        }
        // en caso contrario, buscamos el último nodo
        NodoListaEnlazada actual = cabeza;
        while (actual.getSiguiente() != null) {
            actual = actual.getSiguiente(); // avanzamos al siguiente nodo
        }
        // enlazamos el último nodo al nuevo nodo
        actual.setSiguiente(nuevo);
    }

    @Override
    public void eliminar(Object dato) {
        // si la lista está vacía, no hay nada que eliminar
        if (cabeza == null) return;
        
        // si el dato está en la cabeza, removemos el primer nodo
        if (cabeza.getDato().equals(dato)) {
            cabeza = cabeza.getSiguiente();
            return;
        }
        
        // recorremos la lista para encontrar el nodo anterior al que queremos eliminar
        NodoListaEnlazada actual = cabeza;
        while (actual.getSiguiente() != null &&
               !actual.getSiguiente().getDato().equals(dato)) {
            actual = actual.getSiguiente(); // avanzamos hasta encontrar el dato
        }
        
        // si encontramos el nodo a eliminar, saltamos sobre él
        if (actual.getSiguiente() != null) {
            actual.setSiguiente(actual.getSiguiente().getSiguiente());
        }
    }

    @Override
    public boolean buscar(Object dato) {
        // iniciamos la búsqueda desde la cabeza
        NodoListaEnlazada actual = cabeza;
        while (actual != null) {
            // si encontramos el dato, retornamos true
            if (actual.getDato().equals(dato)) return true;
            actual = actual.getSiguiente(); // avanzamos al siguiente nodo
        }
        // si llegamos al final, el dato no está en la lista
        return false;
    }

    @Override
    public Object[] toArray() {
        // contamos los elementos para crear el arreglo
        int count = 0;
        NodoListaEnlazada actual = cabeza;
        while (actual != null) {
            count++;
            actual = actual.getSiguiente();
        }
        
        // construimos el arreglo con la longitud exacta
        Object[] array = new Object[count];
        actual = cabeza;
        for (int i = 0; i < count; i++) {
            array[i] = actual.getDato(); // asignamos el dato al arreglo
            actual = actual.getSiguiente(); // avanzamos al siguiente nodo
        }
        return array; // devolvemos el estado de la lista como arreglo
    }
}
