package com.mycompany.proyecto_basededatos.service.impl;

import com.mycompany.proyecto_basededatos.dao.ISistemaDAO;
import com.mycompany.proyecto_basededatos.model.Candidato;
import com.mycompany.proyecto_basededatos.model.Eleccion;
import com.mycompany.proyecto_basededatos.model.Elector;
import com.mycompany.proyecto_basededatos.model.Entidad;
import com.mycompany.proyecto_basededatos.service.IVotacionService;
import java.sql.Timestamp;
import java.util.List;

public class VotacionServiceImpl implements IVotacionService {
    
    private final ISistemaDAO dao;

    public VotacionServiceImpl(ISistemaDAO dao) {
        this.dao = dao;
    }

    // ==========================================
    // MÉTODOS DE CONSULTA (LOS QUE FALTABAN)
    // ==========================================

    @Override
    public List<Entidad> listarEntidades() throws Exception {
        return dao.obtenerEntidades();
    }

    @Override
    public List<Eleccion> listarElecciones() throws Exception {
        return dao.obtenerElecciones();
    }

    @Override
    public List<Candidato> listarCandidatos(int electionId) throws Exception {
        return dao.obtenerCandidatosPorEleccion(electionId);
    }

    // ==========================================
    // FASE 1
    // ==========================================

    @Override
    public void registrarNuevaEntidad(String nombre, String tipo) throws Exception {
        dao.registrarEntidad(nombre, tipo);
    }

    @Override
    public void prepararEleccion(int entityId, String nombre, Timestamp inicio, Timestamp fin) throws Exception {
        dao.crearEleccion(entityId, nombre, inicio, fin);
    }

    @Override
    public void agregarCandidato(int electionId, String nombreCompleto, String lista) throws Exception {
        dao.registrarCandidato(electionId, nombreCompleto, lista);
    }

    @Override
    public void cargarPadronelector(int electionId, String dni, String nombreCompleto, String password) throws Exception {
        dao.registrarElector(electionId, dni, nombreCompleto, password);
    }

    // ==========================================
    // FASE 2
    // ==========================================

    @Override
    public Elector procesarLogin(String dni, String password) throws Exception {
        Elector elector = dao.autenticarElector(dni, password);
        if (elector == null) {
            throw new Exception("DNI o contraseña incorrectos.");
        }
        if ("YA VOTO".equals(elector.getVotingStatus())) {
            throw new Exception("Acceso Denegado: Usted ya emitió su voto previamente.");
        }
        return elector;
    }

    @Override
    public void emitirVoto(int voterId, int candidateId, int electionId) throws Exception {
        try {
            dao.registrarVotoTransaccional(voterId, candidateId, electionId);
        } catch (Exception e) {
            throw new Exception("Fallo en la transacción: " + e.getMessage());
        }
    }

    // ==========================================
    // FASE 3
    // ==========================================

    @Override
    public void gestionarEstadoEleccion(int electionId, String estado) throws Exception {
        dao.cambiarEstadoEleccion(electionId, estado);
    }

    @Override
    public String generarReporte(int electionId) throws Exception {
        return dao.obtenerResultados(electionId);
    }
}