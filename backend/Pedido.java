package backend;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

public class Pedido {
    private final int id;
    private final String nombreCliente;
    private final List<Uniforme> items;
    private final double total;
    private final LocalDate fechaEntrega;

    /**
     * Constructor optimizado.
     * Implementa la lógica de negocio automáticamente al crearse la orden.
     */
    public Pedido(int id, String nombreCliente, List<Uniforme> items, double total) {
        this.id = id;
        this.nombreCliente = nombreCliente;
        // Optimizacion: Protegemos la lista para que no sea modificable fuera de esta clase
        this.items = Collections.unmodifiableList(items);
        this.total = total;
        
        // Logica de negocio: Fuente unica de verdad
        LogisticaService logistica = new LogisticaService();
        this.fechaEntrega = logistica.obtenerFechaEntregaEstandar();
    }

    // --- GETTERS (Encapsulamiento profesional) ---
    public int getId() { return id; }
    public String getNombreCliente() { return nombreCliente; }
    public List<Uniforme> getItems() { return items; }
    public double getTotal() { return total; }
    public LocalDate getFechaEntrega() { return fechaEntrega; }

    /**
     * Genera un reporte formateado del pedido.
     * Optimizado para lectura en consola de ingeniería.
     */
    public void generarResumen() {
        DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        
        System.out.println("\n========================================");
        System.out.println("       FACTURA CASA DE MODAS ELAG       ");
        System.out.println("========================================");
        System.out.println("ORDEN ID:    " + id);
        System.out.println("CLIENTE:     " + nombreCliente);
        System.out.println("PRODUCTOS:   " + items.size() + " uniformes");
        System.out.println("TOTAL:       $" + String.format("%.2f", total));
        System.out.println("----------------------------------------");
        System.out.println("ENTREGA ESTIMADA: " + fechaEntrega.format(formatoFecha));
        System.out.println("(*Plazo de 15 días hábiles cumplido*)");
        System.out.println("========================================\n");
    }
}