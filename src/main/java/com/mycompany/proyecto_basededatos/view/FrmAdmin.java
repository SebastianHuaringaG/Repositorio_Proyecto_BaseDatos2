package com.mycompany.proyecto_basededatos.view;

import com.mycompany.proyecto_basededatos.model.Eleccion;
import com.mycompany.proyecto_basededatos.model.Entidad;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class FrmAdmin extends JFrame {
    
    public JComboBox<Entidad> cmbEntidades;
    public JButton btnNuevaEntidad;
    public JTextField txtNombreEleccion, txtFechaInicio, txtFechaFin;
    public JButton btnCrearEleccion;
    
    public JComboBox<Eleccion> cmbEleccionesCandidato;
    public JTextField txtNombreCandidato, txtListaCandidato;
    public JButton btnRegistrarCandidato;
    
    public JComboBox<Eleccion> cmbEleccionesPadron;
    public JTextField txtDniElector, txtNombreElector, txtPassElector;
    public JButton btnCargarPadron;

    public JComboBox<Eleccion> cmbEleccionesControl;
    public JButton btnApertura, btnCierre, btnEscrutinio;
    public JTextArea txtReporteFinal;

    public FrmAdmin() {
        setTitle("Panel de Administración Electoral");
        setSize(900, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 14));
        tabbedPane.setBackground(Color.WHITE);

        // ==========================================
        // PESTAÑA: PREPARACIÓN
        // ==========================================
        JPanel panelPrep = new JPanel(new GridLayout(3, 1, 15, 15));
        panelPrep.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        // 1. Configuración de Evento
        JPanel pnlEleccion = new JPanel(new GridLayout(3, 4, 10, 10));
        pnlEleccion.setBorder(crearBorde("Configuración de Entidad y Evento Electoral"));
        pnlEleccion.add(new JLabel("Ente Cliente:")); cmbEntidades = new JComboBox<>(); pnlEleccion.add(cmbEntidades);
        
        btnNuevaEntidad = new JButton("+ Añadir Nuevo Ente"); 
        estilizarBoton(btnNuevaEntidad, new Color(108, 117, 125)); // Gris Oscuro
        pnlEleccion.add(btnNuevaEntidad); 
        pnlEleccion.add(new JLabel(""));
        
        pnlEleccion.add(new JLabel("Nombre del Evento:")); txtNombreEleccion = new JTextField(); pnlEleccion.add(txtNombreEleccion);
        pnlEleccion.add(new JLabel("Inicio (YYYY-MM-DD HH:MI):")); txtFechaInicio = new JTextField("2026-10-01 08:00"); pnlEleccion.add(txtFechaInicio);
        pnlEleccion.add(new JLabel("Fin (YYYY-MM-DD HH:MI):")); txtFechaFin = new JTextField("2026-10-01 16:00"); pnlEleccion.add(txtFechaFin);
        
        btnCrearEleccion = new JButton("Guardar Elección"); 
        estilizarBoton(btnCrearEleccion, new Color(13, 110, 253)); // Azul Primario
        pnlEleccion.add(btnCrearEleccion);
        panelPrep.add(pnlEleccion);

        // 2. Registro de Candidatos
        JPanel pnlCandidato = new JPanel(new GridLayout(2, 4, 10, 10));
        pnlCandidato.setBorder(crearBorde("Inscripción de Candidatos Oficiales"));
        pnlCandidato.add(new JLabel("Seleccione Evento:")); cmbEleccionesCandidato = new JComboBox<>(); pnlCandidato.add(cmbEleccionesCandidato);
        pnlCandidato.add(new JLabel("Nombre Completo:")); txtNombreCandidato = new JTextField(); pnlCandidato.add(txtNombreCandidato);
        pnlCandidato.add(new JLabel("Lista / Partido:")); txtListaCandidato = new JTextField(); pnlCandidato.add(txtListaCandidato);
        
        btnRegistrarCandidato = new JButton("Añadir Candidato"); 
        estilizarBoton(btnRegistrarCandidato, new Color(13, 110, 253)); 
        pnlCandidato.add(btnRegistrarCandidato);
        panelPrep.add(pnlCandidato);

        // 3. Padrón Electoral
        JPanel pnlPadron = new JPanel(new GridLayout(2, 4, 10, 10));
        pnlPadron.setBorder(crearBorde("Registro de Padrón Electoral"));
        pnlPadron.add(new JLabel("Seleccione Evento:")); cmbEleccionesPadron = new JComboBox<>(); pnlPadron.add(cmbEleccionesPadron);
        pnlPadron.add(new JLabel("DNI:")); txtDniElector = new JTextField(); pnlPadron.add(txtDniElector);
        pnlPadron.add(new JLabel("Nombre Completo:")); txtNombreElector = new JTextField(); pnlPadron.add(txtNombreElector);
        pnlPadron.add(new JLabel("Contraseña:")); txtPassElector = new JTextField(); pnlPadron.add(txtPassElector);
        
        btnCargarPadron = new JButton("Registrar Elector"); 
        estilizarBoton(btnCargarPadron, new Color(13, 110, 253));
        pnlPadron.add(btnCargarPadron);
        panelPrep.add(pnlPadron);

        tabbedPane.addTab("Preparación y Configuración", panelPrep);

        // ==========================================
        // PESTAÑA: CONTROL Y RESULTADOS
        // ==========================================
        JPanel panelControl = new JPanel(new BorderLayout(15, 15));
        panelControl.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JPanel pnlControles = new JPanel(new GridLayout(2, 2, 15, 15));
        pnlControles.add(new JLabel("Seleccione la Elección a Gestionar:", SwingConstants.RIGHT));
        cmbEleccionesControl = new JComboBox<>(); pnlControles.add(cmbEleccionesControl);
        
        btnApertura = new JButton("Habilitar Accesos (EN CURSO)"); 
        estilizarBoton(btnApertura, new Color(40, 167, 69)); // Verde Éxito
        
        btnCierre = new JButton("Bloquear Accesos (FINALIZADA)"); 
        estilizarBoton(btnCierre, new Color(220, 53, 69)); // Rojo Peligro
        
        btnEscrutinio = new JButton("Procesar Escrutinio Oficial"); 
        estilizarBoton(btnEscrutinio, new Color(0, 123, 255)); // Azul Info

        pnlControles.add(btnApertura); 
        pnlControles.add(btnCierre);
        
        txtReporteFinal = new JTextArea(); 
        txtReporteFinal.setEditable(false); 
        txtReporteFinal.setFont(new Font("Monospaced", Font.PLAIN, 16));
        txtReporteFinal.setBackground(new Color(245, 245, 245));
        
        JPanel pnlCentro = new JPanel(new BorderLayout(0, 10));
        pnlCentro.add(btnEscrutinio, BorderLayout.NORTH);
        pnlCentro.add(new JScrollPane(txtReporteFinal), BorderLayout.CENTER);

        panelControl.add(pnlControles, BorderLayout.NORTH);
        panelControl.add(pnlCentro, BorderLayout.CENTER);

        tabbedPane.addTab("Control de Evento y Resultados", panelControl);
        
        add(tabbedPane);
    }

    // Método centralizado para generar botones "Flat" consistentes
    private void estilizarBoton(JButton btn, Color bgColor) {
        btn.setBackground(bgColor);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false); // Esta es la regla de oro para Windows
        btn.setOpaque(true);         // Forza a pintar el color base
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private TitledBorder crearBorde(String titulo) {
        return BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.LIGHT_GRAY), 
            titulo, 
            TitledBorder.DEFAULT_JUSTIFICATION, 
            TitledBorder.DEFAULT_POSITION, 
            new Font("Segoe UI", Font.BOLD, 13), 
            new Color(0, 51, 153)
        );
    }
}