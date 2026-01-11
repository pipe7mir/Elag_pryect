package backend; //

import java.util.ArrayList;
import java.util.List;
import java.util.Collections; // Para optimización de seguridad

public class Uniforme {
    private int id;
    private String nombre;
    private String imagenUrl;
    private List<Variante> variantes;

    public Uniforme(int id, String nombre, String imagenUrl) {
        this.id = id;
        this.nombre = nombre;
        this.imagenUrl = imagenUrl;
        this.variantes = new ArrayList<>();
    }

    // --- MÉTODOS DE ACCESO (Getters) ---
    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public String getImagenUrl() { return imagenUrl; }

    /**
     * Optimización: Devolvemos una lista no modificable para proteger 
     * la integridad de los datos desde fuera de la clase.
     */
    public List<Variante> getVariantes() {
        return Collections.unmodifiableList(variantes);
    }

    // --- LÓGICA DE NEGOCIO ---
    public void agregarVariante(Variante variante) {
        if (variante != null) {
            this.variantes.add(variante);
        }
    }
}