
package cafeteria.controlador;

import cafeteria.modelo.estructuras.Pila;
import java.text.SimpleDateFormat;
import java.util.Date;

public class HistorialController {
    // Pila para almacenar el historial de operaciones realizadas en la cafetería
    private Pila pilaHistorial = new Pila();

    // Agrega un nuevo registro al historial con marca de tiempo
    public void agregarRegistro(String operacion) {
        // Formato de fecha y hora para el registro
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // Obtener fecha y hora actual
        String timestamp = sdf.format(new Date());
        // Insertar registro en la pila con formato 
        pilaHistorial.push(timestamp + " - " + operacion);
    }

    // Devuelve el historial completo como un arreglo de objetos
    public Object[] getHistorial() {
        return pilaHistorial.toArray();
    }
    
    // Reemplaza el historial actual por el proporcionado
    public void setHistorial(Object[] historial) {
        // Crear una pila nueva para evitar residuos del historial anterior
        pilaHistorial = new Pila();
        // Recorrer el arreglo y volver a insertar cada registro
        for (Object registro : historial) {
            pilaHistorial.push(registro);
        }
    }

    // Limpia todo el historial eliminando los registros uno a uno
    public void limpiarHistorial() {
        // Mientras la pila no esté vacía, retiramos el elemento superior
        while (!pilaHistorial.estaVacia()) {
            pilaHistorial.pop();
        }
    }
}
