CREATE TABLE USERS(
	user_id NUMBER PRIMARY KEY,
	name VARCHAR2(50 CHAR) NOT NULL,
	password VARCHAR2(100 CHAR) NOT NULL
);

CREATE TABLE TASKS(
    task_id     NUMBER PRIMARY KEY,
    name       	VARCHAR2(50 CHAR) NOT NULL,
    info        VARCHAR2(1000 CHAR),
    date_time 	TIMESTAMP NOT NULL,
    contacts 	VARCHAR2(100 CHAR),
    			-- NUMBER(1) = BOOLEAN
    active 		NUMBER(1) NOT NULL,
    owner 		NUMBER NOT NULL,

    CONSTRAINT owner_fk
    	FOREIGN KEY (owner)
    	REFERENCES USERS (user_id)
);

CREATE SEQUENCE USERS_SEQ
INCREMENT BY 1 
START WITH 1
CACHE 10
NOORDER
NOCYCLE;
/

CREATE SEQUENCE TASKS_SEQ
INCREMENT BY 1 
START WITH 1
NOCACHE
NOORDER
NOCYCLE;
/

CREATE OR REPLACE TRIGGER INSERTING_AND_UPDATING_USERS BEFORE 
	INSERT OR UPDATE ON USERS
    FOR EACH ROW
BEGIN
	IF(INSERTING AND :new.user_id IS NULL) THEN
    	:new.user_id := USERS_SEQ.NEXTVAL;
    ELSIF(UPDATING AND :new.user_id <> :old.user_id) THEN
    	RAISE_APPLICATION_ERROR(-20001,'ID нельзя менять!');
    END IF;
END;
/

CREATE OR REPLACE TRIGGER INSERTING_AND_UPDATING_TASKS BEFORE 
	INSERT OR UPDATE ON TASKS
    FOR EACH ROW
BEGIN
	IF(INSERTING AND :new.task_id IS NULL) THEN
    	:new.task_id := TASKS_SEQ.NEXTVAL;
    ELSIF(UPDATING AND :new.task_id <> :old.task_id) THEN
    	RAISE_APPLICATION_ERROR(-20001,'ID нельзя менять!');
    END IF;
END;
/

CREATE OR REPLACE FUNCTION ADD_TASK(
  name_      IN VARCHAR2,
  info_      IN VARCHAR2,
  date_time_ IN TIMESTAMP,
  contacts_  IN VARCHAR2,
  active_    IN NUMBER,
  owner_     IN NUMBER) RETURN NUMBER
IS
  ID NUMBER;
BEGIN
  INSERT INTO TASKS(name, info, date_time, contacts, active, owner)
    VALUES(name_, info_, date_time_, contacts_, active_, owner_);

  SELECT task_id INTO ID
    FROM TASKS
    WHERE name = name_ AND owner = owner_ AND info = info_ AND date_time = date_time_;

  RETURN ID;
END;
/

CREATE OR REPLACE FUNCTION ADD_USER(
  name_     IN VARCHAR2,
  password_ IN VARCHAR2) RETURN NUMBER
IS
  ID NUMBER;
BEGIN
  INSERT INTO USERS(name, password) VALUES(name_, password_);

  SELECT user_id INTO ID FROM USERS WHERE name = name_ AND password = password_;

  RETURN ID;
END;
/