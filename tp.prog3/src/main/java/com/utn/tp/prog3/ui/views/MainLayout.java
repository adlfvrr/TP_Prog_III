package com.utn.tp.prog3.ui.views;

import com.utn.tp.prog3.ui.client.ApiClient;
import com.utn.tp.prog3.ui.client.service.AuthService;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.RouterLink;

public class MainLayout extends AppLayout implements BeforeEnterObserver {

    //Utilizaremos apiClient para poder cerrar sesión desde la dashboard
    private final AuthService authService;

    public MainLayout(ApiClient apiClient, AuthService authService) {
        this.authService = authService;
        // Cabecera con botón de menú (drawer) y título
        DrawerToggle toggle = new DrawerToggle();
        H1 title = new H1("Mi Gestión");
        //Creamos un botón para cerrar sesión
        Button logoutButton = new Button("Cerrar sesión", event -> {
            authService.logout();
            getUI().ifPresent(ui -> ui.navigate("login")); // Redirigir a login
        });
        HorizontalLayout header = new HorizontalLayout(toggle, title);
        HorizontalLayout buttonLayout = new HorizontalLayout(logoutButton);
        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        buttonLayout.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.END);
        header.setWidthFull();
        addToNavbar(header);
        addToNavbar(buttonLayout);

        // Menú lateral: usamos Tabs con RouterLink para cada vista
        Tabs tabs = new Tabs();
        tabs.setOrientation(Tabs.Orientation.VERTICAL);
        tabs.setWidthFull();
        tabs.setAutoselect(false);

        // Crear pestañas con íconos y enlaces
        Tab tabTerceros = createTab(VaadinIcon.USER, "Terceros", TercerosView.class);
        Tab tabFacultades = createTab(VaadinIcon.BUILDING, "Facultades", FacultadesView.class);
        Tab tabFacturas = createTab(VaadinIcon.FILE_TEXT, "Facturas", FacturasView.class);
        Tab tabPagos = createTab(VaadinIcon.MONEY, "Pagos", PagosView.class);

        tabs.add(tabTerceros, tabFacultades, tabFacturas, tabPagos);

        // Agregar el menú al drawer (barra lateral)
        addToDrawer(tabs);
    }

    //Método para crear las pestañas de cada entidad
    private Tab createTab(VaadinIcon icon,
                          String text,
                          Class<? extends com.vaadin.flow.component.Component> targetView) {
        RouterLink link = new RouterLink();
        link.setRoute(targetView);
        link.add(icon.create(), new Span(text));
        link.getElement().getStyle().set("display", "flex");
        link.getElement().getStyle().set("align-items", "center");
        link.getElement().getStyle().set("gap", "var(--lumo-space-s)");
        Tab tab = new Tab(link);
        return tab;
    }

    //Método que verifica, antes de entrar, un evento. En este caso utilizamos nuestro isAuthenticated de nuestro servicio para
    //verificar si el usuario está autenticado. Si no lo está, lo redirigimos a la vista de login.
    @Override
    public void beforeEnter(BeforeEnterEvent event){
        if(!authService.isAuthenticated()){
            event.rerouteTo("login");
        }
    }
}
