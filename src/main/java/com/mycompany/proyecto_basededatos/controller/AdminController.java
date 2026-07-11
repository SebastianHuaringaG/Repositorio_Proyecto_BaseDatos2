package com.mycompany.proyecto_basededatos.controller;

import com.mycompany.proyecto_basededatos.model.Eleccion;
import com.mycompany.proyecto_basededatos.model.Entidad;
import com.mycompany.proyecto_basededatos.service.IVotacionService;
import com.mycompany.proyecto_basededatos.view.FrmAdmin;
import javax.swing.JOptionPane;
import java.awt.event.ActionEvent;
import java.sql.Timestamp;
import java.util.List;

public class AdminController {
    private final FrmAdmin view;
    private final IVotacionService service;

    public AdminController(FrmAdmin view, IVotacionService service) {
        this.view = view;
        this.service = service;

        // 1. Cargar datos en los ComboBox al iniciar la pantalla
        cargarEntidadesEnVista();
        cargarEleccionesEnVista();

        // 2. Asignar eventos a los botones de la FASE 1 (Preparación)
        this.view.btnNuevaEntidad.addActionListener((ActionEvent e) -> registrarNuevaEntidad());
        this.view.btnCrearEleccion.addActionListener((ActionEvent e) -> crearEleccion());
        this.view.btnRegistrarCandidato.addActionListener((ActionEvent e) -> registrarCandidato());
        this.view.btnCargarPadron.addActionListener((ActionEvent e) -> cargarPadron());

        // 3. Asignar eventos a los botones de la FASE 3 (Control y Resultados)
        this.view.btnApertura.addActionListener((ActionEvent e) -> actualizarEstado("EN CURSO"));
        this.view.btnCierre.addActionListener((ActionEvent e) -> actualizarEstado("FINALIZADA"));
        this.view.btnEscrutinio.addActionListener((ActionEvent e) -> verResultados());
    }

    public void iniciar() {
        view.setVisible(true);
    }

    // ==========================================
    // MÉTODOS DE CARGA DINÁMICA
    // ==========================================

    private void cargarEntidadesEnVista() {
        try {
            List<Entidad> lista = service.listarEntidades();
            view.cmbEntidades.removeAllItems();
            for (Entidad e : lista) {
                view.cmbEntidades.addItem(e);
            }
        } catch (Exception ex) {
            System.err.println("Error al cargar entidades: " + ex.getMessage());
        }
    }

    private void cargarEleccionesEnVista() {
        try {
            List<Eleccion> lista = service.listarElecciones();
            view.cmbEleccionesCandidato.removeAllItems();
            view.cmbEleccionesPadron.removeAllItems();
            view.cmbEleccionesControl.removeAllItems();
            
            for (Eleccion e : lista) {
                // Llena los 3 combos de elecciones en las distintas pestañas
                view.cmbEleccionesCandidato.addItem(e);
                view.cmbEleccionesPadron.addItem(e);
                view.cmbEleccionesControl.addItem(e);
            }
        } catch (Exception ex) {
            System.err.println("Error al cargar elecciones: " + ex.getMessage());
        }
    }

    // ==========================================
    // LÓGICA DE FASE 1
    // ==========================================

    private void registrarNuevaEntidad() {
        String nombre = JOptionPane.showInputDialog(view, "Ingrese el nombre de la nueva institución:");
        if (nombre != null && !nombre.trim().isEmpty()) {
            String tipo = JOptionPane.showInputDialog(view, "Ingrese el tipo (ej. UNIVERSIDAD, MUNICIPIO, COLEGIO):");
            if (tipo != null && !tipo.trim().isEmpty()) {
                try {
                    service.registrarNuevaEntidad(nombre.toUpperCase(), tipo.toUpperCase());
                    JOptionPane.showMessageDialog(view, "Institución registrada con éxito.");
                    cargarEntidadesEnVista(); // Refresca el ComboBox en tiempo real
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(view, "Error: " + ex.getMessage(), "Fallo", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private void crearEleccion() {
        try {
            Entidad entidadSeleccionada = (Entidad) view.cmbEntidades.getSelectedItem();
            if (entidadSeleccionada == null) {
                JOptionPane.showMessageDialog(view, "Debe seleccionar un Ente Electoral Cliente.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String nombre = view.txtNombreEleccion.getText();
            // Formatear para Oracle Timestamp
            Timestamp inicio = Timestamp.valueOf(view.txtFechaInicio.getText() + ":00");
            Timestamp fin = Timestamp.valueOf(view.txtFechaFin.getText() + ":00");
            
            service.prepararEleccion(entidadSeleccionada.getEntityId(), nombre, inicio, fin);
            JOptionPane.showMessageDialog(view, "Elección generada exitosamente para: " + entidadSeleccionada.getEntityName());
            
            // Limpiamos la caja y refrescamos combos
            view.txtNombreEleccion.setText("");
            cargarEleccionesEnVista(); 
            
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(view, "Error de formato de fecha. Use estrictamente: YYYY-MM-DD HH:MI", "Error de Validación", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(view, "Error BD: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void registrarCandidato() {
        try {
            Eleccion elec = (Eleccion) view.cmbEleccionesCandidato.getSelectedItem();
            if (elec == null) {
                JOptionPane.showMessageDialog(view, "Seleccione a qué elección pertenece este candidato.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            service.agregarCandidato(elec.getElectionId(), view.txtNombreCandidato.getText(), view.txtListaCandidato.getText());
            JOptionPane.showMessageDialog(view, "Candidato asignado a " + elec.getElectionName());
            
            // Limpiar cajas de texto para facilitar múltiples registros
            view.txtNombreCandidato.setText("");
            view.txtListaCandidato.setText("");
            view.txtNombreCandidato.requestFocus();
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(view, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cargarPadron() {
        try {
            Eleccion elec = (Eleccion) view.cmbEleccionesPadron.getSelectedItem();
            if (elec == null) {
                JOptionPane.showMessageDialog(view, "Seleccione la elección para asignar este elector.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            service.cargarPadronelector(elec.getElectionId(), view.txtDniElector.getText(), view.txtNombreElector.getText(), view.txtPassElector.getText());
            JOptionPane.showMessageDialog(view, "Elector guardado en el padrón de " + elec.getElectionName());
            
            // Limpiar cajas de texto
            view.txtDniElector.setText("");
            view.txtNombreElector.setText("");
            view.txtPassElector.setText("");
            view.txtDniElector.requestFocus();
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(view, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ==========================================
    // LÓGICA DE FASE 3
    // ==========================================

    private void actualizarEstado(String estado) {
        try {
            Eleccion elec = (Eleccion) view.cmbEleccionesControl.getSelectedItem();
            if (elec == null) {
                JOptionPane.showMessageDialog(view, "Seleccione una elección a modificar.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            service.gestionarEstadoEleccion(elec.getElectionId(), estado);
            JOptionPane.showMessageDialog(view, "La elección '" + elec.getElectionName() + "' ahora está: " + estado);
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(view, "Error al actualizar estado: " + ex.getMessage(), "Fallo", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void verResultados() {
        try {
            Eleccion elec = (Eleccion) view.cmbEleccionesControl.getSelectedItem();
            if (elec == null) {
                JOptionPane.showMessageDialog(view, "Seleccione una elección para procesar el escrutinio.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            String resultados = service.generarReporte(elec.getElectionId());
            view.txtReporteFinal.setText("REPORTE OFICIAL PARA: " + elec.getElectionName() + "\n\n" + resultados);
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(view, "Error al generar el escrutinio: " + ex.getMessage(), "Fallo", JOptionPane.ERROR_MESSAGE);
        }
    }
}