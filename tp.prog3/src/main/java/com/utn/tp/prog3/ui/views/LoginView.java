package com.utn.tp.prog3.ui.views;

import com.utn.tp.prog3.ui.client.service.AuthService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;


@Route("login") // /ui/login
@PageTitle("Inicio de sesión")
public class LoginView extends VerticalLayout {

    private final AuthService authService;

    public LoginView(AuthService authService) {
        this.authService = authService;
        setSizeFull();                     // Ocupa todo el ancho/alto
        setAlignItems(FlexComponent.Alignment.CENTER);   // Centrar horizontalmente
        setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER); // Centrar verticalmente

        // Componentes del formulario
        TextField usernameField = new TextField("Usuario");
        usernameField.setWidth("300px");

        PasswordField passwordField = new PasswordField("Contraseña");
        passwordField.setWidth("300px");

        Button loginButton = new Button("Iniciar sesión", event -> {
            try {
            String username = usernameField.getValue();
            String password = passwordField.getValue();

                this.authService.login(username, password);
                // Si el login es exitoso, redirigir a la vista principal (aún no creada)
                // Por ahora a una ruta temporal "dashboard" (la crearemos después)
                getUI().ifPresent(ui -> ui.navigate("dashboard"));
            } catch (Exception e) {
                Notification.show("Error: " + e.getMessage(), 3000, Notification.Position.MIDDLE);
            }
        });

        Button registerLink = new Button("¿Eres nuevo? Regístrate", event -> getUI().ifPresent(ui -> ui.navigate("register")));

        registerLink.addClassName("link-button");

        add(
                new H1("Bienvenido"),
                usernameField,
                passwordField,
                loginButton,
                registerLink
        );
    }

}
