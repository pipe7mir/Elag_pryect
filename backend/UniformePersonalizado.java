package backend;

/**
 * Representa un uniforme confeccionado a la medida.
 * Aplica HERENCIA para reutilizar la base de la clase Uniforme.
 */
public class UniformePersonalizado extends Uniforme {
    private double medidaPecho;
    private double medidaCintura;
    private double medidaCadera;

    public UniformePersonalizado(int id, String nombre, String imagenUrl, 
                                double pecho, double cintura, double cadera) {
        // 'super' llama al constructor de la clase padre (Uniforme)
        super(id, nombre + " (Personalizado)", imagenUrl);
        this.medidaPecho = pecho;
        this.medidaCintura = cintura;
        this.medidaCadera = cadera;
    }

    // Sobrescribimos el método toString para incluir las medidas (Polimorfismo de datos)
    @Override
    public String toString() {
        return super.toString() + " [Medidas: P:" + medidaPecho + " C:" + medidaCintura + " Ca:" + medidaCadera + "]";
    }
}