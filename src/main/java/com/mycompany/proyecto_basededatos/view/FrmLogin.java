package com.mycompany.proyecto_basededatos.view;

import javax.swing.*;
import java.awt.*;

public class FrmLogin extends JFrame {
    public JTextField txtDni;
    public JPasswordField txtPassword;
    public JButton btnIngresar;

    public FrmLogin() {
        setTitle("Sistema de Votación Electrónica - Acceso");
        setSize(450, 320); 
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.WHITE);

        JLabel lblHeader = new JLabel("PORTAL DEL ELECTOR", SwingConstants.CENTER);
        lblHeader.setOpaque(true);
        lblHeader.setBackground(new Color(25, 50, 100)); 
        lblHeader.setForeground(Color.WHITE);
        lblHeader.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblHeader.setPreferredSize(new Dimension(450, 60));
        add(lblHeader, BorderLayout.NORTH);

        JPanel pnlForm = new JPanel(new GridLayout(4, 1, 5, 5));
        pnlForm.setBackground(Color.WHITE);
        pnlForm.setBorder(BorderFactory.createEmptyBorder(20, 60, 10, 60));
        
        JLabel lblDni = new JLabel("Número de Documento (DNI):");
        lblDni.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblDni.setForeground(Color.DARK_GRAY);
        txtDni = new JTextField();
        txtDni.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        
        JLabel lblPass = new JLabel("Contraseña de Acceso:");
        lblPass.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblPass.setForeground(Color.DARK_GRAY);
        txtPassword = new JPasswordField();
        txtPassword.setFont(new Font("Segoe UI", Font.PLAIN, 16));

        pnlForm.add(lblDni);
        pnlForm.add(txtDni);
        pnlForm.add(lblPass);
        pnlForm.add(txtPassword);
        add(pnlForm, BorderLayout.CENTER);

        JPanel pnlBotones = new JPanel();
        pnlBotones.setBackground(Color.WHITE);
        pnlBotones.setBorder(BorderFactory.createEmptyBorder(10, 0, 30, 0));
        
        btnIngresar = new JButton("Validar e Ingresar");
        btnIngresar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnIngresar.setBackground(new Color(0, 120, 215));
        btnIngresar.setForeground(Color.WHITE);
        
        // Corrección de renderizado Flat
        btnIngresar.setFocusPainted(false);
        btnIngresar.setBorderPainted(false);
        btnIngresar.setOpaque(true);
        
        btnIngresar.setPreferredSize(new Dimension(300, 45));
        btnIngresar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        pnlBotones.add(btnIngresar);
        add(pnlBotones, BorderLayout.SOUTH);
    }
}