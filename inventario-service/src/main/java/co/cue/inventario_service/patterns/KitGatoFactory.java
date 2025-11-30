package co.cue.inventario_service.patterns;

import co.cue.inventario_service.patterns.kitfactory.KitMascotaDTO;
import co.cue.inventario_service.patterns.kitfactory.KitProductoDTO;
import org.springframework.stereotype.Component;
import java.util.List;


@Component
public class KitGatoFactory implements IKitMascotaFactory {

    // Retorna el tipo de mascota que esta fábrica maneja: "GATO"
    @Override
    public String getTipoMascota() {
        return "GATO";
    }

    // Crea un kit de bienvenida específico para gatos con productos predeterminados
    @Override
    public KitMascotaDTO crearKitBienvenida() {
        return new KitMascotaDTO(
                "Kit de Bienvenida para Gato", // Nombre del kit
                0.10, // 10% de descuento
                List.of(
                        new KitProductoDTO("Juguete Raton", 1),
                        new KitProductoDTO("Alimento Gato", 1),
                        new KitProductoDTO("Cama Suave", 1)
                )
        );
    }
}
