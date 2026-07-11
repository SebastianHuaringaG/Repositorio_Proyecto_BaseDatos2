package com.mycompany.proyecto_basededatos.model;

public class Elector {
    private int voterId;
    private String dni;
    private String fullName;
    private String votingStatus;
    private int electionId; // NUEVO: Para saber a qué elección pertenece

    public Elector() {}

    public int getVoterId() { return voterId; }
    public void setVoterId(int voterId) { this.voterId = voterId; }
    public String getDni() { return dni; }
    public void setDni(String dni) { this.dni = dni; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getVotingStatus() { return votingStatus; }
    public void setVotingStatus(String votingStatus) { this.votingStatus = votingStatus; }
    public int getElectionId() { return electionId; }
    public void setElectionId(int electionId) { this.electionId = electionId; }
}