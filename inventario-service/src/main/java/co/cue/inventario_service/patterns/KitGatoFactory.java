package co.cue.inventario_service.patterns;

import co.cue.inventario_service.patterns.kitfactory.KitMascotaDTO;
import co.cue.inventario_service.patterns.kitfactory.KitProductoDTO;
import org.springframework.stereotype.Component;
import java.util.List;

// (Concrete Factory 1)
@Component // (Colega Senior): La hacemos un Bean de Spring
public class KitGatoFactory implements IKitMascotaFactory {

    @Override
    public String getTipoMascota() {
        return "GATO";
    }

    @Override
    public KitMascotaDTO crearKitBienvenida() {
        return new KitMascotaDTO(
                "Kit de Bienvenida para Gato",
                0.10, // 10% de descuento
                List.of(
                        new KitProductoDTO("Juguete Raton", 1),
                        new KitProductoDTO("Alimento Gato", 1),
                        new KitProductoDTO("Cama Suave", 1)
                )
        );
    }
}
