package backend;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

/**
 * ELAG BI (Business Intelligence) Service v3.6
 * Optimizado con Java NIO.2 y procesamiento funcional (Streams).
 */
public class ReporteService {
    private static final String ARCHIVO = "pedidos_elag.txt";

    /**
     * Genera un balance general utilizando Streams para mayor eficiencia.
     */
    public void generarReporteVentas() {
        System.out.println("\n========================================");
        System.out.println("   REPORTE GLOBAL DE VENTAS - ELAG      ");
        System.out.println("========================================");

        try (Stream<String> lineas = Files.lines(Paths.get(ARCHIVO), StandardCharsets.UTF_8)) {
            double[] estadisticas = {0, 0}; // [0] = totalVentas, [1] = cantidad

            lineas.forEach(linea -> {
                double precio = extraerPrecio(linea);
                if (precio > 0) {
                    estadisticas[0] += precio;
                    estadisticas[1]++;
                }
            });

            System.out.println("Pedidos procesados: " + (int)estadisticas[1]);
            System.out.println("Ingresos totales:   $" + String.format("%.2f", estadisticas[0]));
            System.out.println("----------------------------------------\n");

        } catch (IOException e) {
            System.out.println("INFO: No se encontró historial previo o el archivo está inaccesible.");
        }
    }

    /**
     * Filtra trabajos por club utilizando procesamiento funcional.
     * Requerido por la opción 3 del Menú Principal.
     */
    public void buscarVentasPorClub(String clubBuscado) {
        if (clubBuscado == null || clubBuscado.trim().isEmpty()) {
            System.out.println("⚠️ Error: Ingrese un criterio de búsqueda válido.");
            return;
        }

        String criterio = "Club/Inst: " + clubBuscado.toLowerCase();
        System.out.println("\n--- AUDITORÍA: TRABAJOS PARA [" + clubBuscado.toUpperCase() + "] ---");

        try (Stream<String> lineas = Files.lines(Paths.get(ARCHIVO), StandardCharsets.UTF_8)) {
            long coincidencias = lineas
                .filter(l -> l.toLowerCase().contains(criterio))
                .peek(l -> System.out.println(" -> " + l))
                .count();

            if (coincidencias == 0) {
                System.out.println("x No se registran trabajos para esta institución.");
            }
            System.out.println("--------------------------------------------------\n");

        } catch (IOException e) {
            System.err.println("CRÍTICO: Fallo al leer la base de datos: " + e.getMessage());
        }
    }

    /**
     * Método auxiliar para extraer el monto financiero de cada línea del archivo.
     */
    private double extraerPrecio(String linea) {
        if (linea != null && linea.contains("Total: $")) {
            try {
                String[] partes = linea.split("Total: \\$");
                if (partes.length > 1) {
                    String valor = partes[1].split(" \\|")[0];
                    return Double.parseDouble(valor.trim());
                }
            } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                return 0;
            }
        }
        return 0;
    }
}