package com.utn.tp.prog3.view;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.router.Layout;
import com.vaadin.flow.theme.lumo.Lumo;

@Layout
public class MainLayout extends AppLayout {

    public MainLayout() {
        // Aplica Lumo Dark a toda la aplicación
        getElement().setAttribute("theme", Lumo.DARK);

    }
}
