package com.utn.tp.prog3.ui.views;

import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "facturas", layout = MainLayout.class)
@PageTitle("Facturas")
public class FacturasView extends VerticalLayout {
    public FacturasView() {
        add(new H2("Sección Facturas (próximamente)"));
    }
}