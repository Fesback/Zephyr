package com.zephyr.main;

import com.zephyr.domain.Personal;
import com.zephyr.repository.PersonalRepository;
import com.zephyr.repository.impl.PersonalRepositoryImpl;
import com.zephyr.service.AuthService;
import com.zephyr.service.impl.AuthServiceImpl;

import java.util.Optional;
import java.util.Scanner;

public class ConsoleApp {

    public static void main(String[] args) {
        PersonalRepository  personalRepository = new PersonalRepositoryImpl();

        AuthService authService = new AuthServiceImpl(personalRepository);

        System.out.println("----- Sistema de Gestion Zephyr (Version Consola) -----");
        Scanner scanner = new Scanner(System.in);

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
        } else {
            System.out.println("\n-----------------------------------------------");
            System.out.println("ERROR: Correo o Contrasena INCORRECTOS");
            System.out.println("\n-----------------------------------------------");
        }

        scanner.close();
    }

}
