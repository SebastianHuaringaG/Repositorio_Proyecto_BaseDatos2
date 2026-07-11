package com.mycompany.proyecto_basededatos.controller;

import com.mycompany.proyecto_basededatos.model.Candidato;
import com.mycompany.proyecto_basededatos.model.Elector;
import com.mycompany.proyecto_basededatos.service.IVotacionService;
import com.mycompany.proyecto_basededatos.view.FrmVotacion;
import javax.swing.JOptionPane;
import java.awt.event.ActionEvent;
import java.util.List;

public class VotacionController {
    private final FrmVotacion view;
    private final IVotacionService service;
    private final Elector elector;

    public VotacionController(FrmVotacion view, IVotacionService service, Elector elector) {
        this.view = view;
        this.service = service;
        this.elector = elector;
        
        this.view.lblBienvenida.setText("Elector: " + elector.getFullName());
        
        try {
            // Consulta a la BD solo los candidatos del evento del votante
            List<Candidato> lista = service.listarCandidatos(elector.getElectionId());
            this.view.cargarCandidatos(lista);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(view, "Error cargando candidatos: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        this.view.btnVotar.addActionListener((ActionEvent e) -> emitirVoto());
    }

    // ==========================================
    // ESTE ES EL MÉTODO QUE FALTABA
    // ==========================================
    public void iniciar() {
        view.setVisible(true);
    }

    private void emitirVoto() {
        if (view.grupoVoto.getSelection() == null) {
            JOptionPane.showMessageDialog(view, "Por favor, seleccione un candidato.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Extraemos el ID del ActionCommand que guardamos en la vista
        int candidateId = Integer.parseInt(view.grupoVoto.getSelection().getActionCommand());

        try {
            // Emite el voto asegurándose de que va a SU elección
            service.emitirVoto(elector.getVoterId(), candidateId, elector.getElectionId());
            JOptionPane.showMessageDialog(view, "Su voto ha sido procesado exitosamente de manera segura y anónima.", "Transacción OK", JOptionPane.INFORMATION_MESSAGE);
            view.dispose();
            System.exit(0); // Cierra la aplicación tras emitir el voto (opcional)
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(view, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}