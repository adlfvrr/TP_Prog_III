package com.utn.tp.prog3.ui.views;

import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "pagos", layout = MainLayout.class)
@PageTitle("Pagos")
public class PagosView extends VerticalLayout {
    public PagosView() {
        add(new H2("Sección Pagos (próximamente)"));
    }
}