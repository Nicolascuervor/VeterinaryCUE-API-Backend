package co.cue.inventario_service.patterns;

import co.cue.inventario_service.patterns.kitfactory.KitMascotaDTO;

public interface IKitMascotaFactory {
    String getTipoMascota();
    KitMascotaDTO crearKitBienvenida();

 }
