# Simulación de Pagos - Documentación

## 📋 Descripción

Se ha implementado un sistema completo de simulación de pagos que permite desarrollar y probar el flujo de checkout sin necesidad de configurar Stripe o realizar transacciones reales.

## ✅ Características

- **Simulación completa**: No requiere configuración de Stripe
- **Mismo flujo**: Mantiene la misma interfaz que Stripe real
- **Fácil activación**: Se activa mediante una variable de entorno
- **Endpoints de simulación**: Permite completar pagos manualmente para testing

## 🔧 Configuración

### Activar Modo Simulación

En `application.properties` o variables de entorno:

```properties
payment.simulation.enabled=true
```

O mediante variable de entorno:

```bash
PAYMENT_SIMULATION_ENABLED=true
```

### Desactivar (Usar Stripe Real)

```properties
payment.simulation.enabled=false
```

O simplemente no configurar la variable (por defecto es `false`).

## 🚀 Uso

### 1. Iniciar Checkout (Igual que antes)

El frontend llama al mismo endpoint:

```http
POST /api/pedidos/checkout
Headers:
  X-Usuario-Id: 123 (opcional)
  X-Session-Id: session-abc (opcional)
Body: (opcional para invitados)
  {
    "clienteNombre": "Juan",
    "clienteEmail": "juan@example.com"
  }
```

**Respuesta:**
```json
{
  "pedidoId": 1,
  "clientSecret": "pi_simulated_1_secret_simulated_abc123..."
}
```

### 2. Simular Pago Exitoso

Después de obtener el `clientSecret`, el frontend puede simular la confirmación del pago llamando a:

#### Opción A: Por Pedido ID

```http
POST /api/pedidos/simulate/payment/{pedidoId}
```

**Ejemplo:**
```http
POST /api/pedidos/simulate/payment/1
```

**Respuesta:**
```json
{
  "success": true,
  "message": "Pago simulado completado exitosamente. El pedido ha sido procesado."
}
```

#### Opción B: Por PaymentIntent ID

```http
POST /api/pedidos/simulate/payment/confirm?paymentIntentId=pi_simulated_1_secret_simulated_abc123
```

**Respuesta:**
```json
{
  "success": true,
  "message": "Pago simulado confirmado exitosamente."
}
```

### 3. Flujo Completo

1. **Frontend llama a `/api/pedidos/checkout`**
   - Backend crea el pedido
   - Backend genera un `clientSecret` simulado
   - Retorna `{ pedidoId, clientSecret }`

2. **Frontend simula la confirmación del pago**
   - Opción 1: Llamar directamente a `/api/pedidos/simulate/payment/{pedidoId}`
   - Opción 2: Usar el `clientSecret` y llamar a `/api/pedidos/simulate/payment/confirm?paymentIntentId={clientSecret}`

3. **Backend procesa el pago simulado**
   - Descuenta stock en inventario
   - Actualiza estado del pedido a `COMPLETADO`
   - Limpia el carrito
   - Envía evento a Kafka

## 📝 Formato del ClientSecret Simulado

El `clientSecret` generado tiene el formato:

```
pi_simulated_{pedidoId}_secret_simulated_{uuid}
```

**Ejemplo:**
```
pi_simulated_1_secret_simulated_a1b2c3d4e5f6g7h8i9j0k1l2m3n4o5p6
```

Esto permite:
- Identificar que es una simulación (prefijo `pi_simulated_`)
- Extraer el `pedidoId` fácilmente
- Mantener un formato similar a Stripe para compatibilidad

## 🔄 Integración con Frontend

### Ejemplo de Código Frontend

```javascript
async function procesarPagoSimulado(pedidoId) {
  try {
    // 1. Iniciar checkout (igual que antes)
    const checkoutResponse = await fetch('/api/pedidos/checkout', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'X-Usuario-Id': usuarioId || '',
      }
    });

    const { pedidoId, clientSecret } = await checkoutResponse.json();

    // 2. Simular confirmación del pago
    const simulateResponse = await fetch(`/api/pedidos/simulate/payment/${pedidoId}`, {
      method: 'POST'
    });

    const result = await simulateResponse.json();

    if (result.success) {
      console.log('✅ Pago simulado exitoso');
      // El pedido ya está completado, stock descontado, carrito limpiado
    } else {
      console.error('❌ Error:', result.message);
    }

  } catch (error) {
    console.error('Error al procesar pago simulado:', error);
  }
}
```

## ⚠️ Consideraciones Importantes

1. **Solo para Desarrollo/Testing**: 
   - Los endpoints de simulación están públicos (sin autenticación)
   - No deben usarse en producción

2. **Formato del PaymentIntent**:
   - El formato simulado es diferente al de Stripe real
   - El frontend puede detectar si es simulación verificando si empieza con `pi_simulated_`

3. **Webhooks**:
   - En modo simulación, no se reciben webhooks de Stripe
   - El pago se completa manualmente llamando a los endpoints de simulación

4. **Stock y Carrito**:
   - El stock se descuenta automáticamente al simular el pago
   - El carrito se limpia automáticamente
   - Se envía evento a Kafka como en el flujo real

## 🔍 Verificación

### Verificar que la simulación está activa

Al iniciar el servicio, deberías ver en los logs:

```
🔧 MODO SIMULACIÓN ACTIVADO: Creando SimulatedPaymentGateway
⚠️  Los pagos serán simulados. No se realizarán transacciones reales.
✅ Configuración: Usando SimulatedPaymentGateway
⚠️  MODO SIMULACIÓN: Los pagos no serán procesados por Stripe
```

### Verificar que Stripe real está activo

```
✅ Configuración: Usando StripeAdapterService
```

## 🐛 Troubleshooting

### El endpoint de simulación retorna 403

- Verifica que `payment.simulation.enabled=true` esté configurado
- Reinicia el servicio después de cambiar la configuración

### El clientSecret no tiene el formato esperado

- Verifica que la simulación esté activada
- El formato debe ser: `pi_simulated_{pedidoId}_secret_simulated_{uuid}`

### El pago no se completa

- Verifica que el `pedidoId` existe
- Verifica que el pedido no esté ya completado
- Revisa los logs del servicio para ver errores

## 📚 Archivos Relacionados

- `SimulatedPaymentGateway.java`: Implementación del adaptador simulado
- `PaymentGatewayConfig.java`: Configuración para seleccionar el adaptador
- `SimulatedPaymentController.java`: Endpoints para simular pagos
- `application.properties`: Configuración de `payment.simulation.enabled`

## ✅ Ventajas de la Simulación

1. **Sin dependencias externas**: No requiere Stripe configurado
2. **Desarrollo rápido**: Permite probar el flujo completo sin configuración
3. **Testing fácil**: Permite automatizar pruebas de pago
4. **Mismo código**: El frontend puede usar el mismo código (solo cambia la URL de confirmación)

