package com.mycompany.proyecto_basededatos.controller;

import com.mycompany.proyecto_basededatos.model.Elector;
import com.mycompany.proyecto_basededatos.service.IVotacionService;
import com.mycompany.proyecto_basededatos.view.FrmLogin;
import com.mycompany.proyecto_basededatos.view.FrmVotacion;
import javax.swing.JOptionPane;
import java.awt.event.ActionEvent;

public class LoginController {
    private final FrmLogin view;
    private final IVotacionService service;

    public LoginController(FrmLogin view, IVotacionService service) {
        this.view = view;
        this.service = service;
        this.view.btnIngresar.addActionListener((ActionEvent e) -> autenticar());
    }

    public void iniciar() {
        view.setVisible(true);
    }

    private void autenticar() {
        String dni = view.txtDni.getText();
        String pass = new String(view.txtPassword.getPassword());

        try {
            Elector elector = service.procesarLogin(dni, pass);
            view.dispose(); 

            FrmVotacion vistaVotacion = new FrmVotacion();
            VotacionController votacionCtrl = new VotacionController(vistaVotacion, service, elector);
            votacionCtrl.iniciar();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(view, ex.getMessage(), "Error de Acceso", JOptionPane.ERROR_MESSAGE);
        }
    }
}