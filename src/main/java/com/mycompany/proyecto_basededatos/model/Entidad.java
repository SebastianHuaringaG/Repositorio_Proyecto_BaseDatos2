package com.mycompany.proyecto_basededatos.model;

public class Entidad {
    private int entityId;
    private String entityName;

    public Entidad(int entityId, String entityName) {
        this.entityId = entityId;
        this.entityName = entityName;
    }

    public int getEntityId() { return entityId; }
    public String getEntityName() { return entityName; }

    // El toString es importante para que el JComboBox muestre el nombre correctamente
    @Override
    public String toString() {
        return "[" + entityId + "] " + entityName;
    }
}