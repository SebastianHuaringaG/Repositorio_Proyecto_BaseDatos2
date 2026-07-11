package com.mycompany.proyecto_basededatos.service;

import com.mycompany.proyecto_basededatos.model.Candidato;
import com.mycompany.proyecto_basededatos.model.Eleccion;
import com.mycompany.proyecto_basededatos.model.Elector;
import com.mycompany.proyecto_basededatos.model.Entidad;
import java.sql.Timestamp;
import java.util.List;

public interface IVotacionService {
    // Fase 1: Entidades y Elecciones
    void registrarNuevaEntidad(String nombre, String tipo) throws Exception;
    List<Entidad> listarEntidades() throws Exception;
    List<Eleccion> listarElecciones() throws Exception;
    List<Candidato> listarCandidatos(int electionId) throws Exception;
    
    void prepararEleccion(int entityId, String nombre, Timestamp inicio, Timestamp fin) throws Exception;
    void agregarCandidato(int electionId, String nombreCompleto, String lista) throws Exception;
    void cargarPadronelector(int electionId, String dni, String nombreCompleto, String password) throws Exception;
    
    // Fase 2: Votación
    Elector procesarLogin(String dni, String password) throws Exception;
    void emitirVoto(int voterId, int candidateId, int electionId) throws Exception;
    
    // Fase 3: Resultados
    void gestionarEstadoEleccion(int electionId, String estado) throws Exception;
    String generarReporte(int electionId) throws Exception;
}