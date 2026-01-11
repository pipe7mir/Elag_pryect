package backend;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.util.Objects; // Para validaciones robustas

public class Uniforme {
    // Atributos finales para garantizar la integridad del objeto
    private final int id;
    private final String nombre;
    private final String imagenUrl;
    private final List<Variante> variantes;

    public Uniforme(int id, String nombre, String imagenUrl) {
        // Validación defensiva: El nombre no puede ser nulo o vacío
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del uniforme es obligatorio.");
        }
        
        this.id = id;
        this.nombre = nombre;
        this.imagenUrl = (imagenUrl == null) ? "assets/default.jpg" : imagenUrl;
        this.variantes = new ArrayList<>();
    }

    // --- MÉTODOS DE ACCESO (Getters) ---
    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public String getImagenUrl() { return imagenUrl; }

    /**
     * Devuelve una vista de solo lectura de las variantes.
     * Evita modificaciones externas no autorizadas.
     */
    public List<Variante> getVariantes() {
        return Collections.unmodifiableList(variantes);
    }

    // --- LÓGICA DE NEGOCIO ---
    
    /**
     * Agrega una nueva talla/precio al catálogo de este uniforme.
     */
    public void agregarVariante(Variante variante) {
        Objects.requireNonNull(variante, "No se puede agregar una variante nula.");
        this.variantes.add(variante);
    }

    /**
     * Representación textual para depuración en consola.
     */
    @Override
    public String toString() {
        return "Uniforme [ID=" + id + ", Nombre=" + nombre + ", Variantes=" + variantes.size() + "]";
    }
}
