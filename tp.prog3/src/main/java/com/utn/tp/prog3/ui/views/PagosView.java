package com.utn.tp.prog3.ui.views;

import com.utn.tp.prog3.backend.dto.request.AddPagoDetalleRequest;
import com.utn.tp.prog3.backend.dto.request.AddPagoRequest;
import com.utn.tp.prog3.backend.dto.response.CompletePagoResponse;
import com.utn.tp.prog3.backend.dto.response.PagoDetalleResponse;
import com.utn.tp.prog3.backend.dto.response.TerceroResponse;
import com.utn.tp.prog3.ui.client.service.AuthService;
import com.utn.tp.prog3.ui.client.service.PagoService;
import com.utn.tp.prog3.ui.client.service.TerceroService;
import com.utn.tp.prog3.ui.dto.PageResponse;
import com.vaadin.flow.component.ModalityMode;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;



import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Route(value = "pagos", layout = MainLayout.class)
@PageTitle("Pagos")
public class PagosView extends VerticalLayout {

    private final PagoService pagoService;
    private final TerceroService terceroService;
    private final AuthService authService;

    // Componentes del grid y paginación
    private Grid<CompletePagoResponse> grid;
    private int currentPage = 0;
    private int totalPages = 0;

    // Filtros
    private TextField filterCuit;
    private TextField filterModoPago;
    private DatePicker filterFecha;

    // Formulario (solo ADMIN)
    private VerticalLayout formLayout;
    private DatePicker formFecha;
    private NumberField formMonto;
    private TextField formModoPago;
    private ComboBox<TerceroResponse> formTerceroCombo;
    private List<TerceroResponse> tercerosList = new ArrayList<>();

    // Campos del detalle
    private TextField formInstrumentNumber;
    private DatePicker formInstrumentDate;
    private TextField formBanco;
    private Checkbox formPagoRealizado;

    private Button btnCrear;
    private Button btnBorrar;

    private CompletePagoResponse selectedPago = null;

    public PagosView(PagoService pagoService, TerceroService terceroService, AuthService authService) {
        this.pagoService = pagoService;
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

        loadPagos();
        loadTerceros();
    }

    // ==================== FILTROS ====================
    private void createFilters() {
        filterCuit = new TextField("CUIT del tercero");
        filterCuit.setPlaceholder("Filtrar por CUIT");
        filterCuit.setValueChangeMode(ValueChangeMode.LAZY);
        filterCuit.addValueChangeListener(e -> loadPagos());

        filterModoPago = new TextField("Modo de pago");
        filterModoPago.setPlaceholder("Filtrar por modo (efectivo, tarjeta, etc.)");
        filterModoPago.setValueChangeMode(ValueChangeMode.LAZY);
        filterModoPago.addValueChangeListener(e -> loadPagos());

        filterFecha = new DatePicker("Fecha de pago");
        filterFecha.addValueChangeListener(e -> loadPagos());
    }

    private HorizontalLayout createFilterBar() {
        HorizontalLayout bar = new HorizontalLayout();
        bar.setWrap(true);
        bar.setAlignItems(Alignment.END);
        bar.add(filterCuit, filterModoPago, filterFecha);
        return bar;
    }

    // ==================== GRID ====================
    private void createGrid() {
        grid = new Grid<>(CompletePagoResponse.class);
        grid.removeAllColumns();
        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        grid.setWidthFull();
        grid.setHeight("400px");

        grid.addColumn(CompletePagoResponse::getIdPago)
                .setHeader("ID")
                .setWidth("60px")
                .setKey("id");
        grid.addColumn(p -> p.getFechaPago() != null ? p.getFechaPago().toString() : "")
                .setHeader("Fecha")
                .setKey("fechaPago");
        grid.addColumn(CompletePagoResponse::getMontoPago)
                .setHeader("Monto")
                .setKey("montoPago");
        grid.addColumn(CompletePagoResponse::getModoPago)
                .setHeader("Modo de pago")
                .setKey("modoPago");
        grid.addColumn(p -> "ID: " + (p.getIdTercero() != null ? p.getIdTercero().toString() : "N/A"))
                .setHeader("Tercero (ID)")
                .setKey("tercero");

        // Columna "Detalle" con botón
        grid.addComponentColumn(pago -> {
                    Button detalleBtn = new Button("Ver detalle", VaadinIcon.EYE.create());
                    detalleBtn.addClickListener(e -> showDetalleDialog(pago.getDetalleResponse()));
                    return detalleBtn;
                }).setHeader("Detalle")
                .setKey("detalleResponse")
                .setWidth("120px");

        grid.asSingleSelect().addValueChangeListener(event -> {
            selectedPago = event.getValue();
            if (selectedPago != null) {
                loadPagoToForm(selectedPago);
                enableFormButtons(true);
            } else {
                clearForm();
                enableFormButtons(false);
            }
        });
    }

    // ==================== DIALOG PARA DETALLE ====================
    private void showDetalleDialog(PagoDetalleResponse detalleResponse) {
        if (detalleResponse == null) {
            Notification.show("El pago no tiene detalle asociado", 3000, Notification.Position.MIDDLE);
            return;
        }

        Dialog dialog = new Dialog();
        dialog.setWidth("500px");
        dialog.setModality(ModalityMode.STRICT);
        dialog.setDraggable(true);
        dialog.setResizable(true);

        VerticalLayout layout = new VerticalLayout();
        layout.setPadding(true);
        layout.setSpacing(true);

        layout.add(new H3("Detalle del Pago"));
        layout.add(new Paragraph("Instrument Number: " + detalleResponse.getInstrumentNumber()));
        layout.add(new Paragraph("Instrument Date: " + (detalleResponse.getInstrumentDate() != null ? detalleResponse.getInstrumentDate().toString() : "")));
        layout.add(new Paragraph("Banco: " + (detalleResponse.getBanco() != null ? detalleResponse.getBanco() : "N/A")));
        layout.add(new Paragraph("Pago Realizado: " + (detalleResponse.isPagoRealizado() ? "Sí" : "No")));

        Button closeButton = new Button("Cerrar", e -> dialog.close());
        layout.add(closeButton);

        dialog.add(layout);
        dialog.open();
    }

    // ==================== PAGINACIÓN ====================
    private HorizontalLayout createPaginationBar() {
        Button prevButton = new Button(VaadinIcon.ANGLE_LEFT.create(), e -> {
            if (currentPage > 0) {
                currentPage--;
                loadPagos();
            }
        });
        Button nextButton = new Button(VaadinIcon.ANGLE_RIGHT.create(), e -> {
            if (currentPage < totalPages - 1) {
                currentPage++;
                loadPagos();
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
        formLayout.setVisible(authService.isAdmin());

        H3 titulo = new H3("Crear Pago");
        formFecha = new DatePicker("Fecha de pago");
        formFecha.setWidthFull();
        formMonto = new NumberField("Monto");
        formMonto.setWidthFull();
        formMonto.setStep(0.01);
        formModoPago = new TextField("Modo de pago");
        formModoPago.setWidthFull();

        // ComboBox para Tercero
        formTerceroCombo = new ComboBox<>("Tercero");
        formTerceroCombo.setWidthFull();
        formTerceroCombo.setItemLabelGenerator(t ->
                (t.getCuitl() != null ? t.getCuitl() : "") + " - " +
                        (t.getNombre() != null ? t.getNombre() : ""));
        formTerceroCombo.setPlaceholder("Seleccione un tercero");

        // Detalle
        H3 detalleTitulo = new H3("Detalle del Pago");
        formInstrumentNumber = new TextField("Instrument Number");
        formInstrumentNumber.setWidthFull();
        formInstrumentDate = new DatePicker("Instrument Date");
        formInstrumentDate.setWidthFull();
        formBanco = new TextField("Banco");
        formBanco.setWidthFull();
        formPagoRealizado = new Checkbox("Pago Realizado");

        btnCrear = new Button("Crear Pago", VaadinIcon.PLUS.create());
        btnBorrar = new Button("Borrar Pago", VaadinIcon.TRASH.create());

        btnCrear.addClickListener(e -> crearPago());
        btnBorrar.addClickListener(e -> borrarPago());

        HorizontalLayout mainButtons = new HorizontalLayout(btnCrear, btnBorrar);

        formLayout.add(titulo, formFecha, formMonto, formModoPago, formTerceroCombo,
                detalleTitulo, formInstrumentNumber, formInstrumentDate, formBanco, formPagoRealizado,
                mainButtons);
        enableFormButtons(false);
    }

    private void loadTerceros() {
        try {
            tercerosList = terceroService.findAllSimple();
            formTerceroCombo.setItems(tercerosList);
        } catch (Exception e) {
            Notification.show("Error al cargar terceros: " + e.getMessage(), 3000, Notification.Position.MIDDLE);
        }
    }

    private void loadPagoToForm(CompletePagoResponse pago) {
        // Datos principales
        if (pago.getFechaPago() != null) {
            formFecha.setValue(pago.getFechaPago().toInstant()
                    .atZone(ZoneId.systemDefault()).toLocalDate());
        } else {
            formFecha.clear();
        }
        formMonto.setValue(pago.getMontoPago());
        formModoPago.setValue(pago.getModoPago() != null ? pago.getModoPago() : "");

        if (pago.getIdTercero() != null) {
            TerceroResponse tercero = tercerosList.stream()
                    .filter(t -> t.getIdTercero().equals(pago.getIdTercero()))
                    .findFirst()
                    .orElse(null);
            formTerceroCombo.setValue(tercero);
        } else {
            formTerceroCombo.clear();
        }

        // Detalle
        PagoDetalleResponse detalle = pago.getDetalleResponse();
        if (detalle != null) {
            formInstrumentNumber.setValue(detalle.getInstrumentNumber() != null ? detalle.getInstrumentNumber() : "");
            if (detalle.getInstrumentDate() != null) {
                formInstrumentDate.setValue(detalle.getInstrumentDate().toInstant()
                        .atZone(ZoneId.systemDefault()).toLocalDate());
            } else {
                formInstrumentDate.clear();
            }
            formBanco.setValue(detalle.getBanco() != null ? detalle.getBanco() : "");
            formPagoRealizado.setValue(detalle.isPagoRealizado());
        } else {
            formInstrumentNumber.clear();
            formInstrumentDate.clear();
            formBanco.clear();
            formPagoRealizado.setValue(false);
        }

        // Modo solo lectura
        setFormReadOnly(true);
        btnCrear.setEnabled(false);
        btnBorrar.setEnabled(true);
    }

    private void clearForm() {
        formFecha.clear();
        formMonto.clear();
        formModoPago.clear();
        formTerceroCombo.clear();
        formInstrumentNumber.clear();
        formInstrumentDate.clear();
        formBanco.clear();
        formPagoRealizado.setValue(false);
        setFormReadOnly(false);
        btnCrear.setEnabled(true);
        btnBorrar.setEnabled(false);
        selectedPago = null;
    }

    private void setFormReadOnly(boolean readOnly) {
        formFecha.setReadOnly(readOnly);
        formMonto.setReadOnly(readOnly);
        formModoPago.setReadOnly(readOnly);
        formTerceroCombo.setReadOnly(readOnly);
        formInstrumentNumber.setReadOnly(readOnly);
        formInstrumentDate.setReadOnly(readOnly);
        formBanco.setReadOnly(readOnly);
        formPagoRealizado.setReadOnly(readOnly);
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
    private void loadPagos() {
        String cuit = filterCuit.getValue();
        String modoPago = filterModoPago.getValue();
        LocalDate localDate = filterFecha.getValue();
        Date fecha = localDate != null ? Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant()) : null;

        int pageSize = 5;
        PageResponse<CompletePagoResponse> page = pagoService.findAll(cuit, modoPago, fecha, currentPage, pageSize);
        grid.setItems(page.getContent());
        totalPages = page.getTotalPages();
        if (currentPage >= totalPages && totalPages > 0) {
            currentPage = totalPages - 1;
            loadPagos();
        }
    }

    // ==================== OPERACIONES CRUD ====================
    private void crearPago() {
        // Validar campos
        LocalDate fechaLocal = formFecha.getValue();
        if (fechaLocal == null) {
            Notification.show("La fecha de pago es obligatoria");
            return;
        }
        Double monto = formMonto.getValue();
        if (monto == null || monto <= 0) {
            Notification.show("El monto debe ser mayor a 0");
            return;
        }
        String modoPago = formModoPago.getValue();
        if (modoPago == null || modoPago.trim().isEmpty()) {
            Notification.show("El modo de pago es obligatorio");
            return;
        }
        TerceroResponse tercero = formTerceroCombo.getValue();
        if (tercero == null) {
            Notification.show("Debe seleccionar un tercero");
            return;
        }

        // Detalle
        String instrumentNumber = formInstrumentNumber.getValue();
        if (instrumentNumber == null || instrumentNumber.trim().isEmpty()) {
            Notification.show("El instrument number es obligatorio");
            return;
        }
        LocalDate instrumentDateLocal = formInstrumentDate.getValue();
        if (instrumentDateLocal == null) {
            Notification.show("La instrument date es obligatoria");
            return;
        }

        // Construir request
        AddPagoRequest request = new AddPagoRequest();
        request.setId_tercero(tercero.getIdTercero());
        request.setFecha_pago(Date.from(fechaLocal.atStartOfDay(ZoneId.systemDefault()).toInstant()));
        request.setMonto_pago(monto);
        request.setModo_pago(modoPago);

        AddPagoDetalleRequest detalleRequest = new AddPagoDetalleRequest();
        detalleRequest.setInstrumentNumber(instrumentNumber);
        detalleRequest.setInstrumentDate(Date.from(instrumentDateLocal.atStartOfDay(ZoneId.systemDefault()).toInstant()));
        detalleRequest.setBanco(formBanco.getValue());
        detalleRequest.setPagoRealizado(formPagoRealizado.getValue());
        request.setDetalleRequest(detalleRequest);

        try {
            CompletePagoResponse created = pagoService.create(request);
            Notification.show("Pago creado exitosamente (ID: " + created.getIdPago() + ")", 3000, Notification.Position.MIDDLE);
            clearForm();
            loadPagos();
        } catch (Exception ex) {
            Notification.show("Error al crear: " + ex.getMessage(), 5000, Notification.Position.MIDDLE);
        }
    }

    private void borrarPago() {
        if (selectedPago == null) return;
        try {
            pagoService.delete(selectedPago.getIdPago());
            Notification.show("Pago eliminado correctamente", 3000, Notification.Position.MIDDLE);
            clearForm();
            loadPagos();
        } catch (Exception ex) {
            Notification.show("Error al borrar: " + ex.getMessage(), 5000, Notification.Position.MIDDLE);
        }
    }
}