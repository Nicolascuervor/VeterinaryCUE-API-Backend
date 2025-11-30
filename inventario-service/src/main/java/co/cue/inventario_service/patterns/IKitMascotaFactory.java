package co.cue.inventario_service.patterns;

import co.cue.inventario_service.patterns.kitfactory.KitMascotaDTO;

public interface IKitMascotaFactory {

    // Retorna el tipo de mascota que maneja esta fábrica (ej: "PERRO", "GATO")
    String getTipoMascota();

    // Crea y devuelve un kit de bienvenida para el tipo de mascota definido
    KitMascotaDTO crearKitBienvenida();

 }
