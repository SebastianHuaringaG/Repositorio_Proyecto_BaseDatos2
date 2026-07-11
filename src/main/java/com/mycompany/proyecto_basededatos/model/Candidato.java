package com.mycompany.proyecto_basededatos.model;

public class Candidato {
    private int candidateId;
    private String fullName;
    private String listName;

    public Candidato(int candidateId, String fullName, String listName) {
        this.candidateId = candidateId;
        this.fullName = fullName;
        this.listName = listName;
    }

    public int getCandidateId() { return candidateId; }
    public String getFullName() { return fullName; }
    public String getListName() { return listName; }

    @Override
    public String toString() {
        return "[" + candidateId + "] " + fullName + " - " + listName;
    }
}