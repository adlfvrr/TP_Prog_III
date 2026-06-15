package com.utn.tp.prog3.ui.views;

import com.utn.tp.prog3.backend.dto.request.AddTerceroRequest;
import com.utn.tp.prog3.backend.dto.request.UpdateTerceroRequest;
import com.utn.tp.prog3.backend.dto.response.TerceroResponse;
import com.utn.tp.prog3.ui.client.service.AuthService;
import com.utn.tp.prog3.ui.client.service.TerceroService;
import com.utn.tp.prog3.ui.dto.PageResponse;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "terceros", layout = MainLayout.class)
@PageTitle("Terceros")
public class TercerosView extends VerticalLayout {

        private final TerceroService terceroService;
        private final AuthService authService;

        // Componentes del grid y paginación
        private Grid<TerceroResponse> grid;
        private int currentPage = 0;
    private int totalPages = 0;

        // Filtros
        private TextField filterNombre;
        private TextField filterCuit;
        private ComboBox<String> filterSitIVA;
        private TextField filterDireccion;
        private TextField filterLocalidad;
        private TextField filterProvincia;
        private TextField filterTelefono;
        private TextField filterTipoSaldo;

        // Formulario (solo ADMIN puede editarlo)
        private VerticalLayout formLayout;
        private TextField formNombre;
        private TextField formCuit;
        private ComboBox<String> formSitIVA;
        private TextField formDireccion;
        private TextField formLocalidad;
        private TextField formProvincia;
        private TextField formTelefono;
        private NumberField formSaldoApertura;
        private TextField formTipoSaldo;

        private Button btnCrear;
        private Button btnActualizar;
        private Button btnBorrar;

        private TerceroResponse selectedTercero = null;

    public TercerosView(TerceroService terceroService, AuthService authService) {
            this.terceroService = terceroService;
            this.authService = authService;

            setSizeFull();
            setPadding(true);
            setSpacing(true);

            // Crear componentes
            createFilters();
            createGrid();
            createForm();

            // Layout principal: dos columnas (60% grid, 40% formulario)
            HorizontalLayout mainLayout = new HorizontalLayout();
            mainLayout.setSizeFull();
            mainLayout.setSpacing(true);

            VerticalLayout gridSection = new VerticalLayout();
            gridSection.setWidth("60%");
            gridSection.add(createFilterBar(), grid, createPaginationBar());

            formLayout.setWidth("40%");
            mainLayout.add(gridSection, formLayout);
            add(mainLayout);

            // Cargar primera página
            loadTerceros();
        }

        private void createFilters () {
            filterNombre = new TextField("Nombre");
            filterNombre.setPlaceholder("Filtrar por nombre");
            filterNombre.setValueChangeMode(ValueChangeMode.LAZY);
            filterNombre.addValueChangeListener(e -> loadTerceros());

            filterCuit = new TextField("CUIT");
            filterCuit.setPlaceholder("Filtrar por CUIT");
            filterCuit.addValueChangeListener(e -> loadTerceros());

            filterSitIVA = new ComboBox<>("Situación IVA");
            filterSitIVA.setItems("Responsable Inscripto", "Consumidor Final", "Exento", "Monotributista");
            filterSitIVA.setClearButtonVisible(true);
            filterSitIVA.addValueChangeListener(e -> loadTerceros());

            filterDireccion = new TextField("Dirección");
            filterDireccion.addValueChangeListener(e -> loadTerceros());

            filterLocalidad = new TextField("Localidad");
            filterLocalidad.addValueChangeListener(e -> loadTerceros());

            filterProvincia = new TextField("Provincia");
            filterProvincia.addValueChangeListener(e -> loadTerceros());

            filterTelefono = new TextField("Teléfono");
            filterTelefono.addValueChangeListener(e -> loadTerceros());

            filterTipoSaldo = new TextField("Tipo Saldo");
            filterTipoSaldo.addValueChangeListener(e -> loadTerceros());
        }

        private HorizontalLayout createFilterBar () {
            HorizontalLayout bar = new HorizontalLayout();
            bar.setWrap(true);
            bar.setAlignItems(Alignment.END);
            bar.add(filterNombre, filterCuit, filterSitIVA, filterDireccion,
                    filterLocalidad, filterProvincia, filterTelefono, filterTipoSaldo);
            return bar;
        }

        private void createGrid () {
            grid = new Grid<>(TerceroResponse.class);
            grid.setColumns("idTercero", "nombre", "cuitl", "sitIVA", "direccion",
                    "localidad", "provincia", "telefono", "saldo_apertura", "tipo_saldo");
            grid.getColumnByKey("idTercero").setHeader("ID").setWidth("60px");
            grid.getColumnByKey("saldo_apertura").setHeader("Saldo apertura");
            grid.setWidthFull();
            grid.setHeight("400px");

            // Al seleccionar fila, cargar en formulario
            grid.asSingleSelect().addValueChangeListener(event -> {
                selectedTercero = event.getValue();
                if (selectedTercero != null) {
                    loadTerceroToForm(selectedTercero);
                    enableFormButtons(true);
                } else {
                    clearForm();
                    enableFormButtons(false);
                }
            });
        }

        private HorizontalLayout createPaginationBar () {
            Button prevButton = new Button(VaadinIcon.ANGLE_LEFT.create(), e -> {
                if (currentPage > 0) {
                    currentPage--;
                    loadTerceros();
                }
            });
            Button nextButton = new Button(VaadinIcon.ANGLE_RIGHT.create(), e -> {
                if (currentPage < totalPages - 1) {
                    currentPage++;
                    loadTerceros();
                }
            });
            HorizontalLayout bar = new HorizontalLayout(prevButton, nextButton);
            bar.setAlignItems(Alignment.CENTER);
            return bar;
        }

        private void createForm () {
            formLayout = new VerticalLayout();
            formLayout.setPadding(true);
            formLayout.setSpacing(true);
            formLayout.setVisible(authService.isAdmin()); // Solo visible para ADMIN

            H3 titulo = new H3("Formulario de Tercero");
            formNombre = new TextField("Nombre");
            formCuit = new TextField("CUIT");
            formSitIVA = new ComboBox<>("Situación IVA");
            formSitIVA.setItems("Responsable Inscripto", "Consumidor Final", "Exento", "Monotributista");
            formDireccion = new TextField("Dirección");
            formLocalidad = new TextField("Localidad");
            formProvincia = new TextField("Provincia");
            formTelefono = new TextField("Teléfono");
            formSaldoApertura = new NumberField("Saldo apertura");
            formSaldoApertura.setStep(0.01);
            formTipoSaldo = new TextField("Tipo saldo");

            btnCrear = new Button("Crear", VaadinIcon.PLUS.create());
            btnActualizar = new Button("Actualizar", VaadinIcon.REFRESH.create());
            btnBorrar = new Button("Borrar", VaadinIcon.TRASH.create());

            btnCrear.addClickListener(e -> crearTercero());
            btnActualizar.addClickListener(e -> actualizarTercero());
            btnBorrar.addClickListener(e -> borrarTercero());

            HorizontalLayout buttons = new HorizontalLayout(btnCrear, btnActualizar, btnBorrar);
            formLayout.add(titulo, formNombre, formCuit, formSitIVA, formDireccion,
                    formLocalidad, formProvincia, formTelefono, formSaldoApertura, formTipoSaldo, buttons);
            enableFormButtons(false);
        }

        private void loadTerceroToForm (TerceroResponse t){
            formNombre.setValue(t.getNombre() != null ? t.getNombre() : "");
            formCuit.setValue(t.getCuitl() != null ? t.getCuitl() : "");
            formSitIVA.setValue(t.getSitIVA() != null ? t.getSitIVA() : "");
            formDireccion.setValue(t.getDireccion() != null ? t.getDireccion() : "");
            formLocalidad.setValue(t.getLocalidad() != null ? t.getLocalidad() : "");
            formProvincia.setValue(t.getProvincia() != null ? t.getProvincia() : "");
            formTelefono.setValue(t.getTelefono() != null ? t.getTelefono() : "");
            formSaldoApertura.setValue(t.getSaldo_apertura());
            formTipoSaldo.setValue(t.getTipo_saldo() != null ? t.getTipo_saldo() : "");
        }

        private void clearForm () {
            formNombre.clear();
            formCuit.clear();
            formSitIVA.clear();
            formDireccion.clear();
            formLocalidad.clear();
            formProvincia.clear();
            formTelefono.clear();
            formSaldoApertura.clear();
            formTipoSaldo.clear();
            selectedTercero = null;
        }

        private void enableFormButtons ( boolean hasSelection){
            btnCrear.setEnabled(true); // siempre puede crear
            btnActualizar.setEnabled(hasSelection);
            btnBorrar.setEnabled(hasSelection);
        }

        private void loadTerceros () {
            String nombre = filterNombre.getValue();
            String cuit = filterCuit.getValue();
            String sitIVA = filterSitIVA.getValue();
            String direccion = filterDireccion.getValue();
            String localidad = filterLocalidad.getValue();
            String provincia = filterProvincia.getValue();
            String telefono = filterTelefono.getValue();
            String tipoSaldo = filterTipoSaldo.getValue();

            int pageSize = 5;
            PageResponse<TerceroResponse> page = terceroService.findAll(nombre, cuit, sitIVA,
                    direccion, localidad, provincia, telefono, tipoSaldo,
                    currentPage, pageSize);
            grid.setItems(page.getContent());
            totalPages = page.getTotalPages();
            if (currentPage >= totalPages && totalPages > 0) {
                currentPage = totalPages - 1;
                loadTerceros(); // recursivo, pero solo una vez
            }
        }

        private void crearTercero () {
            AddTerceroRequest request = new AddTerceroRequest();
            request.setNombre(formNombre.getValue());
            request.setCuitl(formCuit.getValue());
            request.setSitIVA(formSitIVA.getValue());
            request.setDireccion(formDireccion.getValue());
            request.setLocalidad(formLocalidad.getValue());
            request.setProvincia(formProvincia.getValue());
            request.setTelefono(formTelefono.getValue());
            request.setSaldo_apertura(formSaldoApertura.getValue());
            request.setTipo_saldo(formTipoSaldo.getValue());

            try {
                terceroService.create(request);
                Notification.show("Creado exitosamente");
                clearForm();
                loadTerceros();
            } catch (Exception ex) {
                Notification.show("Error al crear: " + ex.getMessage());
            }
        }

        private void actualizarTercero () {
            if (selectedTercero == null) return;
            UpdateTerceroRequest request = new UpdateTerceroRequest();
            request.setNombre(formNombre.getValue());
            request.setCuitl(formCuit.getValue());
            request.setSitIVA(formSitIVA.getValue());
            request.setDireccion(formDireccion.getValue());
            request.setLocalidad(formLocalidad.getValue());
            request.setProvincia(formProvincia.getValue());
            request.setTelefono(formTelefono.getValue());
            request.setSaldo_apertura(formSaldoApertura.getValue());
            request.setTipo_saldo(formTipoSaldo.getValue());

            try {
                terceroService.update(selectedTercero.getIdTercero(), request);
                Notification.show("Actualizado correctamente");
                loadTerceros();
                clearForm();
            } catch (Exception ex) {
                Notification.show("Error al actualizar: " + ex.getMessage());
            }
        }

        private void borrarTercero () {
            if (selectedTercero == null) return;
            try {
                terceroService.delete(selectedTercero.getIdTercero());
                Notification.show("Eliminado correctamente");
                clearForm();
                loadTerceros();
            } catch (Exception ex) {
                Notification.show("Error al borrar: " + ex.getMessage());
            }
        }


    }
