# ✈️ Zephyr - Sistema de Gestión Aeroportuaria

Sistema de escritorio para la gestión interna de operaciones de embarque y desembarque, desarrollado como proyecto para el curso de Programación Orientada a Objetos (POO).

---

## 📖 Descripción del Proyecto

**Zephyr** es una aplicación de escritorio (JavaFX) diseñada para el personal interno de un aeropuerto (agentes de puerta y supervisores de operaciones). El sistema permite administrar, controlar y monitorear todo el ciclo de vida de un vuelo, desde su programación hasta la finalización del desembarque, con un enfoque principal en la gestión de pasajeros y el embarque.

El proyecto está construido en **Java puro (Vanilla)**, sin el uso de frameworks como Spring, para demostrar un dominio profundo de los principios de POO, la arquitectura de software y la conectividad a bases de datos (JDBC).

## ✨ Características Principales

El sistema está diseñado para manejar las siguientes funcionalidades:

* **🖥️ Dashboard Operativo:** Visualización en tiempo real de todos los vuelos programados, con su estado actual (Programado, Embarcando, Retrasado, Cancelado).
* **🛂 Gestión de Embarque:**
    * Ver la lista de pasajeros (manifiesto) por vuelo.
    * Verificar pasajeros en la puerta (simulando un escaneo de boleto).
    * Monitorear el progreso del embarque en vivo (ej. "Pasajeros Abordo: 45 / 180").
* **🛫 Gestión de Vuelos:**
    * Actualizar el estado de un vuelo.
    * Asignar puertas de embarque y personal de tierra (agentes, supervisores) a los vuelos.
* **📄 Generación de Documentos:**
    * Generar **Tarjetas de Embarque (Boarding Pass)** en formato PDF para impresión física.
    * Generar listas de pasajeros y reportes operativos.
* **📧 Notificaciones (Futuro):**
    * Enviar alertas por correo electrónico (usando JavaMailSender) a supervisores sobre anomalías (retrasos, cancelaciones).

## 🏗️ Arquitectura del Software

El proyecto sigue una estricta **Arquitectura en 3 Capas (3-Tier)** para asegurar la separación de responsabilidades y la mantenibilidad.



1.  **Capa de Presentación (UI):**
    * Construida con **JavaFX** y FXML para la interfaz gráfica.
    * Responsable de manejar las interacciones del usuario y mostrar los datos.
    * Paquetes: `com.zephyr.ui.controller`, `com.zephyr.ui.fxml`, `com.zephyr.ui.css`

2.  **Capa de Lógica de Negocio (Service):**
    * Contiene el "cerebro" de la aplicación y las reglas de negocio.
    * Orquesta las operaciones entre la UI y la capa de datos. (Ej. `sp_verificar_pasajero_en_puerta`).
    * Paquetes: `com.zephyr.service`, `com.zephyr.service.impl`

3.  **Capa de Acceso a Datos (Repository/DAO):**
    * Es la única capa que habla con la base de datos.
    * Utiliza **JDBC** puro para ejecutar consultas SQL, Vistas y Procedimientos Almacenados.
    * Paquetes: `com.zephyr.repository`, `com.zephyr.repository.impl`

## 🛠️ Stack Tecnológico

* **Lenguaje:** Java 21+ (Vanilla)
* **Interfaz Gráfica:** JavaFX
* **Base de Datos:** PostgreSQL
* **Conexión BD:** JDBC (Java Database Connectivity)
* **IDE:** IntelliJ IDEA

## 🗃️ Base de Datos

La base de datos `zephyr` en PostgreSQL es el núcleo del sistema. Está altamente normalizada (17 tablas) y utiliza lógica del lado del servidor para garantizar la integridad y el rendimiento.

* **Vistas (Views):** Se utilizan 4 vistas principales (ej. `v_vuelos_programados`, `v_pasajeros_por_vuelo`) para simplificar las consultas de lectura complejas para la aplicación Java.
* **Procedimientos Almacenados (SPs):** La lógica de negocio crítica (ej. actualizar estados, verificar pasajeros) se maneja a través de SPs para garantizar la integridad transaccional.

## 🚀 Cómo Empezar

Sigue estos pasos para ejecutar el proyecto localmente.

### 1. Configuración de la Base de Datos

1.  Asegúrate de tener **PostgreSQL** instalado.
2.  Crea una nueva base de datos llamada `zephyr`.
3.  Abre DBeaver (o tu cliente de SQL preferido) y conéctate a la base de datos `zephyr`.
4.  Ejecuta los scripts SQL de la carpeta `/sql` en el siguiente orden:
    1.  `01_crear_tablas.sql` (Crea la estructura de 17 tablas)
    2.  `02_inserts_maestros.sql` (Puebla los catálogos de aerolíneas, aeropuertos, etc.)
    3.  `03_vistas_y_procedimientos.sql` (Crea las Vistas y SPs)
    4.  `04_inserts_prueba.sql` (Inserta vuelos y pasajeros de prueba)

### 2. Configuración de la Aplicación Java

1.  Clona este repositorio: `git clone ...`
2.  Abre el proyecto con IntelliJ IDEA.
3.  **Configura el SDK de JavaFX:**
    * Descarga el JavaFX SDK (versión 21 o superior).
    * Ve a `File` > `Project Structure` > `Libraries` y añade los JARs de la carpeta `lib` de tu JavaFX SDK.
4.  **Configura las VM Options:**
    * Ve a `Run` > `Edit Configurations...`.
    * Busca tu clase `MainApp`.
    * En el campo **`VM options`**, añade lo siguiente (¡reemplaza la ruta!):
        ```bash
        --module-path "C:\ruta\a\tu\javafx-sdk-21\lib" --add-modules javafx.controls,javafx.fxml
        ```
5.  Listo para ejecutar.
