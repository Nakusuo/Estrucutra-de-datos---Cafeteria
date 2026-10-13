package cafeteria.controlador;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class HorarioController {
    // Matriz para controlar ocupación en la cafetería: 
    // filas = días de la semana, columnas = franjas horarias
    private final boolean[][] ocupado = new boolean[7][8];
    // Fecha de referencia para la semana actual
    private Date semanaActual = new Date();

    // Marcar una franja horaria como ocupada (por ejemplo, turno de barista o pedido)
    public void asignarOcupacion(int dia, int hora) {
        if (dia >= 0 && dia < 7 && hora >= 0 && hora < 8) {
            ocupado[dia][hora] = true;
        }
    }

    // Liberar una franja horaria ocupada
    public void liberarHorario(int dia, int hora) {
        if (dia >= 0 && dia < 7 && hora >= 0 && hora < 8) {
            ocupado[dia][hora] = false;
        }
    }

    // Consultar si una franja horaria está ocupada
    public boolean estaOcupado(int dia, int hora) {
        if (dia >= 0 && dia < 7 && hora >= 0 && hora < 8) {
            return ocupado[dia][hora];
        }
        // Si los índices no son válidos, consideramos libre
        return false;
    }

    // Obtener la fecha de inicio de la semana actual
    public Date getSemanaActual() {
        return semanaActual;
    }

    // Establecer una nueva fecha de referencia para la semana
    public void setSemanaActual(Date fecha) {
        this.semanaActual = fecha;
    }

    // Generar un arreglo con las fechas de lunes a domingo de la semana actual
    public String[] getFechasSemana() {
        String[] fechas = new String[7];
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Calendar cal = Calendar.getInstance();
        cal.setTime(semanaActual);
        // Ajustar al lunes de la semana correspondiente
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);

        for (int i = 0; i < 7; i++) {
            fechas[i] = sdf.format(cal.getTime());
            cal.add(Calendar.DAY_OF_MONTH, 1);
        }
        return fechas;
    }
}
