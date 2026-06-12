package com.utn.tp.prog3.ui.views;

import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "facultades", layout = MainLayout.class)
@PageTitle("Facultades")
public class FacultadesView extends VerticalLayout {
    public FacultadesView() {
        add(new H2("Sección Facultades (próximamente)"));
    }
}