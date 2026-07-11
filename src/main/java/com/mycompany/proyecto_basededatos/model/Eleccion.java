package com.mycompany.proyecto_basededatos.model;

public class Eleccion {
    private int electionId;
    private String electionName;

    public Eleccion(int electionId, String electionName) {
        this.electionId = electionId;
        this.electionName = electionName;
    }

    public int getElectionId() { return electionId; }
    public String getElectionName() { return electionName; }

    @Override
    public String toString() {
        return "[" + electionId + "] " + electionName;
    }
}