package com.utn.tp.prog3.ui.views;

import com.utn.tp.prog3.backend.dto.request.AddFacultadRequest;
import com.utn.tp.prog3.backend.dto.request.UpdateFacultadRequest;
import com.utn.tp.prog3.backend.dto.response.FacultadResponse;
import com.utn.tp.prog3.ui.client.service.AuthService;
import com.utn.tp.prog3.ui.client.service.FacultadService;
import com.utn.tp.prog3.ui.dto.PageResponse;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "facultades", layout = MainLayout.class)
@PageTitle("Facultades")
public class FacultadesView extends VerticalLayout {
    private final FacultadService facultadService;
    private final AuthService authService;

    // Componentes del grid y paginación
    private Grid<FacultadResponse> grid;
    private int currentPage = 0;
    private int totalPages = 0;

    // Filtros
    private TextField filterNombre;
    private TextField filterDireccion;
    private TextField filterCuit;
    private TextField filterTelefono;
    private TextField filterEmail;

    // Formulario (solo ADMIN)
    private VerticalLayout formLayout;
    private TextField formNombre;
    private TextField formDireccion;
    private TextField formCuit;
    private IntegerField formSucursal;
    private TextField formTelefono;
    private TextField formEmail;
    private Checkbox formDefecto;

    private Button btnCrear;
    private Button btnActualizar;
    private Button btnBorrar;

    private FacultadResponse selectedFacultad = null;

    public FacultadesView(FacultadService facultadService, AuthService authService) {
        this.facultadService = facultadService;
        this.authService = authService;

        setSizeFull();
        setPadding(true);
        setSpacing(true);

        createFilters();
        createGrid();
        createForm();

        HorizontalLayout mainLayout = new HorizontalLayout();
        mainLayout.setSizeFull();
        mainLayout.setSpacing(true);

        VerticalLayout gridSection = new VerticalLayout();
        gridSection.setWidth("60%");
        gridSection.add(createFilterBar(), grid, createPaginationBar());

        formLayout.setWidth("40%");
        mainLayout.add(gridSection, formLayout);
        add(mainLayout);

        loadFacultades();
    }

    // ==================== FILTROS ====================
    private void createFilters() {
        filterNombre = new TextField("Nombre");
        filterNombre.setPlaceholder("Filtrar por nombre");
        filterNombre.setValueChangeMode(ValueChangeMode.LAZY);
        filterNombre.addValueChangeListener(e -> loadFacultades());

        filterDireccion = new TextField("Dirección");
        filterDireccion.setPlaceholder("Filtrar por dirección");
        filterDireccion.setValueChangeMode(ValueChangeMode.LAZY);
        filterDireccion.addValueChangeListener(e -> loadFacultades());

        filterCuit = new TextField("CUIT");
        filterCuit.setPlaceholder("Filtrar por CUIT");
        filterCuit.setValueChangeMode(ValueChangeMode.LAZY);
        filterCuit.addValueChangeListener(e -> loadFacultades());

        filterTelefono = new TextField("Teléfono");
        filterTelefono.setPlaceholder("Filtrar por teléfono");
        filterTelefono.setValueChangeMode(ValueChangeMode.LAZY);
        filterTelefono.addValueChangeListener(e -> loadFacultades());

        filterEmail = new TextField("Email");
        filterEmail.setPlaceholder("Filtrar por email");
        filterEmail.setValueChangeMode(ValueChangeMode.LAZY);
        filterEmail.addValueChangeListener(e -> loadFacultades());
    }

    private HorizontalLayout createFilterBar() {
        HorizontalLayout bar = new HorizontalLayout();
        bar.setWrap(true);
        bar.setAlignItems(Alignment.END);
        bar.add(filterNombre, filterDireccion, filterCuit, filterTelefono, filterEmail);
        return bar;
    }

    // ==================== GRID ====================
    private void createGrid() {
        grid = new Grid<>(FacultadResponse.class);
        // Seleccionamos las columnas a mostrar (ignoramos "defectos" que es un string derivado, mejor mostrar el booleano)
        grid.setColumns("id", "nombre", "direccion", "cuit", "sucursal", "telefono", "email");
        // Añadir columna personalizada para "defecto" (booleano)
        grid.addColumn(facultad -> facultad.getDefectos() != null && facultad.getDefectos() ? "Con defectos" : "Sin defectos")
                .setHeader("Estado")
                .setKey("estado");
        grid.getColumnByKey("id").setHeader("ID").setWidth("60px");
        grid.setWidthFull();
        grid.setHeight("400px");

        grid.asSingleSelect().addValueChangeListener(event -> {
            selectedFacultad = event.getValue();
            if (selectedFacultad != null) {
                loadFacultadToForm(selectedFacultad);
                enableFormButtons(true);
            } else {
                clearForm();
                enableFormButtons(false);
            }
        });
    }

    // ==================== PAGINACIÓN ====================
    private HorizontalLayout createPaginationBar() {
        Button prevButton = new Button(VaadinIcon.ANGLE_LEFT.create(), e -> {
            if (currentPage > 0) {
                currentPage--;
                loadFacultades();
            }
        });
        Button nextButton = new Button(VaadinIcon.ANGLE_RIGHT.create(), e -> {
            if (currentPage < totalPages - 1) {
                currentPage++;
                loadFacultades();
            }
        });
        HorizontalLayout bar = new HorizontalLayout(prevButton, nextButton);
        bar.setAlignItems(Alignment.CENTER);
        return bar;
    }

    // ==================== FORMULARIO ====================
    private void createForm() {
        formLayout = new VerticalLayout();
        formLayout.setPadding(true);
        formLayout.setSpacing(true);
        // Solo visible para ADMIN
        formLayout.setVisible(authService.isAdmin());

        H3 titulo = new H3("Formulario de Facultad");
        formNombre = new TextField("Nombre");
        formNombre.setWidthFull();
        formDireccion = new TextField("Dirección");
        formDireccion.setWidthFull();
        formCuit = new TextField("CUIT");
        formCuit.setWidthFull();
        formSucursal = new IntegerField("Sucursal");
        formSucursal.setWidthFull();
        formTelefono = new TextField("Teléfono");
        formTelefono.setWidthFull();
        formEmail = new TextField("Email");
        formEmail.setWidthFull();
        formDefecto = new Checkbox("¿Tiene defectos?");

        btnCrear = new Button("Crear", VaadinIcon.PLUS.create());
        btnActualizar = new Button("Actualizar", VaadinIcon.REFRESH.create());
        btnBorrar = new Button("Borrar", VaadinIcon.TRASH.create());

        btnCrear.addClickListener(e -> crearFacultad());
        btnActualizar.addClickListener(e -> actualizarFacultad());
        btnBorrar.addClickListener(e -> borrarFacultad());

        HorizontalLayout buttons = new HorizontalLayout(btnCrear, btnActualizar, btnBorrar);
        formLayout.add(titulo, formNombre, formDireccion, formCuit, formSucursal,
                formTelefono, formEmail, formDefecto, buttons);
        enableFormButtons(false);
    }

    private void loadFacultadToForm(FacultadResponse f) {
        formNombre.setValue(f.getNombre() != null ? f.getNombre() : "");
        formDireccion.setValue(f.getDireccion() != null ? f.getDireccion() : "");
        formCuit.setValue(f.getCuit() != null ? f.getCuit() : "");
        formSucursal.setValue(f.getSucursal());
        formTelefono.setValue(f.getTelefono() != null ? f.getTelefono() : "");
        formEmail.setValue(f.getEmail() != null ? f.getEmail() : "");
        // El campo defectos en la respuesta es un string, pero necesitamos el booleano original.
        // Como el backend no devuelve 'defecto' booleano, debemos tenerlo en FacultadResponse.
        // Si tu FacultadResponse no tiene campo 'defecto', pide al backend que lo incluya.
        formDefecto.setValue(f.getDefectos() != null ? f.getDefectos() : false); // Necesitas que FacultadResponse tenga getDefecto() boolean
    }

    private void clearForm() {
        formNombre.clear();
        formDireccion.clear();
        formCuit.clear();
        formSucursal.clear();
        formTelefono.clear();
        formEmail.clear();
        formDefecto.setValue(false);
        selectedFacultad = null;
    }

    private void enableFormButtons(boolean hasSelection) {
        btnCrear.setEnabled(true);
        btnActualizar.setEnabled(hasSelection);
        btnBorrar.setEnabled(hasSelection);
    }

    // ==================== CARGA DE DATOS ====================
    private void loadFacultades() {
        String nombre = filterNombre.getValue();
        String direccion = filterDireccion.getValue();
        String cuit = filterCuit.getValue();
        String telefono = filterTelefono.getValue();
        String email = filterEmail.getValue();

        int pageSize = 5;
        PageResponse<FacultadResponse> page = facultadService.findAll(nombre, direccion, cuit,
                telefono, email, currentPage, pageSize);
        grid.setItems(page.getContent());
        totalPages = page.getTotalPages();
        // Ajustar página si la actual excede el total
        if (currentPage >= totalPages && totalPages > 0) {
            currentPage = totalPages - 1;
            loadFacultades();
        }
    }

    // ==================== OPERACIONES CRUD ====================
    private void crearFacultad() {
        AddFacultadRequest request = new AddFacultadRequest();
        request.setNombre(formNombre.getValue());
        request.setDireccion(formDireccion.getValue());
        request.setCuit(formCuit.getValue());
        request.setSucursal(formSucursal.getValue());
        request.setTelefono(formTelefono.getValue());
        request.setEmail(formEmail.getValue());
        request.setDefecto(formDefecto.getValue());

        try {
            facultadService.create(request);
            Notification.show("Facultad creada exitosamente");
            clearForm();
            loadFacultades();
        } catch (Exception ex) {
            Notification.show("Error al crear: " + ex.getMessage());
        }
    }

    private void actualizarFacultad() {
        if (selectedFacultad == null) return;
        UpdateFacultadRequest request = new UpdateFacultadRequest();
        request.setNombre(formNombre.getValue());
        request.setDireccion(formDireccion.getValue());
        request.setCuit(formCuit.getValue());
        request.setSucursal(formSucursal.getValue());
        request.setTelefono(formTelefono.getValue());
        request.setEmail(formEmail.getValue());
        request.setDefecto(formDefecto.getValue());

        try {
            facultadService.update(selectedFacultad.getId(), request);
            Notification.show("Facultad actualizada correctamente");
            loadFacultades();
            clearForm();
        } catch (Exception ex) {
            Notification.show("Error al actualizar: " + ex.getMessage());
        }
    }

    private void borrarFacultad() {
        if (selectedFacultad == null) return;
        try {
            facultadService.delete(selectedFacultad.getId());
            Notification.show("Facultad eliminada correctamente");
            clearForm();
            loadFacultades();
        } catch (Exception ex) {
            Notification.show("Error al borrar: " + ex.getMessage());
        }
    }
}