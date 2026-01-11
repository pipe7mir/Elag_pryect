package backend;

import java.util.List;

/**
 * Modelo de Datos Unificado v3.8.
 * Resuelve conflictos de argumentos y automatiza la logística.
 */
public class Pedido {
    private int id;
    private String nombreCliente;
    private String nombreClub;
    private double total;
    private String fechaEntrega;
    private List<Uniforme> productos;

    /**
     * Constructor de 5 parámetros.
     * La fecha de entrega se calcula internamente, no se recibe por parámetro.
     */
    public Pedido(int id, String nombreCliente, String nombreClub, List<Uniforme> productos, double total) {
        this.id = id;
        this.nombreCliente = nombreCliente;
        // Lógica defensiva: Asigna "Particular" si el club viene vacío.
        this.nombreClub = (nombreClub == null || nombreClub.isEmpty()) ? "Particular" : nombreClub;
        this.productos = productos;
        this.total = total;

        // EJECUCIÓN LOGÍSTICA: Se conecta con LogisticaService para obtener la fecha real.
        LogisticaService logistica = new LogisticaService();
        this.fechaEntrega = logistica.calcularFechaEntrega(15);
    }

    // Getters necesarios para el ArchivoService (NIO/UTF-8).
    public int getId() { return id; }
    public String getNombreCliente() { return nombreCliente; }
    public String getNombreClub() { return nombreClub; }
    public double getTotal() { return total; }
    public String getFechaEntrega() { return fechaEntrega; }

    public void generarResumen() {
        System.out.println("\n----------------------------------------");
        System.out.println("📦 PEDIDO REGISTRADO: #" + id);
        System.out.println("👤 CLIENTE: " + nombreCliente);
        System.out.println("🛡️ ENTIDAD: " + nombreClub);
        System.out.println("💰 TOTAL: $" + String.format("%.2f", total));
        System.out.println("📅 ENTREGA ESTIMADA: " + fechaEntrega);
        System.out.println("----------------------------------------\n");
    }
}