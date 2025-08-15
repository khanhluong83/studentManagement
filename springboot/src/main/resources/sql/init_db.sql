CREATE SCHEMA student_management;

CREATE SEQUENCE IF NOT EXISTS student_management.student_seq
    INCREMENT 1
    START 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    CACHE 1;

CREATE SEQUENCE IF NOT EXISTS student_management.course_seq
    INCREMENT 1
    START 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    CACHE 1;


CREATE TABLE IF NOT EXISTS student_management.student
(
    id integer NOT NULL,
    first_name varchar(200),
    last_name varchar(200),
    email varchar(200),
    phone varchar(200),
    
    CONSTRAINT student_pkey PRIMARY KEY (id)
)

TABLESPACE pg_default;
    
CREATE TABLE IF NOT EXISTS student_management.course
(
    id integer NOT NULL,
    code varchar(30),
    name varchar(200),
    start_date date,
    end_date date,
    CONSTRAINT course_pkey PRIMARY KEY (id),
    CONSTRAINT unique_code UNIQUE (code)
)

TABLESPACE pg_default;

CREATE TABLE IF NOT EXISTS student_management.course_enrollment
(
    student_id integer NOT NULL,
    course_id integer NOT NULL,
    CONSTRAINT course_enrollment_pkey PRIMARY KEY (student_id, course_id),
    
    CONSTRAINT course_enrollment_student_fk FOREIGN KEY (student_id)
        REFERENCES student_management.student (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    
    CONSTRAINT course_enrollment_course_fk FOREIGN KEY (course_id)
        REFERENCES student_management.course (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)

TABLESPACE pg_default;
