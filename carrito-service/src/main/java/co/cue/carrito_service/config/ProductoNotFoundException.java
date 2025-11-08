package co.cue.carrito_service.config;

// Excepción que lanzaremos si inventario-service devuelve 404
public class ProductoNotFoundException extends RuntimeException {
    public ProductoNotFoundException(String message) {
        super(message);
    }
}
