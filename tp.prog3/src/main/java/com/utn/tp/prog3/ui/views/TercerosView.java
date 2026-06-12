package com.utn.tp.prog3.ui.views;

import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "terceros", layout = MainLayout.class)
@PageTitle("Terceros")
public class TercerosView extends VerticalLayout {
    public TercerosView() {
        add(new H2("Sección Terceros (próximamente)"));
    }
}
