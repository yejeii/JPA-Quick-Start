CREATE TABLE s_emp (
    id number(7) constraint s_emp_id_nn not NULL,
    name varchar2(25) constraint s_emp_name_nn not NULL,
    start_date DATE,
    title varchar2(25),
    dept_name VARCHAR2(25),
    salary number(11,2) ,
    constraint s_emp_id_pk PRIMARY KEY(id)
);

INSERT INTO s_emp VALUES (1, '안은경', '2002-12-03', '영업대표이사', '영업부', 2500);
SELECT * from s_emp;