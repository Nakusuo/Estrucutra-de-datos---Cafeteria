package cafeteria.modelo;

public class Pedido {
    // Identificador único del pedido
    private int id;
    // Cliente asociado al pedido
    private Cliente cliente;
    // Barista encargado del pedido
    private Barista barista;
    // Fecha programada para el pedido (ej. "2025-07-01")
    private String fecha;
    // Hora programada para el pedido (ej. "14:30")
    private String hora;
    // Nivel de prioridad del pedido: 1 baja, 2 media, 3 alta
    private int prioridad;
    // Detalle o descripción del pedido
    private String detalle;
    
    // Constructor que inicializa todos los campos del pedido
    public Pedido(int id, Cliente cliente, Barista barista, String fecha, String hora, int prioridad, String detalle) {
        this.id = id;                   // Asigna el ID proporcionado
        this.cliente = cliente;         // Asocia el cliente
        this.barista = barista;         // Asocia el barista
        this.fecha = fecha;             // Establece la fecha
        this.hora = hora;               // Establece la hora
        this.prioridad = prioridad;     // Establece la prioridad
        this.detalle = detalle;         // Establece el detalle
    }
    
    // Obtiene el ID del pedido
    public int getId() { 
        return id; 
    }
    
    // Modifica el ID del pedido
    public void setId(int id) { 
        this.id = id; 
    }
    
    // Obtiene el cliente del pedido
    public Cliente getCliente() { 
        return cliente; 
    }
    
    // Modifica el cliente del pedido
    public void setCliente(Cliente cliente) { 
        this.cliente = cliente; 
    }
    
    // Obtiene el barista del pedido
    public Barista getBarista() { 
        return barista; 
    }
    
    // Modifica el barista del pedido
    public void setBarista(Barista barista) { 
        this.barista = barista; 
    }
    
    // Obtiene la fecha del pedido
    public String getFecha() { 
        return fecha; 
    }
    
    // Modifica la fecha del pedido
    public void setFecha(String fecha) { 
        this.fecha = fecha; 
    }
    
    // Obtiene la hora del pedido
    public String getHora() { 
        return hora; 
    }
    
    // Modifica la hora del pedido
    public void setHora(String hora) { 
        this.hora = hora; 
    }
    
    // Obtiene la prioridad numérica del pedido
    public int getPrioridad() { 
        return prioridad; 
    }
    
    // Modifica la prioridad del pedido
    public void setPrioridad(int prioridad) { 
        this.prioridad = prioridad; 
    }
    
    // Obtiene el detalle del pedido
    public String getDetalle() { 
        return detalle; 
    }
    
    // Modifica el detalle del pedido
    public void setDetalle(String detalle) { 
        this.detalle = detalle; 
    }
    
    // Convierte la prioridad numérica en texto descriptivo
    public String getPrioridadTexto() {
        return switch (prioridad) {
            case 1 -> "Baja";      // Prioridad baja
            case 2 -> "Media";     // Prioridad media
            case 3 -> "Alta";      // Prioridad alta
            default -> "Desconocida"; // Prioridad desconocida
        };
    }
    
    // Representación en texto del pedido para visualización
    @Override
    public String toString() {
        return "Pedido #" + id + " - " + fecha + " " + hora + " (" + getPrioridadTexto() + ")";
    }
}
