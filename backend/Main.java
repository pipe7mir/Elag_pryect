package backend;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // 1. Creamos las variantes para un uniforme
        Variante tallaS = new Variante("Talla S", 85000);
        Variante tallaM = new Variante("Talla M", 85000);
        Variante tallaL = new Variante("Talla L", 95000);

        // 2. Creamos el producto principal
        Uniforme medico = new Uniforme(1, "Uniforme Médico Anti-fluido", "assets/medico.jpg");
        medico.agregarVariante(tallaS);
        medico.agregarVariante(tallaM);
        medico.agregarVariante(tallaL);

        // 3. Simulamos la selección del cliente (Lista de productos)
        List<Uniforme> carritoSimulado = new ArrayList<>();
        carritoSimulado.add(medico);

        // 4. Creamos el Pedido (Aquí se activa tu lógica de 15 días hábiles)
        Pedido nuevoPedido = new Pedido(101, "Esnaider Barragán", carritoSimulado, 85000);

        // 5. Mostramos el resultado en consola
        nuevoPedido.generarResumen();
    }
}