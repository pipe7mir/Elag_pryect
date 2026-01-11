package backend;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase encargada de recibir las peticiones de la web.
 * Actúa como el punto de entrada para los nuevos pedidos de la Casa de Modas Elag.
 */
public class PedidoController {

    /**
     * Recibe pedidos desde el frontend y los unifica con el sistema de archivos.
     * Corregido: Ahora envía 5 argumentos al constructor de Pedido para evitar fallos de build.
     */
    public void recibirPedidoDesdeWeb(String jsonRecibido) {
        System.out.println("LOG: Sincronizando orden desde plataforma web de Elag...");
        
        // Simulación de extracción de datos (Parsing).
        int idGenerado = (int)(System.currentTimeMillis() % 1000);
        String cliente = "Cliente Web";
        String institucion = "Particular"; // Argumento corregido para cumplir con el constructor
        double total = 150000.0;
        
        // 1. Preparación de la lista de productos
        List<Uniforme> items = new ArrayList<>();
        // Se añade producto base para asegurar integridad
        items.add(new Uniforme(idGenerado, "Pedido Web Pendiente", "assets/web.jpg"));

        // 2. CREACIÓN DEL PEDIDO (SOLUCIÓN: 5 parámetros)
        // Se pasan: int, String, String, List, double
        Pedido pedidoWeb = new Pedido(idGenerado, cliente, institucion, items, total);
        
        // 3. PERSISTENCIA EN DISCO (NIO + UTF-8)
        ArchivoService persistencia = new ArchivoService();
        persistencia.guardarPedido(pedidoWeb);

        System.out.println("ÉXITO: Pedido web guardado y fecha de entrega calculada.");
        pedidoWeb.generarResumen();
    }
}