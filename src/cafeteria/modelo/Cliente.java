package cafeteria.modelo;

public class Cliente {
    // Contador estático para asignar IDs únicos a cada cliente
    private static int contador = 321;

    // ID único e inmutable del cliente
    private final int id;
    // Nombre de pila del cliente
    private String nombre;
    // Apellido del cliente
    private String apellido;
    // Edad del cliente
    private int edad;
    // Número de teléfono de contacto del cliente
    private String telefono;
    
    // Constructor para crear un nuevo cliente
    // Al instanciar, se asigna automáticamente un ID único
    public Cliente(String nombre, String apellido, int edad, String telefono) {
        this.id = contador++; // Asigna y luego incrementa el contador
        this.nombre = nombre; // Establece el nombre recibido
        this.apellido = apellido; // Establece el apellido recibido
        this.edad = edad; // Establece la edad recibida
        this.telefono = telefono; // Establece el teléfono recibido
    }
    
    // Obtiene el ID único del cliente
    public int getId() { 
        return id; 
    }
    
    // Obtiene el nombre de pila del cliente
    public String getNombre() { 
        return nombre; 
    }
    
    // Modifica el nombre de pila del cliente
    public void setNombre(String nombre) { 
        this.nombre = nombre; 
    }
    
    // Obtiene el apellido del cliente
    public String getApellido() { 
        return apellido; 
    }
    
    // Modifica el apellido del cliente
    public void setApellido(String apellido) { 
        this.apellido = apellido; 
    }
    
    // Obtiene la edad del cliente
    public int getEdad() { 
        return edad; 
    }
    
    // Actualiza la edad del cliente
    public void setEdad(int edad) { 
        this.edad = edad; 
    }
    
    // Obtiene el teléfono de contacto del cliente
    public String getTelefono() { 
        return telefono; 
    }
    
    // Modifica el número de teléfono del cliente
    public void setTelefono(String telefono) { 
        this.telefono = telefono; 
    }
    
    // Construye el nombre completo del cliente
    public String getNombreCompleto() {
        return nombre + " " + apellido; // Combina nombre y apellido
    }
    
    // Representación en texto del cliente
    // Muestra nombre completo, edad y teléfono
    @Override
    public String toString() {
        return getNombreCompleto() + " - Edad: " + edad + " - Tel: " + telefono;
    }
}

