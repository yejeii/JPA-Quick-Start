DROP TABLE IF EXISTS s_dept;
DROP TABLE IF EXISTS s_emp;

-- 부서 테이블
CREATE TABLE s_dept
(
    dept_id number(7) CONSTRAINT s_dept_id_nn NOT NULL ,
    name varchar2(25) CONSTRAINT s_dept_name_nn NOT NULL ,
    CONSTRAINT s_dept_id_pk PRIMARY KEY (dept_id)
);

-- 직원 테이블
CREATE TABLE s_emp (
    id number(7) CONSTRAINT s_emp_id_nn NOT NULL ,
    name varchar2(25) CONSTRAINT s_emp_name_nn NOT NULL ,
    dept_id number(7) ,
    CONSTRAINT s_emp_id_pk PRIMARY KEY (id)
);

ALTER TABLE s_emp ADD CONSTRAINT s_emp_dept_id_fk
    FOREIGN KEY (dept_id) REFERENCES s_dept (dept_id);

INSERT INTO s_dept VALUES(1, '개발부');
INSERT INTO s_emp VALUES(1, '둘리', 1);
INSERT INTO s_emp VALUES(2, '도우너', 1);
