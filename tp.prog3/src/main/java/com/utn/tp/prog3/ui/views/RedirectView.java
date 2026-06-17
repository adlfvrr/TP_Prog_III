package com.utn.tp.prog3.ui.views;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route("")   // Vista que se encarga de, al ejecutar la aplicación, directamente envíe al login
@PageTitle("Redirigiendo...")
public class RedirectView extends VerticalLayout implements BeforeEnterObserver {

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        // Redirige automáticamente al login
        event.forwardTo("login");
    }
}