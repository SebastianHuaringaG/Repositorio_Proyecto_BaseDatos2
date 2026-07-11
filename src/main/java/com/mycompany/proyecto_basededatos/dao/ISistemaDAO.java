package com.mycompany.proyecto_basededatos.dao;

import com.mycompany.proyecto_basededatos.model.Candidato;
import com.mycompany.proyecto_basededatos.model.Eleccion;
import com.mycompany.proyecto_basededatos.model.Elector;
import com.mycompany.proyecto_basededatos.model.Entidad;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

public interface ISistemaDAO {
    void registrarEntidad(String nombre, String tipo) throws SQLException;
    List<Entidad> obtenerEntidades() throws SQLException;
    List<Eleccion> obtenerElecciones() throws SQLException;
    List<Candidato> obtenerCandidatosPorEleccion(int electionId) throws SQLException;

    void crearEleccion(int entityId, String nombre, Timestamp inicio, Timestamp fin) throws SQLException;
    void registrarCandidato(int electionId, String nombreCompleto, String lista) throws SQLException;
    void registrarElector(int electionId, String dni, String nombreCompleto, String password) throws SQLException;
    
    Elector autenticarElector(String dni, String password) throws SQLException;
    void registrarVotoTransaccional(int voterId, int candidateId, int electionId) throws SQLException;
    void cambiarEstadoEleccion(int electionId, String estado) throws SQLException;
    String obtenerResultados(int electionId) throws SQLException;
}