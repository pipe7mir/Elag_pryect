package backend; // Define que pertenece a tu carpeta backend

import java.time.LocalDate;
import java.time.DayOfWeek;

/**
 * Servicio encargado de la lógica de entrega de la Casa de Modas Elag.
 * Calcula el plazo de confección respetando solo días laborales.
 */
public class LogisticaService {

    // Plazo estándar definido por la Casa de Modas
    private static final int PLAZO_CONFECCION_ESTANDAR = 15;

    /**
     * Calcula la fecha de entrega sumando 15 días hábiles a partir de hoy.
     * @return LocalDate con la fecha final de entrega.
     */
    public LocalDate obtenerFechaEntregaEstandar() {
        return calcularFechaHabil(LocalDate.now(), PLAZO_CONFECCION_ESTANDAR);
    }

    /**
     * Algoritmo principal para saltar fines de semana.
     */
    private LocalDate calcularFechaHabil(LocalDate fechaInicio, int diasAHabilitar) {
        LocalDate fechaResultado = fechaInicio;
        int diasContados = 0;

        while (diasContados < diasAHabilitar) {
            // Avanzamos un día a la vez
            fechaResultado = fechaResultado.plusDays(1);
            
            // Verificamos si es un día laboral (Lunes a Viernes)
            if (esDiaLaboral(fechaResultado)) {
                diasContados++;
            }
        }
        return fechaResultado;
    }

    /**
     * Verifica si una fecha cae en día de oficina.
     */
    private boolean esDiaLaboral(LocalDate fecha) {
        DayOfWeek dia = fecha.getDayOfWeek();
        // Retorna verdadero si NO es Sábado ni Domingo
        return !(dia == DayOfWeek.SATURDAY || dia == DayOfWeek.SUNDAY);
    }
}