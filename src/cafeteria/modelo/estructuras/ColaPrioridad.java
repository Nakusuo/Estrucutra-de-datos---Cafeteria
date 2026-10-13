package cafeteria.modelo.estructuras;

import cafeteria.modelo.interfaces.IColaPrioridad;
import cafeteria.modelo.nodos.NodoColaPrioridad;

public class ColaPrioridad implements IColaPrioridad {
    private NodoColaPrioridad frente; // inicio de la cola (mayor prioridad)
    private NodoColaPrioridad fin;    // fin de la cola (menor prioridad)

    @Override
    public void encolar(Object dato, int prioridad) {
        // creamos un nuevo nodo con el dato y su nivel de prioridad
        NodoColaPrioridad nuevo = new NodoColaPrioridad(dato, prioridad);
        
        // si la cola está vacía, frente y fin apuntan al nuevo nodo
        if (frente == null) {
            frente = fin = nuevo;
            return;
        }
        
        // si la nueva prioridad es mayor que la del frente, insertamos al inicio
        if (prioridad > ((cafeteria.modelo.Pedido) frente.getDato()).getPrioridad()) {
            nuevo.setSiguiente(frente);
            frente = nuevo; // actualizamos frente
        } else {
            // recorremos la cola para encontrar dónde insertar
            NodoColaPrioridad actual = frente;
            while (actual.getSiguiente() != null &&
                   prioridad <= ((cafeteria.modelo.Pedido) actual.getSiguiente().getDato()).getPrioridad()) {
                actual = actual.getSiguiente(); // avanzamos hasta la posición correcta
            }
            // enlazamos el nuevo nodo en su lugar
            nuevo.setSiguiente(actual.getSiguiente());
            actual.setSiguiente(nuevo);
            // si llega al final, actualizamos fin
            if (nuevo.getSiguiente() == null) fin = nuevo;
        }
    }

    @Override
    public Object desencolar() {
        // si la cola está vacía, no hay nada que desencolar
        if (frente == null) return null;
        
        // tomamos el dato del frente y lo eliminamos
        Object dato = frente.getDato();
        frente = frente.getSiguiente();
        // si al desencolar queda vacía, actualizamos fin
        if (frente == null) fin = null;
        return dato; // devolvemos el elemento con mayor prioridad
    }

    @Override
    public boolean estaVacia() {
        // verifica si no hay nodos en la cola
        return frente == null;
    }

    @Override
    public Object[] toArray() {
        // contamos cuántos elementos hay para crear el arreglo
        int count = 0;
        NodoColaPrioridad actual = frente;
        while (actual != null) {
            count++;
            actual = actual.getSiguiente();
        }
        
        // llenamos el arreglo con los datos de la cola
        Object[] array = new Object[count];
        actual = frente;
        for (int i = 0; i < count; i++) {
            array[i] = actual.getDato(); // agregamos el dato al arreglo
            actual = actual.getSiguiente();
        }
        return array; // devolvemos el estado de la cola como arreglo
    }
}
