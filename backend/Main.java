package backend;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * ELAG CORE v3.8 | Sistema de Gestión de Confección Unificado.
 * Resuelve errores de sincronización y parámetros.
 */
public class Main {
    public static void main(String[] args) {
        // Uso de try-with-resources para evitar fugas de memoria (Memory Leaks)
        try (Scanner sc = new Scanner(System.in)) {
            ReporteService reporte = new ReporteService();
            ArchivoService persistencia = new ArchivoService();
            boolean salir = false;

            while (!salir) {
                mostrarMenu();
                String opcion = sc.nextLine();

                switch (opcion) {
                    case "1":
                        ejecutarRegistro(sc, persistencia);
                        break;
                    case "2":
                        reporte.generarReporteVentas();
                        break;
                    case "3":
                        System.out.print("🛡️ Ingrese el Club o Institución a buscar: ");
                        reporte.buscarVentasPorClub(sc.nextLine());
                        break;
                    case "4":
                        System.out.println("SISTEMA: Cerrando sesión en Elag Core. ¡Éxito en sus labores!");
                        salir = true;
                        break;
                    default:
                        System.out.println("⚠️ Opción no válida. Intente de nuevo.");
                }
            }
        } catch (Exception e) {
            System.err.println("🚨 ERROR CRÍTICO DE SISTEMA: " + e.getMessage());
        }
    }

    private static void mostrarMenu() {
        System.out.println("\n========================================");
        System.out.println("   CASA DE MODAS ELAG - PANEL DE CONTROL ");
        System.out.println("========================================");
        System.out.println("1. 📝 Registrar Nuevo Pedido de Trabajo");
        System.out.println("2. 📊 Ver Reporte Histórico de Ventas");
        System.out.println("3. 🔍 Consultar Auditoría por Club");
        System.out.println("4. 🚪 Salir del Sistema");
        System.out.print("\nSeleccione una operación: ");
    }

    private static void ejecutarRegistro(Scanner sc, ArchivoService persistencia) {
        System.out.println("\n--- REGISTRO DE NUEVA ORDEN ---");
        System.out.print("👤 Nombre del Cliente: ");
        String cliente = sc.nextLine();
        System.out.print("🛡️ Institución/Club (Opcional): ");
        String club = sc.nextLine();

        // 1. Definición de productos base (Simulación)
        List<Uniforme> carrito = new ArrayList<>();
        Uniforme u = new Uniforme(1, "Uniforme Médico Premium", "assets/medico.jpg");
        u.agregarVariante(new Variante("Talla L", 85000));
        carrito.add(u);

        // 2. Cálculo de ID basado en tiempo real para evitar duplicados
        int id = (int) (System.currentTimeMillis() % 100000);

        // 3. CREACIÓN DEL OBJETO PEDIDO (Solución al error de 5 parámetros)
        // Se pasan exactamente: int, String, String, List, double
        Pedido nuevoPedido = new Pedido(id, cliente, club, carrito, 85000.0);

        // 4. PERSISTENCIA Y SALIDA (Solución a error de Logística)
        persistencia.guardarPedido(nuevoPedido);
        nuevoPedido.generarResumen();
        System.out.println("✅ Éxito: El pedido se ha guardado en pedidos_elag.txt (UTF-8)");
    }
}