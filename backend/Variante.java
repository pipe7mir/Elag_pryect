package backend;

import java.util.Objects;

/**
 * Representa una variante específica (talla/tela) de un uniforme.
 * Optimizado para integridad de datos en el sistema Elag.
 */
public class Variante {
    private final String etiqueta; // Final: el identificador de la variante no cambia
    private double precio;

    public Variante(String etiqueta, double precio) {
        // Validación defensiva: Evita etiquetas vacías o nulas
        if (etiqueta == null || etiqueta.trim().isEmpty()) {
            throw new IllegalArgumentException("La etiqueta de la variante no puede estar vacía.");
        }
        
        // Validación de precio inicial
        if (precio < 0) {
            throw new IllegalArgumentException("El precio inicial no puede ser negativo.");
        }

        this.etiqueta = etiqueta;
        this.precio = precio;
    }

    // --- GETTERS ---
    public String getEtiqueta() { return etiqueta; }
    public double getPrecio() { return precio; }

    /**
     * Actualiza el precio de la variante.
     * Solo permite valores positivos o cero.
     */
    public void setPrecio(double precio) {
        if (precio >= 0) {
            this.precio = precio;
        } else {
            System.err.println("Error: Intento de asignar precio negativo a " + etiqueta);
        }
    }

    @Override
    public String toString() {
        return etiqueta + " ($" + String.format("%.2f", precio) + ")";
    }
}
