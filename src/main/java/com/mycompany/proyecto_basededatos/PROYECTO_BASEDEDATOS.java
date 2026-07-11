package com.mycompany.proyecto_basededatos;

import com.mycompany.proyecto_basededatos.controller.AdminController;
import com.mycompany.proyecto_basededatos.controller.LoginController;
import com.mycompany.proyecto_basededatos.dao.ISistemaDAO;
import com.mycompany.proyecto_basededatos.dao.impl.SistemaDAOImpl;
import com.mycompany.proyecto_basededatos.service.IVotacionService;
import com.mycompany.proyecto_basededatos.service.impl.VotacionServiceImpl;
import com.mycompany.proyecto_basededatos.view.FrmAdmin;
import com.mycompany.proyecto_basededatos.view.FrmLogin;

import javax.swing.*;
import java.awt.*;

public class PROYECTO_BASEDEDATOS {

    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception e) {}

        ISistemaDAO dao = new SistemaDAOImpl();
        IVotacionService service = new VotacionServiceImpl(dao);

        JFrame menu = new JFrame("Sistema de Votación Electrónica");
        menu.setSize(380, 220);
        menu.setLocationRelativeTo(null);
        menu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        menu.setLayout(new BorderLayout());

        JLabel lblTitulo = new JLabel("SELECCIONE SU PERFIL", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        menu.add(lblTitulo, BorderLayout.NORTH);

        JPanel pnlBotones = new JPanel(new GridLayout(2, 1, 15, 15));
        pnlBotones.setBorder(BorderFactory.createEmptyBorder(0, 30, 30, 30));

        // Botón Elector Estilizado
        JButton btnElector = new JButton("Ingresar como ELECTOR");
        btnElector.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnElector.setBackground(new Color(0, 102, 204));
        btnElector.setForeground(Color.WHITE);
        btnElector.setFocusPainted(false);
        btnElector.setBorderPainted(false); // Solución al fondo fantasma
        btnElector.setOpaque(true);
        btnElector.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Botón Admin Estilizado
        JButton btnAdmin = new JButton("Ingresar como ADMINISTRADOR");
        btnAdmin.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnAdmin.setBackground(new Color(108, 117, 125));
        btnAdmin.setForeground(Color.WHITE);
        btnAdmin.setFocusPainted(false);
        btnAdmin.setBorderPainted(false); // Solución al fondo fantasma
        btnAdmin.setOpaque(true);
        btnAdmin.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnElector.addActionListener(e -> {
            LoginController loginCtrl = new LoginController(new FrmLogin(), service);
            loginCtrl.iniciar();
        });

        btnAdmin.addActionListener(e -> {
            AdminController adminCtrl = new AdminController(new FrmAdmin(), service);
            adminCtrl.iniciar();
        });

        pnlBotones.add(btnElector);
        pnlBotones.add(btnAdmin);
        menu.add(pnlBotones, BorderLayout.CENTER);
        
        menu.setVisible(true);
    }
}