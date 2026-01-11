package backend;

import java.util.ArrayList;
import java.util.List;

public class PedidoController {

    /**
     * Recibe pedidos desde el frontend y los sincroniza con el backend.
     * Corregido: Ahora envía 5 argumentos al constructor de Pedido.
     */
    public void recibirPedidoDesdeWeb(String jsonRecibido) {
        System.out.println("SISTEMA: Sincronizando orden desde plataforma web...");
        
        // Simulación de extracción de datos (Parsing).
        int idGenerado = (int)(System.currentTimeMillis() % 1000);
        String cliente = "Cliente Web";
        String institucion = "Particular"; 
        double total = 150000.0;
        
        List<Uniforme> items = new ArrayList<>();
        // Producto base para asegurar la integridad de la lista.
        items.add(new Uniforme(idGenerado, "Pedido Web Pendiente", "assets/web.jpg"));

        // SOLUCIÓN AL ERROR: Se incluyen los 5 parámetros requeridos.
        Pedido pedidoWeb = new Pedido(idGenerado, cliente, institucion, items, total);
        
        // Persistencia inmediata con codificación UTF-8.
        ArchivoService persistencia = new ArchivoService();
        persistencia.guardarPedido(pedidoWeb);

        pedidoWeb.generarResumen();
    }
}