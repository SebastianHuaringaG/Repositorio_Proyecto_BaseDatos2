package com.mycompany.proyecto_basededatos.view;

import com.mycompany.proyecto_basededatos.model.Candidato;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class FrmVotacion extends JFrame {
    public JButton btnVotar;
    public JLabel lblBienvenida;
    public JPanel pnlCandidatos;
    public ButtonGroup grupoVoto;

    public FrmVotacion() {
        setTitle("Cédula de Sufragio Oficial");
        setSize(550, 480);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.WHITE);

        JPanel pnlHeader = new JPanel(new BorderLayout());
        pnlHeader.setBackground(new Color(240, 245, 250));
        pnlHeader.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));
        
        lblBienvenida = new JLabel("Bienvenido", SwingConstants.LEFT);
        lblBienvenida.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblBienvenida.setForeground(new Color(25, 50, 100));
        
        JLabel lblInstruccion = new JLabel("Seleccione una única opción y presione Emitir Voto.", SwingConstants.LEFT);
        lblInstruccion.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblInstruccion.setForeground(Color.DARK_GRAY);
        
        pnlHeader.add(lblBienvenida, BorderLayout.NORTH);
        pnlHeader.add(lblInstruccion, BorderLayout.SOUTH);
        add(pnlHeader, BorderLayout.NORTH);

        pnlCandidatos = new JPanel();
        pnlCandidatos.setLayout(new BoxLayout(pnlCandidatos, BoxLayout.Y_AXIS));
        pnlCandidatos.setBorder(BorderFactory.createEmptyBorder(25, 60, 25, 60));
        pnlCandidatos.setBackground(Color.WHITE);
        grupoVoto = new ButtonGroup();
        
        JScrollPane scrollPane = new JScrollPane(pnlCandidatos);
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        scrollPane.getViewport().setBackground(Color.WHITE); 
        add(scrollPane, BorderLayout.CENTER);

        JPanel pnlFooter = new JPanel();
        pnlFooter.setBackground(Color.WHITE);
        pnlFooter.setBorder(BorderFactory.createEmptyBorder(20, 0, 25, 0));
        
        btnVotar = new JButton("EMITIR VOTO ANÓNIMO");
        btnVotar.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnVotar.setBackground(new Color(40, 167, 69)); 
        btnVotar.setForeground(Color.WHITE);
        
        // Corrección de renderizado Flat
        btnVotar.setFocusPainted(false);
        btnVotar.setBorderPainted(false);
        btnVotar.setOpaque(true);
        
        btnVotar.setPreferredSize(new Dimension(350, 55));
        btnVotar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        pnlFooter.add(btnVotar);
        add(pnlFooter, BorderLayout.SOUTH);
    }

    public void cargarCandidatos(List<Candidato> lista) {
        for (Candidato c : lista) {
            JRadioButton rdb = new JRadioButton(c.getFullName() + "   |   " + c.getListName());
            rdb.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            rdb.setBackground(Color.WHITE);
            rdb.setCursor(new Cursor(Cursor.HAND_CURSOR));
            rdb.setActionCommand(String.valueOf(c.getCandidateId())); 
            
            grupoVoto.add(rdb);
            pnlCandidatos.add(rdb);
            pnlCandidatos.add(Box.createRigidArea(new Dimension(0, 20))); 
        }
        pnlCandidatos.revalidate();
        pnlCandidatos.repaint();
    }
}