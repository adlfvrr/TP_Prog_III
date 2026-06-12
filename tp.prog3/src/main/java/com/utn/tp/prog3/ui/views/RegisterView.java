package com.utn.tp.prog3.ui.views;

import com.utn.tp.prog3.ui.client.service.AuthService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.validation.constraints.Email;

@Route("register")
@PageTitle("Registro de usuarios")
public class RegisterView extends VerticalLayout {

    private final AuthService authService;

    public RegisterView(AuthService authService) {

        this.authService = authService;
        //Seguimos el mismo proceso que con el login

        setSizeFull();
        setAlignItems(FlexComponent.Alignment.CENTER);
        setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);

        TextField usernameField = new TextField("Usuario");
        usernameField.setWidth("300px");
        EmailField emailField = new EmailField("Correo electrónico");
        emailField.setWidth("300px");
        PasswordField passwordField = new PasswordField("Contraseña");
        passwordField.setWidth("300px");

        Button registerButton = new Button("Registrar", event -> {
            try {
                String username = usernameField.getValue();
                String email = emailField.getValue();
                String password = passwordField.getValue();

                this.authService.register(username, email, password);

                getUI().ifPresent(ui -> ui.navigate("login"));
            } catch (Exception e) {
                Notification.show("Error: " + e.getMessage(), 3000, Notification.Position.MIDDLE);
            }
        });

        Button loginLink = new Button("¿Ya tienes una cuenta? Inicia sesión", event -> {
            getUI().ifPresent(ui -> ui.navigate("login"));
        });

        add(usernameField,
                emailField,
                passwordField,
                registerButton,
                loginLink);

    }

}
