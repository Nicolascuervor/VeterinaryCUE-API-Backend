package co.cue.inventario_service.patterns;

import co.cue.inventario_service.patterns.kitfactory.KitMascotaDTO;
import co.cue.inventario_service.patterns.kitfactory.KitProductoDTO;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class KitPerroFactory implements IKitMascotaFactory {

    @Override
    public String getTipoMascota() {
        return "PERRO";
    }

    @Override
    public KitMascotaDTO crearKitBienvenida() {
        return new KitMascotaDTO(
                "Kit de Bienvenida para Perro",
                0.15,
                List.of(
                        new KitProductoDTO("Juguete Hueso", 1),
                        new KitProductoDTO("Alimento Perro", 1),
                        new KitProductoDTO("Cama Pequeña", 1)
                )
        );
    }
}
