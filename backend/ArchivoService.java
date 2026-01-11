package backend;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ArchivoService {
    private static final String NOMBRE_ARCHIVO = "pedidos_elag.txt";
    private static final DateTimeFormatter TIMESTAMP_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public void guardarPedido(Pedido pedido) {
        if (pedido == null) return;

        String timestamp = LocalDateTime.now().format(TIMESTAMP_FORMAT);

        // Uso de NIO y UTF-8 para garantizar la integridad de tildes y eñes
        try (BufferedWriter writer = Files.newBufferedWriter(
                Paths.get(NOMBRE_ARCHIVO), 
                StandardCharsets.UTF_8, 
                StandardOpenOption.CREATE, 
                StandardOpenOption.APPEND)) {

            StringBuilder sb = new StringBuilder();
            sb.append("[").append(timestamp).append("] ");
            sb.append("ID: ").append(pedido.getId()).append(" | ");
            sb.append("Cliente: ").append(pedido.getNombreCliente()).append(" | ");
            sb.append("Club/Inst: ").append(pedido.getNombreClub()).append(" | ");
            sb.append("Total: $").append(String.format("%.2f", pedido.getTotal())).append(" | ");
            sb.append("Entrega: ").append(pedido.getFechaEntrega());

            writer.write(sb.toString());
            writer.newLine();
            
            System.out.println("SISTEMA: Transacción exitosa guardada en disco.");
            
        } catch (IOException e) {
            System.err.println("CRÍTICO: Error de escritura en el registro de Elag: " + e.getMessage());
        }
    }
}