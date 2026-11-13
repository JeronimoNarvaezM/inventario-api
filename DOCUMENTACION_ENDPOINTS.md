# Documentación de endpoints: Inventario API

**Proyecto:** Inventario y movimientos para microempresa
**Autores:** Narváez Montoya Jerónimo · Muñoz Cataño Daniel José
**URL base:** `http://localhost:8080`
**Formato:** todas las peticiones y respuestas usan JSON (`Content-Type: application/json`).

Valores permitidos:
- `rol`: `ADMINISTRADOR` o `AUXILIAR_BODEGA`
- `tipo` (movimiento): `ENTRADA` o `SALIDA`
---

## Estado del servicio

| Método | URL | Body | Respuestas |
|---|---|---|---|
| GET | `localhost:8080/api/estado` | — | 200 |
 
---

## Categorías

| Método | URL | Body | Respuestas |
|---|---|---|---|
| GET | `localhost:8080/api/categorias` | — | 200 |
| GET | `localhost:8080/api/categorias/{id}` | — | 200, 404 |
| POST | `localhost:8080/api/categorias` | `{"nombre": "Bebidas", "descripcion": "Gaseosas y jugos"}` | 201, 400, 409 |
| PUT | `localhost:8080/api/categorias/{id}` | `{"nombre": "Bebidas frías", "descripcion": "Actualizada"}` | 200, 400, 404, 409 |
| DELETE | `localhost:8080/api/categorias/{id}` | — | 204, 404, 409 (tiene productos asociados) |
 
---

## Productos

| Método | URL | Body | Respuestas |
|---|---|---|---|
| GET | `localhost:8080/api/productos` | — | 200 |
| GET | `localhost:8080/api/productos/{id}` | — | 200, 404 |
| POST | `localhost:8080/api/productos` | `{"nombre": "Gaseosa 400ml", "descripcion": "Botella plástica", "umbralAlerta": 10, "categoriaId": 1}` | 201, 400, 404 |
| PUT | `localhost:8080/api/productos/{id}` | `{"nombre": "Gaseosa 500ml", "descripcion": "Botella plástica", "umbralAlerta": 15, "categoriaId": 1}` | 200, 400, 404 |
| PATCH | `localhost:8080/api/productos/{id}/estado` | `{"activo": false}` | 200, 400, 404 |

Notas:
- El producto nace con `stockActual = 0` y `activo = true`. El stock solo cambia mediante movimientos.
- `umbralAlerta` debe ser cero o positivo (RN-04).
- No existe `DELETE` de productos: se inactivan con el `PATCH` (RN-05).
---

## Usuarios

| Método | URL | Body | Respuestas |
|---|---|---|---|
| GET | `localhost:8080/api/usuarios` | — | 200 |
| GET | `localhost:8080/api/usuarios/{id}` | — | 200, 404 |
| POST | `localhost:8080/api/usuarios` | `{"nombre": "Luis Bodega", "correo": "luis@mail.com", "rol": "AUXILIAR_BODEGA"}` | 201, 400, 409 |
 
---

## Movimientos

| Método | URL | Body | Respuestas |
|---|---|---|---|
| GET | `localhost:8080/api/movimientos?productoId=1` | — | 200, 400, 404 |
| POST | `localhost:8080/api/movimientos` | `{"tipo": "SALIDA", "cantidad": 15, "usuarioId": 2, "observacion": "Venta del día", "productoId": 1}` | 201, 400, 403, 404, 409 |

Notas:
- `GET` devuelve el kardex del producto, del movimiento más reciente al más antiguo.
- Solo un usuario activo con rol `AUXILIAR_BODEGA` puede registrar movimientos (403 en otro caso).
- `cantidad` debe ser positiva (RN-02). Una salida mayor al stock se rechaza con 409 (RN-01).
- Cada movimiento guarda fecha, usuario y observación (RN-06).
---

## Alertas de stock

| Método | URL | Body | Respuestas |
|---|---|---|---|
| GET | `localhost:8080/api/alertas` | — | 200 |
| GET | `localhost:8080/api/alertas?productoId=1` | — | 200, 404 |

Una alerta se genera cuando una salida deja el stock **por debajo** del umbral del producto.
 
---

## Formato de los errores

Todos los errores responden con la misma estructura:

```json
{
  "timestamp": "2026-11-12T17:45:03.512",
  "status": 409,
  "error": "Conflict",
  "mensaje": "Stock insuficiente para registrar la salida",
  "ruta": "/api/movimientos"
}
```

## Códigos HTTP

| Código | Significado |
|---|---|
| 200 | Consulta o actualización exitosa |
| 201 | Recurso creado |
| 204 | Eliminación exitosa (sin cuerpo) |
| 400 | Datos inválidos o faltantes |
| 403 | El rol del usuario no permite la operación |
| 404 | El recurso no existe |
| 405 | Verbo no permitido en esa ruta (por ejemplo, borrar un producto) |
| 409 | Conflicto con el estado actual (stock insuficiente, duplicado, producto inactivo, categoría con productos) |

## Colección de pruebas

El repositorio incluye `postman/inventario-api.postman_collection.json`, con pruebas automáticas de todos los endpoints y reglas de negocio.