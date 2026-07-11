ALTER SESSION SET "_ORACLE_SCRIPT"=true;

CREATE TABLESPACE onpe_data 
DATAFILE 'onpe_data_01.dbf' SIZE 100M AUTOEXTEND ON NEXT 50M MAXSIZE 500M;

CREATE USER onpe_sys IDENTIFIED BY onpe123
DEFAULT TABLESPACE onpe_data
QUOTA UNLIMITED ON onpe_data;

GRANT CREATE SESSION, CREATE TABLE, CREATE VIEW, CREATE PROCEDURE, CREATE TRIGGER, CREATE SEQUENCE TO onpe_sys;
ALTER SESSION SET CURRENT_SCHEMA = onpe_sys;

-- Tablas
CREATE TABLE electoral_entity (
    entity_id NUMBER PRIMARY KEY,
    entity_name VARCHAR2(150) NOT NULL,
    entity_type VARCHAR2(100) NOT NULL,
    address VARCHAR2(255),
    contact_email VARCHAR2(150),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
);

CREATE TABLE user_account (
    user_id NUMBER PRIMARY KEY,
    username VARCHAR2(50) UNIQUE NOT NULL,
    password_hash VARCHAR2(255) NOT NULL,
    role VARCHAR2(20) CHECK (role IN ('ADMIN', 'AUDITOR')) NOT NULL,
    email VARCHAR2(150) UNIQUE NOT NULL,
    is_active NUMBER(1) DEFAULT 1 NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
);

CREATE TABLE election (
    election_id NUMBER PRIMARY KEY,
    entity_id NUMBER NOT NULL REFERENCES electoral_entity(entity_id),
    created_by NUMBER NOT NULL REFERENCES user_account(user_id),
    election_name VARCHAR2(200) NOT NULL,
    start_datetime TIMESTAMP NOT NULL,
    end_datetime TIMESTAMP NOT NULL,
    status VARCHAR2(20) DEFAULT 'PROGRAMADA' CHECK (status IN ('PROGRAMADA', 'EN CURSO', 'FINALIZADA')) NOT NULL,
    majority_type VARCHAR2(50),
    currency VARCHAR2(10) CHECK (currency IN ('PEN', 'USD', 'EUR')) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT chk_dates CHECK (start_datetime < end_datetime)
);

CREATE TABLE audit_log (
    log_id NUMBER PRIMARY KEY,
    user_id NUMBER NOT NULL REFERENCES user_account(user_id),
    operation_type VARCHAR2(50) NOT NULL,
    affected_table VARCHAR2(50) NOT NULL,
    operation_detail VARCHAR2(1000),
    operation_timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    ip_address VARCHAR2(45)
);

CREATE TABLE candidate (
    candidate_id NUMBER PRIMARY KEY,
    election_id NUMBER NOT NULL REFERENCES election(election_id),
    full_name VARCHAR2(150) NOT NULL,
    list_name VARCHAR2(100),
    position VARCHAR2(100),
    vote_count NUMBER DEFAULT 0 NOT NULL,
    registered_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
);

CREATE TABLE electoral_roll (
    roll_id NUMBER PRIMARY KEY,
    election_id NUMBER UNIQUE NOT NULL REFERENCES election(election_id),
    roll_name VARCHAR2(150) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
);

CREATE TABLE voter (
    voter_id NUMBER PRIMARY KEY,
    roll_id NUMBER NOT NULL REFERENCES electoral_roll(roll_id),
    dni VARCHAR2(20) NOT NULL,
    full_name VARCHAR2(150) NOT NULL,
    password_hash VARCHAR2(255) NOT NULL,
    voting_status VARCHAR2(20) DEFAULT 'NO VOTO' CHECK (voting_status IN ('NO VOTO', 'YA VOTO')) NOT NULL,
    registered_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT uq_voter UNIQUE (roll_id, dni)
);

CREATE TABLE vote (
    vote_id NUMBER PRIMARY KEY,
    election_id NUMBER NOT NULL REFERENCES election(election_id),
    candidate_id NUMBER NOT NULL REFERENCES candidate(candidate_id),
    voted_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
);

-- Secuencias e Índices[cite: 1]
CREATE SEQUENCE seq_vote_id START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_audit_id START WITH 1 INCREMENT BY 1;

CREATE INDEX idx_candidate_election ON candidate(election_id);
CREATE INDEX idx_voter_roll ON voter(roll_id);
CREATE INDEX idx_vote_election ON vote(election_id);
CREATE INDEX idx_vote_candidate ON vote(candidate_id);

-- Inserción de Datos Iniciales[cite: 1]
INSERT INTO electoral_entity VALUES (1, 'UNMSM', 'UNIVERSIDAD', 'Lima', 'info@unmsm.edu.pe', CURRENT_TIMESTAMP);
INSERT INTO user_account VALUES (1, 'admin', 'hash123', 'ADMIN', 'admin@onpe.gob.pe', 1, CURRENT_TIMESTAMP);
INSERT INTO election VALUES (1, 1, 1, 'Elecciones Generales', TO_TIMESTAMP('2026-10-01 08:00:00', 'YYYY-MM-DD HH24:MI:SS'), TO_TIMESTAMP('2026-10-01 16:00:00', 'YYYY-MM-DD HH24:MI:SS'), 'EN CURSO', 'MAYORIA', 'PEN', CURRENT_TIMESTAMP);
INSERT INTO candidate VALUES (1, 1, 'Dr. Perez', 'Lista 1', 'Decano', 0, CURRENT_TIMESTAMP);
INSERT INTO candidate VALUES (2, 1, 'Dra. Gomez', 'Lista 2', 'Decano', 0, CURRENT_TIMESTAMP);
INSERT INTO electoral_roll VALUES (1, 1, 'Padron Principal', CURRENT_TIMESTAMP);
INSERT INTO voter VALUES (1, 1, '12345678', 'Estudiante Uno', 'pass123', 'NO VOTO', CURRENT_TIMESTAMP);
COMMIT;

-- Procedimiento Almacenado Transaccional[cite: 1]
CREATE OR REPLACE PROCEDURE PRC_CAST_VOTE (
    p_voter_id IN NUMBER,
    p_candidate_id IN NUMBER,
    p_election_id IN NUMBER
) AS
    v_status VARCHAR2(20);
BEGIN
    SELECT voting_status INTO v_status 
    FROM voter 
    WHERE voter_id = p_voter_id FOR UPDATE;

    IF v_status = 'NO VOTO' THEN
        INSERT INTO vote (vote_id, election_id, candidate_id)
        VALUES (seq_vote_id.NEXTVAL, p_election_id, p_candidate_id);

        UPDATE candidate SET vote_count = vote_count + 1 WHERE candidate_id = p_candidate_id;
        UPDATE voter SET voting_status = 'YA VOTO' WHERE voter_id = p_voter_id;
    ELSE
        RAISE_APPLICATION_ERROR(-20001, 'El elector ya emitio su voto.');
    END IF;
END;
/

SELECT * FROM vote;