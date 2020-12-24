CREATE TABLE curso(
    ID SERIAL CONSTRAINT pk_cur PRIMARY KEY,
    Nome VARCHAR(100) NOT NULL
);

CREATE TABLE materia(
    ID SERIAL CONSTRAINT pk_mat PRIMARY KEY,
    Nome    VARCHAR(100) NOT NULL,
    Curso   INT NOT NULL,
    CONSTRAINT fk_cur_id FOREIGN KEY(Curso) REFERENCES curso(ID)
);

CREATE TABLE topico(
    ID SERIAL CONSTRAINT pk_topico PRIMARY KEY,
    Nome VARCHAR(100) NOT NULL,
    MatID INT NOT NULL,
    CONSTRAINT fk_mat_id FOREIGN KEY(MatID) REFERENCES materia(ID)
);

CREATE TABLE questao_aberta(
    ID SERIAL CONSTRAINT pk_quest_aberta PRIMARY KEY,
    Enunciado TEXT  NOT NULL,
    Num_Linhas INT NOT NULL,
    Gabarito TEXT NOT NULL,
    TopicoID INT NOT NULL,
    CONSTRAINT qa_top FOREIGN KEY(TopicoID) REFERENCES topico(ID)
);

CREATE TABLE questao_fechada(
    ID SERIAL CONSTRAINT pk_questao_fechada PRIMARY KEY,
    Enunciado TEXT NOT NULL,
    TopicoID INT NOT NULL,
    CONSTRAINT qf_top FOREIGN KEY(TopicoID) REFERENCES topico(ID)
);

CREATE TABLE alternativa(
    ID SERIAL CONSTRAINT pk_alternativa PRIMARY KEY,
    Texto TEXT NOT NULL,
    Certa BOOLEAN NOT NULL,
    QuestF_ID INT NOT NULL,
    CONSTRAINT fk_questao_fechada FOREIGN KEY(QuestF_ID) REFERENCES questao_fechada (id) MATCH SIMPLE ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE prova(
    ID SERIAL CONSTRAINT pk_prova PRIMARY KEY,
    Tipo VARCHAR NOT NULL DEFAULT '5:3',
    Tamanho INTEGER NOT NULL DEFAULT 12,
    Nome_Prof   VARCHAR(100) NOT NULL,
    Nome_Curso  VARCHAR(100) NOT NULL,
    Nome_Inst   VARCHAR(100),
    Data_Prova  DATE NOT NULL,
    Nota        FLOAT NOT NULL,
    Turma       VARCHAR(100) NOT NULL,
    Materia_ID  INTEGER NOT NULL,
    Nome_Prova  VARCHAR(100),
    CONSTRAINT fk_prova_mat FOREIGN KEY(Materia_ID) REFERENCES materia(ID)
);

CREATE TABLE idioma(
    Codigo_Idioma  VARCHAR(100) DEFAULT 'en_US' NOT NULL PRIMARY KEY
);

INSERT INTO idioma VALUES (DEFAULT);

CREATE TABLE questao_fechada_prova(
    Prova_ID INT NOT NULL,
    QuestaoFID INT NOT NULL,
    CONSTRAINT pk_pf PRIMARY KEY(Prova_ID,QuestaoFID),
    CONSTRAINT fk_prova_f FOREIGN KEY(Prova_ID) REFERENCES prova(ID) MATCH SIMPLE ON UPDATE CASCADE ON DELETE CASCADE,
    CONSTRAINT fk_questao_f FOREIGN KEY(QuestaoFID) REFERENCES questao_fechada(ID)
);

CREATE TABLE questao_aberta_prova(
    Prova_ID INT NOT NULL,
    QuestAID INT NOT NULL,
    CONSTRAINT pk_pa PRIMARY KEY(Prova_ID,QuestAID),
    CONSTRAINT fk_prova_a FOREIGN KEY(Prova_ID) REFERENCES prova(ID) MATCH SIMPLE ON UPDATE CASCADE ON DELETE CASCADE,
    CONSTRAINT fk_questao_a FOREIGN KEY(QuestAID) REFERENCES questao_aberta(ID)
);
CREATE OR REPLACE FUNCTION verefica_alts()
RETURNS trigger AS $$
DECLARE tot INT;
BEGIN
    SELECT INTO tot (SELECT COUNT(id) FROM alternativa WHERE questf_id = NEW.questf_id AND certa = NEW.certa );
    IF((NEW.certa = TRUE AND tot <3) OR (NEW.certa = FALSE AND tot <5)) THEN
            RETURN NEW;
    ELSE
            RAISE EXCEPTION 'A Questão já possuí o número maximo de alternativas certas ou erradas';
            RETURN NULL;
    END IF;
END $$ LANGUAGE 'plpgsql';

CREATE TRIGGER verifica_alts_add BEFORE INSERT ON alternativa FOR EACH ROW EXECUTE PROCEDURE verefica_alts();