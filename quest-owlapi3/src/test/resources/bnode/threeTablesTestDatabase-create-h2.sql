CREATE TABLE exam(
    id       int(10),
    name  VARCHAR,
    primary key (id)
);

CREATE TABLE student(
    id       int(10),
    name  VARCHAR,
    type int(2),
    primary key (id)
);

CREATE TABLE student_exam(
    student_id     int(10),
    exam_id        int(10),
    status       VARCHAR,
    primary key (student_id, exam_id),
    foreign key (student_id) REFERENCES student(id),
    foreign key (exam_id) REFERENCES exam(id)
);


INSERT INTO exam VALUES (1, 'Semantic Technologies');
INSERT INTO exam VALUES (2, 'Theory of computation');
INSERT INTO exam VALUES (3, 'Foundation of databases');


INSERT INTO student VALUES (1, 'John', 0);
INSERT INTO student VALUES (2, 'Jena', 0);
INSERT INTO student VALUES (3, 'Mark', 1);

INSERT INTO student_exam VALUES (1, 1, 'PASSED');
INSERT INTO student_exam VALUES (2, 1, 'PASSED');
INSERT INTO student_exam VALUES (2, 2, 'PASSED');
INSERT INTO student_exam VALUES (2, 3, 'PASSED');
INSERT INTO student_exam VALUES (3, 2, 'FAILED');



