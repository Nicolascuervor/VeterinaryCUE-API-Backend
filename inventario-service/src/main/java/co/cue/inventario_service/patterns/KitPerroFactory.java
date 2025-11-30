package co.cue.inventario_service.patterns;

import co.cue.inventario_service.patterns.kitfactory.KitMascotaDTO;
import co.cue.inventario_service.patterns.kitfactory.KitProductoDTO;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class KitPerroFactory implements IKitMascotaFactory {

    // Retorna el tipo de mascota asociado a esta fábrica: "PERRO"
    @Override
    public String getTipoMascota() {
        return "PERRO";
    }

    // Crea un kit de bienvenida específico para perros con sus productos predeterminados
    @Override
    public KitMascotaDTO crearKitBienvenida() {
        return new KitMascotaDTO(
                "Kit de Bienvenida para Perro", // Nombre del kit
                0.15,  // 15% de descuento aplicado al kit
                List.of(
                        new KitProductoDTO("Juguete Hueso", 1), // Juguete incluido
                        new KitProductoDTO("Alimento Perro", 1),  // Alimento para perro
                        new KitProductoDTO("Cama Pequeña", 1)  // Cama especial para perros
                )
        );
    }
}
