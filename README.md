## 🛒 Sistema de Gestión de Carrito de Compras

Sistema desarrollado en Java que simula el flujo de un carrito de compras, aplicando principios de Programación Orientada a Objetos (POO) y Arquitectura Hexagonal, con análisis previo mediante diagramas UML.  

El resultado es una **API RESTful** que permite gestionar carritos de compra, productos, usuarios y pedidos, incluyendo funcionalidades para:
- Agregar, eliminar y modificar productos en el carrito.
- Finalizar o cancelar compras.
- Consultar usuarios, pedidos y sus detalles asociados.

Este proyecto es un rework mejorado de una versión anterior desarrollada también en Java, incorporando nuevas funcionalidades, una estructura más sólida y mejores prácticas de arquitectura:

[👉 Ver proyecto anterior](https://github.com/Espectro0/shoppingCart)

---

## Diagrama UML
_El archivo svg se encuentra en la carpeta `src/main/resources`_

![Diagrama UML](./src/main/resources/Diagrama.svg)

---

## ***Ejemplo de uso: (Postman / JSON)***

***[Documentación de Postman](https://documenter.getpostman.com/view/49417793/2sB3WvMJSS)*** | EndPoints principalmente para el CRUD de productos, usuarios y pedidos. 

| Acción | Endpoint                                                                       | Método | Código Éxito | Códigos Error | Descripción |
|--------|--------------------------------------------------------------------------------|---------|---------------|----------------|--------------|
| Lista todos los productos en el catálogo | `/api/v1/products?category={category}?minPrice={minValue}?maxPrice={maxValue}` | GET | 200 OK | 500 Internal Server Error | Devuelve lista |
| Obtiene la información de un producto por su ID | `/api/v1/products/{productId}`                                                 | GET | 200 OK | 400 Bad Request, 404 Not Found, 500 Internal Server Error | Devuelve un único objeto (producto) |
| Crear un producto | `/api/v1/products`                                                             | POST | 201 Created | 409 Conflict, 500 Internal Server Error | **BODY:** { id, name, category, price, stock } |
| Eliminar un producto | `/api/v1/products/{productId}`                                                 | DELETE | 204 No Content | 404 Not Found, 500 Internal Server Error | No devuelve cuerpo |
| Actualizar el stock de un producto | `/api/v1/products/{productId}?quantity={stock}`                                | PUT | 200 OK | 400 Bad Request, 404 Not Found, 500 Internal Server Error | **BODY:** { stock } |
| Listar todos los usuarios | `/api/v1/users`                                                                | GET | 200 OK | 500 Internal Server Error | Devuelve lista |
| Obtener la información de un usuario por ID | `/api/v1/users/{userId}`                                                       | GET | 200 OK | 404 Not Found, 500 Internal Server Error | Devuelve un único objeto (usuario) |
| Crear un usuario | `/api/v1/users`                                                                | POST | 201 Created | 400 Bad Request, 409 Conflict, 500 Internal Server Error | **BODY:** { id, name, email } |
| Actualizar usuario | `/api/v1/users/{userId}`                                                       | PUT | 200 OK / 204 No Content | 404 Not Found, 400 Bad Request, 500 Internal Server Error | **BODY:** { name, email } |
| Eliminar usuario | `/api/v1/users/{userId}`                                                       | DELETE | 204 No Content | 404 Not Found, 500 Internal Server Error | No devuelve cuerpo |
| Crear un carrito | `/api/v1/users/{userId}/carts`                                                 | POST | 201 Created | 500 Internal Server Error | Sin cuerpo |
| Listar todos los carritos del usuario | `/api/v1/users/{userId}/carts?status={abierto/cerrado}`                        | GET | 200 OK | 500 Internal Server Error | Devuelve lista |
| Ver un carrito en específico | `/api/v1/users/{userId}/carts/{cartId}`                                        | GET | 200 OK | 403 Forbidden, 404 Not Found, 500 Internal Server Error | Devuelve un único objeto (carrito) |
| Agregar producto al carrito | `/api/v1/users/{userId}/carts/{cartId}/item/{productId}?quantity={value}`      | POST | 201 Created | 403 Forbidden, 404 Not Found, 500 Internal Server Error | **BODY:** { quantity } |
| Eliminar producto del carrito | `/api/v1/users/{userId}/carts/{cartId}/item/{productId}`                       | DELETE | 204 No Content | 403 Forbidden, 404 Not Found, 500 Internal Server Error | No devuelve cuerpo |
| Modificar producto del carrito | `/api/v1/users/{userId}/carts/{cartId}/item/{productId}`                       | PUT | 200 OK / 204 No Content | 403 Forbidden, 404 Not Found, 500 Internal Server Error | **BODY:** { quantity } |
| Checkout del carrito | `/api/v1/users/{userId}/carts/{cartId}/checkout`                               | POST | 200 OK | 403 Forbidden, 404 Not Found, 500 Internal Server Error | Devuelve un único objeto (carrito) |
| Cancelar carrito | `/api/v1/users/{userId}/carts/{cartId}`                                        | DELETE | 204 No Content | 403 Forbidden, 404 Not Found, 500 Internal Server Error | No devuelve cuerpo |

---

## Configuración del sistema

### Requisitos previos:

- Java 21 o superior
- Maven 3.9+
- MySQL 8.0+
- Postman Client o cualquier cliente REST
- IDE Recomendado: IntelliJ IDEA / Eclipse / VS Code

### Configuración de la base de datos:
Las tablas y la base de datos fueron creadas utilizando MySQL Workbench utilizando el puerto por defecto `3306`:

````mysql
CREATE DATABASE shoppingcartapi;
USE shoppingcartapi;

CREATE TABLE users (
    id VARCHAR(36) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL
);

CREATE TABLE products (
    id VARCHAR(36) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    category VARCHAR(255),
    description TEXT,
    price DOUBLE NOT NULL,
    stock INT NOT NULL
);

CREATE TABLE orders (
    id VARCHAR(36) PRIMARY KEY,
    user_id VARCHAR(36) NOT NULL,
    total DOUBLE,
    discount DOUBLE,
    date VARCHAR(255),
    is_checked_out BOOLEAN,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE order_items (
    id VARCHAR(36) PRIMARY KEY,
    order_id VARCHAR(36) NOT NULL,
    product_id VARCHAR(36) NOT NULL,
    quantity INT NOT NULL,
    subtotal DOUBLE,
    FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES products(id)
);
````

El servidor se levantará por defecto en el puerto `8080`.

_**DISCLAIMER: Este código puede contener errores, ya que es un proyecto de aprendizaje para la universidad.**_

---

## Tecnologías
- ***Java 21:*** Lenguaje de programación principal
- ***Spring Boot:*** Framework para construcción de APIs RESTful
- ***Maven:*** Herramienta de construcción y gestión de dependencias
- ***MySQL:*** Base de datos relacional
- ***POO:*** Aplicación de encapsulamiento, herencia y polimorfismo
- ***Arquitectura Hexagonal:*** Separación de capas y responsabilidades
- ***UML:*** Modelado previo de entidades y flujos
