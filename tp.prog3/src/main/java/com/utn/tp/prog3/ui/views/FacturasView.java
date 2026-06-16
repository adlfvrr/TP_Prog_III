package com.utn.tp.prog3.ui.views;

import com.utn.tp.prog3.backend.dto.request.AddFacturaItemRequest;
import com.utn.tp.prog3.backend.dto.request.AddFacturaRequest;
import com.utn.tp.prog3.backend.dto.response.CompleteFacturaResponse;
import com.utn.tp.prog3.backend.dto.response.FacturaItemResponse;
import com.utn.tp.prog3.backend.dto.response.TerceroResponse;
import com.utn.tp.prog3.ui.client.service.AuthService;
import com.utn.tp.prog3.ui.client.service.FacturaService;
import com.utn.tp.prog3.ui.client.service.TerceroService;
import com.utn.tp.prog3.ui.dto.PageResponse;
import com.vaadin.flow.component.ModalityMode;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
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

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Route(value = "facturas", layout = MainLayout.class)
@PageTitle("Facturas")
public class FacturasView extends VerticalLayout {
    private final FacturaService facturaService;
    private final TerceroService terceroService;
    private final AuthService authService;

    // Componentes del grid y paginación
    private Grid<CompleteFacturaResponse> grid;
    private int currentPage = 0;
    private int totalPages = 0;

    // Filtros
    private IntegerField filterNumero;
    private TextField filterCuit;
    private DatePicker filterFecha;

    // Formulario (solo ADMIN)
    private VerticalLayout formLayout;
    private IntegerField formNumero;
    private DatePicker formFecha;
    private TextField formMontoItem;
    private IntegerField formCantidadItem;
    private TextField formDetalleItem;
    private ComboBox<TerceroResponse> formTerceroCombo; // ComboBox con CUIT - Nombre
    private Grid<AddFacturaItemRequest> itemsGrid;
    private final List<AddFacturaItemRequest> itemsList = new ArrayList<>();
    private static final int MAX_ITEMS = 5;

    private Button btnCrear;
    private Button btnBorrar;
    private Button btnAgregarItem;
    private Button btnEliminarItem;

    private CompleteFacturaResponse selectedFactura = null;

    private List<TerceroResponse> tercerosList = new ArrayList<>();

    public FacturasView(FacturaService facturaService, TerceroService terceroService, AuthService authService) {
        this.facturaService = facturaService;
        this.terceroService = terceroService;
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

        loadFacturas();
        loadTerceros(); // Cargar lista de terceros para el ComboBox
    }

    // ==================== FILTROS ====================
    private void createFilters() {
        filterNumero = new IntegerField("Número");
        filterNumero.setPlaceholder("Filtrar por número");
        filterNumero.addValueChangeListener(e -> loadFacturas());

        filterCuit = new TextField("CUIT del tercero");
        filterCuit.setPlaceholder("Filtrar por CUIT");
        filterCuit.setValueChangeMode(ValueChangeMode.LAZY);
        filterCuit.addValueChangeListener(e -> loadFacturas());

        filterFecha = new DatePicker("Fecha de factura");
        filterFecha.addValueChangeListener(e -> loadFacturas());
    }

    private HorizontalLayout createFilterBar() {
        HorizontalLayout bar = new HorizontalLayout();
        bar.setWrap(true);
        bar.setAlignItems(Alignment.END);
        bar.add(filterNumero, filterCuit, filterFecha);
        return bar;
    }

    // ==================== GRID ====================
    private void createGrid() {
        grid = new Grid<>(CompleteFacturaResponse.class);
        grid.removeAllColumns();
        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        grid.setWidthFull();
        grid.setHeight("400px");

        // Columnas personalizadas
        grid.addColumn(CompleteFacturaResponse::getId_factura)
                .setHeader("ID")
                .setWidth("60px")
                .setKey("id");
        grid.addColumn(CompleteFacturaResponse::getNumero)
                .setHeader("Número")
                .setKey("numero");
        grid.addColumn(f -> f.getFecha_factura() != null ? f.getFecha_factura().toString() : "")
                .setHeader("Fecha")
                .setKey("fecha");

        // Columna Tercero (CUIT - Nombre) - Necesitamos obtener el CUIT y nombre del tercero.
        // Como CompleteFacturaResponse solo tiene id_tercero, necesitamos un mapa cache o llamada extra.
        // Para simplificar, mostraremos "ID: " + id_tercero. Luego mejoraremos.
        grid.addColumn(f -> "ID: " + (f.getId_tercero() != null ? f.getId_tercero().toString() : "N/A"))
                .setHeader("Tercero (ID)")
                .setKey("tercero");

        // Columna Cantidad de items (con evento click para abrir Dialog)
        grid.addColumn(f -> f.getItemResponseList() != null ? f.getItemResponseList().size() : 0)
                .setHeader("Items")
                .setKey("cantidadItems")
                .setComparator((f1, f2) -> {
                    int size1 = f1.getItemResponseList() != null ? f1.getItemResponseList().size() : 0;
                    int size2 = f2.getItemResponseList() != null ? f2.getItemResponseList().size() : 0;
                    return Integer.compare(size1, size2);
                });

        // Hacer que la celda de "Items" sea clickeable para abrir el Dialog
        grid.addItemClickListener(event -> {
            if (event.getColumn() != null && "cantidadItems".equals(event.getColumn().getKey())) {
                CompleteFacturaResponse factura = event.getItem();
                if (factura != null && factura.getItemResponseList() != null && !factura.getItemResponseList().isEmpty()) {
                    showItemsDialog(factura.getItemResponseList());
                } else {
                    Notification.show("Esta factura no tiene items", 2000, Notification.Position.MIDDLE);
                }
            }
        });

        grid.asSingleSelect().addValueChangeListener(event -> {
            selectedFactura = event.getValue();
            if (selectedFactura != null) {
                loadFacturaToForm(selectedFactura);
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
                loadFacturas();
            }
        });
        Button nextButton = new Button(VaadinIcon.ANGLE_RIGHT.create(), e -> {
            if (currentPage < totalPages - 1) {
                currentPage++;
                loadFacturas();
            }
        });
        HorizontalLayout bar = new HorizontalLayout(prevButton, nextButton);
        bar.setAlignItems(Alignment.CENTER);
        return bar;
    }

    // ==================== DIALOG PARA ITEMS ====================
    private void showItemsDialog(List<FacturaItemResponse> items) {
        Dialog dialog = new Dialog();
        dialog.setWidth("600px");
        dialog.setHeight("400px");
        dialog.setModality(ModalityMode.STRICT);
        dialog.setDraggable(true);
        dialog.setResizable(true);

        Grid<FacturaItemResponse> itemsGridDialog = new Grid<>(FacturaItemResponse.class);
        itemsGridDialog.setItems(items);
        itemsGridDialog.setColumns("monto", "cantidad", "detalle");
        itemsGridDialog.getColumnByKey("monto").setHeader("Monto");
        itemsGridDialog.getColumnByKey("cantidad").setHeader("Cantidad");
        itemsGridDialog.getColumnByKey("detalle").setHeader("Detalle");
        itemsGridDialog.setWidthFull();
        itemsGridDialog.setHeight("300px");

        Button closeButton = new Button("Cerrar", e -> dialog.close());
        VerticalLayout dialogLayout = new VerticalLayout(
                new H3("Items de la factura"),
                itemsGridDialog,
                closeButton
        );
        dialogLayout.setPadding(true);
        dialogLayout.setSpacing(true);
        dialog.add(dialogLayout);
        dialog.open();
    }

    // ==================== FORMULARIO ====================
    private void createForm() {
        formLayout = new VerticalLayout();
        formLayout.setPadding(true);
        formLayout.setSpacing(true);
        formLayout.setVisible(authService.isAdmin());

        H3 titulo = new H3("Crear Factura");
        formNumero = new IntegerField("Número");
        formNumero.setWidthFull();
        formFecha = new DatePicker("Fecha");
        formFecha.setWidthFull();

        // ComboBox para Tercero (CUIT - Nombre)
        formTerceroCombo = new ComboBox<>("Tercero");
        formTerceroCombo.setWidthFull();
        formTerceroCombo.setItemLabelGenerator(tercero ->
                (tercero.getCuitl() != null ? tercero.getCuitl() : "") +
                        " - " +
                        (tercero.getNombre() != null ? tercero.getNombre() : ""));
        formTerceroCombo.setPlaceholder("Seleccione un tercero");

        //Para la creación de items
        formMontoItem = new TextField("Monto");
        formMontoItem.setWidthFull();
        formCantidadItem = new IntegerField("Cantidad");
        formCantidadItem.setWidthFull();
        formDetalleItem = new TextField("Detalle");
        formDetalleItem.setWidthFull();

        // Grid de items (para creación)
        itemsGrid = new Grid<>(AddFacturaItemRequest.class);
        itemsGrid.setColumns("monto", "cantidad", "detalle");
        itemsGrid.getColumnByKey("monto").setHeader("Monto");
        itemsGrid.getColumnByKey("cantidad").setHeader("Cantidad");
        itemsGrid.getColumnByKey("detalle").setHeader("Detalle");
        itemsGrid.setHeight("150px");

        btnAgregarItem = new Button("Agregar item", VaadinIcon.PLUS.create());
        btnEliminarItem = new Button("Eliminar item", VaadinIcon.MINUS.create());
        btnEliminarItem.setEnabled(false);

        itemsGrid.asSingleSelect().addValueChangeListener(e -> btnEliminarItem.setEnabled(e.getValue() != null));

        btnAgregarItem.addClickListener(e -> {
            if (itemsList.size() >= MAX_ITEMS) {
                Notification.show("Máximo " + MAX_ITEMS + " items por factura", 3000, Notification.Position.MIDDLE);
                return;
            }
            double montoItem = Double.parseDouble(formMontoItem.getValue());
            int cantidadItem = formCantidadItem.getValue();
            String detalleItem = formDetalleItem.getValue();
            AddFacturaItemRequest item = new AddFacturaItemRequest();
            item.setMonto(montoItem);
            item.setCantidad(cantidadItem);
            item.setDetalle(detalleItem);
            itemsList.add(item);
            refreshItemsGrid();
        });

        btnEliminarItem.addClickListener(e -> {
            AddFacturaItemRequest selected = itemsGrid.asSingleSelect().getValue();
            if (selected != null) {
                itemsList.remove(selected);
                refreshItemsGrid();
                btnEliminarItem.setEnabled(false);
            }
        });

        HorizontalLayout itemButtons = new HorizontalLayout(btnAgregarItem, btnEliminarItem);

        btnCrear = new Button("Crear Factura", VaadinIcon.PLUS.create());
        btnBorrar = new Button("Borrar Factura", VaadinIcon.TRASH.create());

        btnCrear.addClickListener(e -> crearFactura());
        btnBorrar.addClickListener(e -> borrarFactura());

        HorizontalLayout mainButtons = new HorizontalLayout(btnCrear, btnBorrar);

        formLayout.add(titulo, formNumero, formFecha, formTerceroCombo,
                new H3("Items"), formMontoItem, formCantidadItem, formDetalleItem, itemsGrid, itemButtons, mainButtons);
        enableFormButtons(false);
    }

    private void refreshItemsGrid() {
        itemsGrid.setItems(itemsList);
    }

    private void loadTerceros() {
        try {
            List<TerceroResponse> terceros = terceroService.findAllSimple();
            this.tercerosList = terceros;
            formTerceroCombo.setItems(terceros);
        } catch (Exception e) {
            Notification.show("Error al cargar terceros: " + e.getMessage(), 3000, Notification.Position.MIDDLE);
        }
    }

    private void loadFacturaToForm(CompleteFacturaResponse f) {
        // Cargar datos principales (solo lectura)
        formNumero.setValue(f.getNumero());
        if (f.getFecha_factura() != null) {
            formFecha.setValue(f.getFecha_factura().toInstant()
                    .atZone(ZoneId.systemDefault()).toLocalDate());
        } else {
            formFecha.clear();
        }
        // Seleccionar el tercero en el ComboBox (si está en la lista)
        if (f.getId_tercero() != null) {
            // Como el ComboBox contiene objetos TerceroResponse, necesitamos buscar el que tenga ese ID.
            // Si no lo encontramos, mostramos un placeholder con el ID.
            TerceroResponse terceroSeleccionado = tercerosList
                    .stream()
                    .filter(t -> t.getIdTercero().equals(f.getId_tercero()))
                    .findFirst()
                    .orElse(null);
            formTerceroCombo.setValue(terceroSeleccionado);
        } else {
            formTerceroCombo.clear();
        }

        // Cargar items en el grid (solo lectura)
        itemsList.clear();
        if (f.getItemResponseList() != null) {
            for (FacturaItemResponse itemResp : f.getItemResponseList()) {
                AddFacturaItemRequest item = new AddFacturaItemRequest();
                item.setMonto(itemResp.getMonto());
                item.setCantidad(itemResp.getCantidad());
                item.setDetalle(itemResp.getDetalle());
                itemsList.add(item);
            }
        }
        refreshItemsGrid();
        // Deshabilitar edición en modo visualización
        itemsGrid.setEnabled(false);
        btnAgregarItem.setEnabled(false);
        btnEliminarItem.setEnabled(false);
        formNumero.setReadOnly(true);
        formFecha.setReadOnly(true);
        formTerceroCombo.setReadOnly(true);
        btnCrear.setEnabled(false);
    }

    private void clearForm() {
        formNumero.clear();
        formFecha.clear();
        formTerceroCombo.clear();
        itemsList.clear();
        refreshItemsGrid();
        // Habilitar edición para nuevo registro
        itemsGrid.setEnabled(true);
        btnAgregarItem.setEnabled(true);
        btnEliminarItem.setEnabled(false);
        formNumero.setReadOnly(false);
        formFecha.setReadOnly(false);
        formTerceroCombo.setReadOnly(false);
        btnCrear.setEnabled(true);
        selectedFactura = null;
    }

    private void enableFormButtons(boolean hasSelection) {
        if (hasSelection) {
            btnCrear.setEnabled(false);
            btnBorrar.setEnabled(true);
        } else {
            btnCrear.setEnabled(true);
            btnBorrar.setEnabled(false);
        }
    }

    // ==================== CARGA DE DATOS ====================
    private void loadFacturas() {
        Integer numero = filterNumero.getValue();
        String cuit = filterCuit.getValue();
        LocalDate localDate = filterFecha.getValue();
        Date fecha = localDate != null ? Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant()) : null;


        int pageSize = 5;
        PageResponse<CompleteFacturaResponse> page = facturaService.findAll(numero, cuit, fecha, currentPage, pageSize);
        grid.setItems(page.getContent());
        totalPages = page.getTotalPages();
        if (currentPage >= totalPages && totalPages > 0) {
            currentPage = totalPages - 1;
            loadFacturas();
        }
    }

    // ==================== OPERACIONES CRUD ====================
    private void crearFactura() {
        // Validar campos
        Integer numero = formNumero.getValue();
        if (numero == null || numero <= 0) {
            Notification.show("El número debe ser mayor a 0");
            return;
        }
        LocalDate fechaLocal = formFecha.getValue();
        if (fechaLocal == null) {
            Notification.show("La fecha es obligatoria");
            return;
        }
        TerceroResponse terceroSeleccionado = formTerceroCombo.getValue();
        if (terceroSeleccionado == null) {
            Notification.show("Debe seleccionar un tercero");
            return;
        }
        if (itemsList.isEmpty()) {
            Notification.show("Debe agregar al menos un item");
            return;
        }

        // Construir request
        AddFacturaRequest request = new AddFacturaRequest();
        request.setNumero(numero);
        request.setFecha_factura(Date.from(fechaLocal.atStartOfDay(ZoneId.systemDefault()).toInstant()));
        request.setId_tecero(terceroSeleccionado.getIdTercero());
        request.setItemRequestList(new ArrayList<>(itemsList));

        try {
            CompleteFacturaResponse created = facturaService.create(request);
            Notification.show("Factura creada exitosamente (ID: " + created.getId_factura() + ")", 3000, Notification.Position.MIDDLE);
            clearForm();
            loadFacturas();
        } catch (Exception ex) {
            Notification.show("Error al crear: " + ex.getMessage(), 5000, Notification.Position.MIDDLE);
        }
    }

    private void borrarFactura() {
        if (selectedFactura == null) return;
        try {
            facturaService.delete(selectedFactura.getId_factura());
            Notification.show("Factura eliminada correctamente", 3000, Notification.Position.MIDDLE);
            clearForm();
            loadFacturas();
        } catch (Exception ex) {
            Notification.show("Error al borrar: " + ex.getMessage(), 5000, Notification.Position.MIDDLE);
        }
    }
}