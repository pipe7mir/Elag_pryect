package backend;

import java.time.LocalDate;
import java.time.DayOfWeek;
import java.time.format.DateTimeFormatter;

/**
 * Servicio de Ingeniería Logística - Casa de Modas Elag.
 * Calcula plazos de confección omitiendo fines de semana (Sábados y Domingos).
 */
public class LogisticaService {

    // Formato estándar de fecha para toda la aplicación Elag
    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    /**
     * Calcula la fecha de entrega sumando días hábiles a la fecha actual.
     * Este nombre de método es el que requiere Pedido.java para compilar.
     * @param diasAHabilitar Plazo de confección (ej: 15 días).
     * @return String con la fecha final formateada.
     */
    public String calcularFechaEntrega(int diasAHabilitar) {
        // Programación Defensiva: Asegurar que el plazo sea válido
        if (diasAHabilitar < 0) {
            diasAHabilitar = 0;
        }

        LocalDate fechaResultado = LocalDate.now();
        int diasContados = 0;

        // Algoritmo de desplazamiento temporal: omite Sábados y Domingos
        while (diasContados < diasAHabilitar) {
            fechaResultado = fechaResultado.plusDays(1);

            // Verificamos si es un día laboral (Lunes a Viernes)
            if (esDiaLaboral(fechaResultado)) {
                diasContados++;
            }
        }

        return fechaResultado.format(FORMATO_FECHA);
    }

    /**
     * Determina si una fecha es día de oficina (Lunes-Viernes).
     * @param fecha Objeto LocalDate a evaluar.
     * @return true si es laborable, false si es fin de semana.
     */
    private boolean esDiaLaboral(LocalDate fecha) {
        DayOfWeek dia = fecha.getDayOfWeek();
        // Retorna verdadero si NO es Sábado ni Domingo
        return !(dia == DayOfWeek.SATURDAY || dia == DayOfWeek.SUNDAY);
    }
}