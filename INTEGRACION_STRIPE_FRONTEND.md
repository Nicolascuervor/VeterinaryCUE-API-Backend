# Integración con Stripe - Guía para el Frontend

## 📋 Resumen de la Integración

El backend ya tiene implementada la integración completa con Stripe para procesar pagos. Esta guía documenta todo lo que el frontend necesita para procesar una venta de productos.

---

## 🔄 Flujo Completo de Pago

### 1. **Iniciar Checkout** (Crear Pedido)
### 2. **Obtener Client Secret** (del backend)
### 3. **Confirmar Pago con Stripe** (en el frontend)
### 4. **Webhook de Stripe** (procesado automáticamente por el backend)

---

## 📡 Endpoints Disponibles

### 1. **POST `/api/pedidos/checkout`** - Iniciar Proceso de Checkout

**Descripción:** Crea un pedido en el sistema, valida stock, y genera un PaymentIntent en Stripe.

**Autenticación:** No requiere autenticación (público)

**Headers:**
```
X-Usuario-Id: {userId}  (opcional, si el usuario está autenticado)
X-Session-Id: {sessionId}  (opcional, para carritos de invitados)
Content-Type: application/json
```

**Body (opcional, solo para usuarios invitados):**
```json
{
  "clienteNombre": "Juan Pérez",
  "clienteEmail": "juan@example.com"
}
```

**Respuesta Exitosa (200 OK):**
```json
{
  "pedidoId": 123,
  "clientSecret": "pi_xxxxx_secret_xxxxx"
}
```

**Errores Posibles:**
- **400 Bad Request:**
  - `"No se puede procesar un pedido con un carrito vacío."` - El carrito está vacío
  - `"Stock insuficiente para {producto}. Solicitados: X, Disponibles: Y"` - No hay suficiente stock
  - `"El producto {id} no está disponible."` - Producto no disponible para venta
  - `"Datos del cliente (usuarioId o guestDTO) son requeridos."` - Faltan datos del cliente

**Notas:**
- Si el usuario está autenticado, envía `X-Usuario-Id` y omite el body
- Si es usuario invitado, envía `X-Session-Id` y el body con datos del cliente
- El backend valida automáticamente el stock antes de crear el pedido
- El pedido se crea con estado `PENDIENTE`

---

### 2. **POST `/api/pedidos/stripe/webhook`** - Webhook de Stripe

**Descripción:** Endpoint que Stripe llama automáticamente cuando un pago se completa. **NO debe ser llamado desde el frontend.**

**Autenticación:** Público (Stripe valida con firma)

**Nota:** Este endpoint es manejado completamente por el backend. El frontend no necesita interactuar con él.

---

## 💳 Integración con Stripe en el Frontend

### Paso 1: Instalar Stripe.js

```bash
npm install @stripe/stripe-js
```

### Paso 2: Código de Ejemplo Completo

```javascript
import { loadStripe } from '@stripe/stripe-js';

// Inicializar Stripe con tu clave pública
const stripe = await loadStripe('pk_test_...'); // Tu Stripe Publishable Key

async function procesarPago(usuarioId, sessionId, guestData) {
  try {
    // 1. Iniciar checkout en el backend
    const checkoutResponse = await fetch('/api/pedidos/checkout', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'X-Usuario-Id': usuarioId || '',  // Si está autenticado
        'X-Session-Id': sessionId || '',  // Si es invitado
      },
      body: usuarioId ? undefined : JSON.stringify(guestData)  // Solo si es invitado
    });

    if (!checkoutResponse.ok) {
      const errorMessage = await checkoutResponse.text();
      throw new Error(errorMessage);
    }

    const { pedidoId, clientSecret } = await checkoutResponse.json();

    // 2. Confirmar el pago con Stripe
    const { error, paymentIntent } = await stripe.confirmCardPayment(clientSecret, {
      payment_method: {
        card: cardElement,  // Elemento de tarjeta de Stripe
        billing_details: {
          name: guestData?.clienteNombre || usuarioNombre,
          email: guestData?.clienteEmail || usuarioEmail,
        }
      }
    });

    if (error) {
      // El pago falló
      console.error('Error en el pago:', error.message);
      throw new Error(error.message);
    }

    if (paymentIntent.status === 'succeeded') {
      // El pago fue exitoso
      // El backend procesará automáticamente el webhook de Stripe
      // y actualizará el pedido, descontará stock, limpiará el carrito, etc.
      
      return {
        success: true,
        pedidoId: pedidoId,
        paymentIntentId: paymentIntent.id
      };
    }

  } catch (error) {
    console.error('Error al procesar el pago:', error);
    throw error;
  }
}
```

### Paso 3: Manejo de Estados del Pago

```javascript
// Estados posibles del PaymentIntent:
// - requires_payment_method: Necesita método de pago
// - requires_confirmation: Necesita confirmación
// - requires_action: Requiere acción adicional (3D Secure, etc.)
// - processing: Procesando
// - succeeded: Exitoso ✅
// - requires_capture: Requiere captura
// - canceled: Cancelado

if (paymentIntent.status === 'requires_action') {
  // El pago requiere autenticación adicional (3D Secure)
  // Stripe manejará esto automáticamente
  const { error: actionError } = await stripe.handleCardAction(clientSecret);
  
  if (actionError) {
    throw new Error(actionError.message);
  }
  
  // Reintentar confirmación después de la acción
  const { error, paymentIntent: retryPaymentIntent } = 
    await stripe.confirmCardPayment(clientSecret);
  
  if (retryPaymentIntent.status === 'succeeded') {
    return { success: true, pedidoId };
  }
}
```

---

## 🔐 Variables de Entorno Necesarias

El backend requiere estas variables en `application.properties`:

```properties
stripe.api.secret-key=sk_test_...  # Clave secreta de Stripe
stripe.webhook.secret=whsec_...    # Secreto del webhook de Stripe
```

**Frontend necesita:**
- `STRIPE_PUBLISHABLE_KEY`: Clave pública de Stripe (pk_test_... o pk_live_...)

---

## 📦 Datos que el Backend Procesa Automáticamente

Cuando Stripe confirma el pago exitosamente, el backend automáticamente:

1. ✅ **Recibe el webhook** de Stripe (`payment_intent.succeeded`)
2. ✅ **Valida la firma** del webhook para seguridad
3. ✅ **Busca el pedido** asociado al PaymentIntent
4. ✅ **Descuenta el stock** de todos los productos en el inventario
5. ✅ **Actualiza el estado** del pedido a `COMPLETADO`
6. ✅ **Limpia el carrito** del usuario
7. ✅ **Envía evento a Kafka** para notificar a otros servicios (facturación, notificaciones, etc.)

**El frontend NO necesita hacer nada adicional después de confirmar el pago con Stripe.**

---

## ⚠️ Manejo de Errores

### Errores del Backend (Checkout)

```javascript
try {
  const response = await fetch('/api/pedidos/checkout', {...});
  
  if (!response.ok) {
    const errorMessage = await response.text();
    
    if (response.status === 400) {
      // Error de validación (carrito vacío, stock insuficiente, etc.)
      console.error('Error de validación:', errorMessage);
      // Mostrar mensaje al usuario
    } else {
      // Error del servidor
      console.error('Error del servidor:', errorMessage);
    }
  }
} catch (error) {
  console.error('Error de red:', error);
}
```

### Errores de Stripe

```javascript
const { error } = await stripe.confirmCardPayment(clientSecret, {...});

if (error) {
  switch (error.type) {
    case 'card_error':
      // Error de la tarjeta (rechazada, fondos insuficientes, etc.)
      console.error('Error de tarjeta:', error.message);
      break;
    case 'validation_error':
      // Error de validación (datos incorrectos)
      console.error('Error de validación:', error.message);
      break;
    default:
      // Otro error
      console.error('Error desconocido:', error.message);
  }
}
```

---

## 🧪 Flujo de Prueba Completo

### 1. Usuario Autenticado

```javascript
// Usuario con ID 123 tiene productos en su carrito
const resultado = await procesarPago(123, null, null);
// ✅ Pedido creado, pago procesado
```

### 2. Usuario Invitado

```javascript
// Usuario invitado con sessionId "abc123"
const guestData = {
  clienteNombre: "Juan Pérez",
  clienteEmail: "juan@example.com"
};

const resultado = await procesarPago(null, "abc123", guestData);
// ✅ Pedido creado, pago procesado
```

### 3. Tarjeta de Prueba de Stripe

Para testing, usa estas tarjetas de prueba:
- **Éxito:** `4242 4242 4242 4242`
- **Requiere autenticación:** `4000 0025 0000 3155`
- **Rechazada:** `4000 0000 0000 0002`

Fecha de expiración: cualquier fecha futura  
CVC: cualquier 3 dígitos

---

## 📝 Checklist para el Frontend

- [ ] Instalar `@stripe/stripe-js`
- [ ] Configurar Stripe Publishable Key
- [ ] Implementar formulario de pago con Stripe Elements
- [ ] Llamar a `/api/pedidos/checkout` antes de procesar el pago
- [ ] Usar el `clientSecret` recibido para confirmar el pago
- [ ] Manejar errores de validación (stock, carrito vacío)
- [ ] Manejar errores de Stripe (tarjeta rechazada, etc.)
- [ ] Mostrar mensaje de éxito después de pago exitoso
- [ ] Redirigir a página de confirmación después del pago

---

## 🔍 Verificación del Estado del Pedido (Opcional)

Si necesitas verificar el estado del pedido después del pago, puedes consultar:

```javascript
// GET /api/pedidos/{pedidoId}
// Requiere autenticación

const response = await fetch(`/api/pedidos/${pedidoId}`, {
  headers: {
    'Authorization': `Bearer ${token}`
  }
});

const pedido = await response.json();
// pedido.estado puede ser: PENDIENTE, COMPLETADO, CANCELADO
```

**Nota:** El estado se actualiza automáticamente cuando Stripe confirma el pago (vía webhook).

---

## ⚡ Notas Importantes

1. **Moneda:** El backend usa `COP` (Pesos Colombianos). Los montos se convierten a centavos automáticamente.

2. **Idempotencia:** El backend maneja eventos duplicados de Stripe. Si el mismo webhook llega dos veces, solo procesa el pedido una vez.

3. **Stock:** El stock se valida al crear el pedido y se descuenta solo cuando el pago es exitoso.

4. **Carrito:** El carrito se limpia automáticamente después de un pago exitoso.

5. **Webhook:** El webhook de Stripe debe estar configurado en el dashboard de Stripe apuntando a:
   ```
   https://api.veterinariacue.com/api/pedidos/stripe/webhook
   ```

---

## 🐛 Troubleshooting

### Error: "No se puede procesar un pedido con un carrito vacío"
- **Causa:** El carrito no tiene productos
- **Solución:** Verificar que el carrito tenga items antes de iniciar checkout

### Error: "Stock insuficiente"
- **Causa:** El stock cambió entre agregar al carrito y procesar el pago
- **Solución:** Mostrar mensaje al usuario y permitir actualizar el carrito

### Error: "Error al contactar la pasarela de pago"
- **Causa:** Problema de conexión con Stripe o clave API incorrecta
- **Solución:** Verificar configuración de Stripe en el backend

### El pago se procesa pero el pedido no se completa
- **Causa:** El webhook de Stripe no está configurado o no puede alcanzar el backend
- **Solución:** Verificar configuración del webhook en Stripe Dashboard

---

## 📚 Recursos Adicionales

- [Documentación de Stripe.js](https://stripe.com/docs/js)
- [Stripe Elements](https://stripe.com/docs/stripe-js/react)
- [Payment Intents API](https://stripe.com/docs/payments/payment-intents)

---

**Última actualización:** Diciembre 2024

