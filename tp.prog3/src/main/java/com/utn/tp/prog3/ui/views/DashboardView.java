package com.utn.tp.prog3.ui.views;

import com.utn.tp.prog3.ui.client.service.AuthService;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "dashboard", layout = MainLayout.class)   // URL /ui
@PageTitle("Dashboard")
public class DashboardView extends VerticalLayout {

    private final AuthService authService;

    public DashboardView(AuthService authService) {
        this.authService = authService;

        Paragraph paragraph = new Paragraph("Presione en una pestaña para mostrarla aquí.");
        paragraph.getStyle().set("margin-top", "10rem");
        paragraph.getStyle().set("margin-left", "3rem");
        paragraph.getStyle().set("font-size", "2rem");
        paragraph.getStyle().set("color", "gray");
        paragraph.getStyle().set("font-weight", "bold");
        paragraph.getStyle().set("text-align", "center");

        add(new H2("¡Bienvenido " + authService.getUsername() +"\uD83D\uDC4B! Selecciona una opción del menú."),
                paragraph);
    }
}
