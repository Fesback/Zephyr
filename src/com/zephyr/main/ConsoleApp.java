package com.zephyr.main;

import com.zephyr.domain.Personal;
import com.zephyr.domain.Rol;
import com.zephyr.repository.PersonalRepository;
import com.zephyr.repository.impl.PersonalRepositoryImpl;
import com.zephyr.service.AuthService;
import com.zephyr.service.PersonalService;
import com.zephyr.service.impl.AuthServiceImpl;
import com.zephyr.service.impl.PersonalServiceImpl;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class ConsoleApp {

    private static final Scanner scanner = new Scanner(System.in);
    private static PersonalService personalService;

    public static void main(String[] args) {
        PersonalRepository personalRepository = new PersonalRepositoryImpl();
        AuthService authService = new AuthServiceImpl(personalRepository);
        personalService = new PersonalServiceImpl(personalRepository);

        System.out.println("----- Sistema de Gestion Zephyr (Version Consola) -----");

        // login
        System.out.println("Introduzca su correo: ");
        String correo = scanner.nextLine();

        System.out.println("Introduzca su contrasena: ");
        String contrasena = scanner.nextLine();

        Optional<Personal> personalLogueado = authService.login(correo, contrasena);

        if (personalLogueado.isPresent()) {
            Personal personal = personalLogueado.get();
            System.out.println("\n-----------------------------------------------");
            System.out.println("Bienvenido, " + personal.getNombres() + " " + personal.getApellidos() + "!");
            System.out.println("Login Exitoso.");
            System.out.println("Tu Rol es: " + personal.getRol().getNombreRol());
            System.out.println("\n-----------------------------------------------");

            // nw logic menu
            if (personal.getRol().getNombreRol().equals("Administrador")) {
                mostrarMenuAdmin();
            } else if (personal.getRol().getNombreRol().equals("Agente de Puerta")) {
                mostrarMenuAgente();
            } else {
                System.out.println("Funcionalidades limitadas para este rol.");
            }

        } else {
            System.out.println("\n-----------------------------------------------");
            System.out.println("ERROR: Correo o Contrasena INCORRECTOS");
            System.out.println("\n-----------------------------------------------");
        }

        scanner.close();
        System.out.println("Saliendo del Sistema...");
    }

    // Admin menu method
    private static void mostrarMenuAdmin() {
        boolean salir = false;

        while (!salir) {
            System.out.println("\n---- Menú de Administrador ----");
            System.out.println("1. Listar todo el Personal (Read)");
            System.out.println("2. Registrar nuevo Agente (Create)");
            System.out.println("3. Actualizar Agente (Update)");
            System.out.println("4. Eliminar Agente (Delete)");
            System.out.println("5. Probar Cola de Embarque (LinkedList)");
            System.out.println("6. Salir");
            System.out.print("Seleccione una opción: ");

            String opcion = scanner.nextLine();

            switch (opcion) {
                case "1":
                    listarPersonal();
                    break;
                case "2":
                    registrarAgente();
                    break;
                case "3":
                    actualizarAgente();
                    break;
                case "4":
                    eliminarAgente();
                    break;
                case "5":
                    simularColaDeEmbarque();
                    break;
                case "6":
                    salir = true;
                    break;
                default:
                    System.out.println("Opcion no valida. intenta de nuevo");
            }
        }
    }


    //Listar
    private static void listarPersonal() {
        System.out.println("\n---- Lista de Personal ----");
        List<Personal> personalLista = personalService.listarTodoElPersonal();
        if (personalLista.isEmpty()) {
            System.out.println("No hay personal registrado.");
            return;
        }
        for (Personal p : personalLista) {
            System.out.println("ID: " + p.getIdPersonal() + " | Nombre: " + p.getNombres() + " " + p.getApellidos() +
                    " | Correo: " + p.getCorreo() + " | Rol: " + p.getRol().getNombreRol());
        }
    }

    // Crear
    private static void registrarAgente() {
        System.out.println("\n---- Registro de Nuevo Agente ----");
        System.out.print("Nombres: ");
        String nombres = scanner.nextLine();
        System.out.print("Apellidos: ");
        String apellidos = scanner.nextLine();
        System.out.print("Correo: ");
        String correo = scanner.nextLine();
        System.out.print("Contraseña temporal: ");
        String pass = scanner.nextLine();

        Rol rolAgente = new Rol(3, "Agente de puerta", "Rol de Agente");
        Personal nuevoAgente = new Personal(
                0,
                nombres,
                apellidos,
                null,
                correo,
                pass,                   //TODO: Los que estan en null es porque no pido esos datos para consola
                null,
                "Mañana",
                1,
                rolAgente
        );

        personalService.registrarPersonal(nuevoAgente);
        System.out.println("\n¡Agente " + nombres + " registrado exitosamente!");
    }

    // Actualizar
    private static void actualizarAgente() {
        System.out.println("\n---- Actualizar Agente ----");
        System.out.print("Ingrese el ID del agente a actualizar: ");
        int id = Integer.parseInt(scanner.nextLine());

        Optional<Personal> personalOptional = personalService.findById(id);

        if (personalOptional.isEmpty()) {
            System.out.println("ERROR: No se encontró ningún agente con el ID: " + id);
            return;
        }

        Personal personalParaActualizar = personalOptional.get();
        System.out.println("Actualizando a: " + personalParaActualizar.getNombres() + " " + personalParaActualizar.getApellidos());
        System.out.print("Ingrese el nuevo correo: ");
        String nuevoCorreo = scanner.nextLine();
        System.out.print("Ingrese el nuevo turno (Mañana/Tarde/Noche): ");
        String nuevoTurno = scanner.nextLine();

        personalParaActualizar.setCorreo(nuevoCorreo);
        personalParaActualizar.setTurno(nuevoTurno);

        personalService.actualizarPersonal(personalParaActualizar);
        System.out.println("\n¡Agente actualizado!");
    }

    // Eliminar
    private static void eliminarAgente() {
        System.out.println("\n---- Eliminar Agente ----");
        System.out.print("Ingrese el ID del agente a eliminar: ");
        int id = Integer.parseInt(scanner.nextLine());

        personalService.eliminarAgente(id);
        System.out.println("\n¡Agente eliminado!");
    }

    // linkedlist
    private static void simularColaDeEmbarque() {
        LinkedList<String> colaEmbarque = new LinkedList<>();

        System.out.println("\n---- Simulación de Cola de Embarque (LinkedList) ----");

        colaEmbarque.add("Juan Pérez (Asiento 12A)");
        colaEmbarque.add("Maria García (Asiento 12B)");
        colaEmbarque.add("Carlos Sánchez (Asiento 12C)");

        System.out.println("Pasajeros en la cola de espera: " + colaEmbarque);

        String pasajeroEmbarcando = colaEmbarque.poll();
        System.out.println("Embarcando a: " + pasajeroEmbarcando);
        System.out.println("Pasajeros restantes: " + colaEmbarque);

        pasajeroEmbarcando = colaEmbarque.poll();
        System.out.println("Embarcando a: " + pasajeroEmbarcando);
        System.out.println("Pasajeros restantes: " + colaEmbarque);
    }

    private static void mostrarMenuAgente() {
        System.out.println("\n---- Menú de Agente de Puerta ----");
        System.out.println("1. Verificar pasajero (Función no implementada en consola)");
        System.out.println("2. Salir");
        System.out.print("Seleccione una opción: ");
        scanner.nextLine();
    }
}



