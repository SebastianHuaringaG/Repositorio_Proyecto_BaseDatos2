package com.mycompany.proyecto_basededatos.dao.impl;

import com.mycompany.proyecto_basededatos.config.ConexionDB;
import com.mycompany.proyecto_basededatos.dao.ISistemaDAO;
import com.mycompany.proyecto_basededatos.model.Candidato;
import com.mycompany.proyecto_basededatos.model.Eleccion;
import com.mycompany.proyecto_basededatos.model.Elector;
import com.mycompany.proyecto_basededatos.model.Entidad;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SistemaDAOImpl implements ISistemaDAO {

    @Override
    public void registrarEntidad(String nombre, String tipo) throws SQLException {
        String sql = "INSERT INTO electoral_entity (entity_id, entity_name, entity_type) VALUES ((SELECT NVL(MAX(entity_id), 0) + 1 FROM electoral_entity), ?, ?)";
        try (Connection conn = ConexionDB.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nombre); ps.setString(2, tipo); ps.executeUpdate();
        }
    }

    @Override
    public List<Entidad> obtenerEntidades() throws SQLException {
        List<Entidad> lista = new ArrayList<>();
        String sql = "SELECT entity_id, entity_name FROM electoral_entity ORDER BY entity_id";
        try (Connection conn = ConexionDB.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(new Entidad(rs.getInt("entity_id"), rs.getString("entity_name")));
        }
        return lista;
    }

    @Override
    public List<Eleccion> obtenerElecciones() throws SQLException {
        List<Eleccion> lista = new ArrayList<>();
        String sql = "SELECT election_id, election_name FROM election ORDER BY election_id DESC";
        try (Connection conn = ConexionDB.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(new Eleccion(rs.getInt("election_id"), rs.getString("election_name")));
        }
        return lista;
    }

    @Override
    public List<Candidato> obtenerCandidatosPorEleccion(int electionId) throws SQLException {
        List<Candidato> lista = new ArrayList<>();
        String sql = "SELECT candidate_id, full_name, list_name FROM candidate WHERE election_id = ? ORDER BY candidate_id";
        try (Connection conn = ConexionDB.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, electionId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(new Candidato(rs.getInt("candidate_id"), rs.getString("full_name"), rs.getString("list_name")));
            }
        }
        return lista;
    }

    @Override
    public void crearEleccion(int entityId, String nombre, Timestamp inicio, Timestamp fin) throws SQLException {
        String sqlEleccion = "INSERT INTO election (election_id, entity_id, created_by, election_name, start_datetime, end_datetime, status, currency) VALUES ((SELECT NVL(MAX(election_id), 0) + 1 FROM election), ?, 1, ?, ?, ?, 'PROGRAMADA', 'PEN')";
        String sqlPadron = "INSERT INTO electoral_roll (roll_id, election_id, roll_name) VALUES ((SELECT NVL(MAX(roll_id), 0) + 1 FROM electoral_roll), (SELECT MAX(election_id) FROM election), ?)";
        
        try (Connection conn = ConexionDB.getConnection()) {
            conn.setAutoCommit(false); // Transacción para crear elección y su padrón al mismo tiempo
            try (PreparedStatement ps1 = conn.prepareStatement(sqlEleccion); PreparedStatement ps2 = conn.prepareStatement(sqlPadron)) {
                ps1.setInt(1, entityId); ps1.setString(2, nombre); ps1.setTimestamp(3, inicio); ps1.setTimestamp(4, fin);
                ps1.executeUpdate();
                
                ps2.setString(1, "Padron Oficial - " + nombre);
                ps2.executeUpdate();
                
                conn.commit();
            } catch (SQLException e) {
                conn.rollback(); throw e;
            } finally { conn.setAutoCommit(true); }
        }
    }

    @Override
    public void registrarCandidato(int electionId, String nombreCompleto, String lista) throws SQLException {
        String sql = "INSERT INTO candidate (candidate_id, election_id, full_name, list_name, position, vote_count) VALUES ((SELECT NVL(MAX(candidate_id), 0) + 1 FROM candidate), ?, ?, ?, 'Candidato', 0)";
        try (Connection conn = ConexionDB.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, electionId); ps.setString(2, nombreCompleto); ps.setString(3, lista); ps.executeUpdate();
        }
    }

    @Override
    public void registrarElector(int electionId, String dni, String nombreCompleto, String password) throws SQLException {
        String sqlPadron = "SELECT roll_id FROM electoral_roll WHERE election_id = ?";
        String sqlVoter = "INSERT INTO voter (voter_id, roll_id, dni, full_name, password_hash, voting_status) VALUES ((SELECT NVL(MAX(voter_id), 0) + 1 FROM voter), ?, ?, ?, ?, 'NO VOTO')";
        
        try (Connection conn = ConexionDB.getConnection(); PreparedStatement ps1 = conn.prepareStatement(sqlPadron)) {
            ps1.setInt(1, electionId);
            ResultSet rs = ps1.executeQuery();
            if (rs.next()) {
                int rollId = rs.getInt("roll_id");
                try (PreparedStatement ps2 = conn.prepareStatement(sqlVoter)) {
                    ps2.setInt(1, rollId); ps2.setString(2, dni); ps2.setString(3, nombreCompleto); ps2.setString(4, password);
                    ps2.executeUpdate();
                }
            } else {
                throw new SQLException("No existe un padrón asociado a esta elección.");
            }
        }
    }

    @Override
    public Elector autenticarElector(String dni, String password) throws SQLException {
        // Hacemos JOIN con el padrón para saber exactamente a qué elección pertenece este elector
        String sql = "SELECT v.voter_id, v.full_name, v.voting_status, r.election_id FROM voter v INNER JOIN electoral_roll r ON v.roll_id = r.roll_id WHERE v.dni = ? AND v.password_hash = ?";
        try (Connection conn = ConexionDB.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, dni); ps.setString(2, password);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Elector elector = new Elector();
                    elector.setVoterId(rs.getInt("voter_id"));
                    elector.setFullName(rs.getString("full_name"));
                    elector.setVotingStatus(rs.getString("voting_status"));
                    elector.setElectionId(rs.getInt("election_id"));
                    return elector;
                }
            }
        }
        return null;
    }

    @Override
    public void registrarVotoTransaccional(int voterId, int candidateId, int electionId) throws SQLException {
        String sql = "{CALL PRC_CAST_VOTE(?, ?, ?)}";
        Connection conn = null; CallableStatement cstmt = null;
        try {
            conn = ConexionDB.getConnection(); conn.setAutoCommit(false); 
            cstmt = conn.prepareCall(sql);
            cstmt.setInt(1, voterId); cstmt.setInt(2, candidateId); cstmt.setInt(3, electionId);
            cstmt.execute();
            conn.commit(); 
        } catch (SQLException e) {
            if (conn != null) conn.rollback(); throw e;
        } finally {
            if (cstmt != null) cstmt.close();
            if (conn != null) { conn.setAutoCommit(true); conn.close(); }
        }
    }

    @Override
    public void cambiarEstadoEleccion(int electionId, String estado) throws SQLException {
        String sql = "UPDATE election SET status = ? WHERE election_id = ?";
        try (Connection conn = ConexionDB.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, estado); ps.setInt(2, electionId); ps.executeUpdate();
        }
    }

    @Override
    public String obtenerResultados(int electionId) throws SQLException {
        StringBuilder reporte = new StringBuilder();
        String sql = "SELECT full_name, list_name, vote_count FROM candidate WHERE election_id = ? ORDER BY vote_count DESC";
        try (Connection conn = ConexionDB.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, electionId);
            ResultSet rs = ps.executeQuery();
            boolean ganador = true;
            reporte.append("=== ESCRUTINIO FINAL ===\n\n");
            while (rs.next()) {
                if(ganador){
                    reporte.append("⭐ GANADOR: ").append(rs.getString("full_name")).append("\nVotos: ").append(rs.getInt("vote_count")).append("\n\n");
                    ganador = false;
                } else {
                    reporte.append("Candidato: ").append(rs.getString("full_name")).append(" | Votos: ").append(rs.getInt("vote_count")).append("\n");
                }
            }
        }
        return reporte.toString();
    }
}