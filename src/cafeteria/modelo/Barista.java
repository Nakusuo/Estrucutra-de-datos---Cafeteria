package cafeteria.modelo;

public class Barista {
    // Contador estático para asignar IDs únicos a cada barista
    private static int contador = 111;

    // ID único e inmutable del barista
    private final int id;
    // Nombre de pila del barista
    private String nombre;
    // Apellido del barista
    private String apellido;
    // Especialidad del barista (por ejemplo: Latte Art, Espresso, etc.)
    private String especialidad;
    // Número de teléfono de contacto del barista
    private String telefono;
    
    // Constructor para crear un nuevo barista
    // Se asigna automáticamente un ID único al instanciar
    public Barista(String nombre, String apellido, String especialidad, String telefono) {
        this.id = contador++;             // Asigna y luego incrementa el contador
        this.nombre = nombre;             // Establece el nombre recibido
        this.apellido = apellido;         // Establece el apellido recibido
        this.especialidad = especialidad; // Establece la especialidad recibida
        this.telefono = telefono;         // Establece el teléfono recibido
    }
    
    // Obtiene el ID único del barista
    public int getId() { 
        return id; 
    }
    
    // Obtiene el nombre de pila del barista
    public String getNombre() { 
        return nombre; 
    }
    
    // Modifica el nombre de pila del barista
    public void setNombre(String nombre) { 
        this.nombre = nombre; 
    }
    
    // Obtiene el apellido del barista
    public String getApellido() { 
        return apellido; 
    }
    
    // Modifica el apellido del barista
    public void setApellido(String apellido) { 
        this.apellido = apellido; 
    }
    
    // Obtiene la especialidad del barista
    public String getEspecialidad() { 
        return especialidad; 
    }
    
    // Modifica la especialidad del barista
    public void setEspecialidad(String especialidad) { 
        this.especialidad = especialidad; 
    }
    
    // Obtiene el teléfono de contacto del barista
    public String getTelefono() { 
        return telefono; 
    }
    
    // Modifica el número de teléfono del barista
    public void setTelefono(String telefono) { 
        this.telefono = telefono; 
    }
    
    // Construye el nombre completo del barista combinando nombre y apellido
    public String getNombreCompleto() {
        return nombre + " " + apellido; 
    }
    
    // Representación en texto del barista
    // Muestra nombre completo y especialidad
    @Override
    public String toString() {
        return getNombreCompleto() + " (" + especialidad + ")"; 
    }
}
