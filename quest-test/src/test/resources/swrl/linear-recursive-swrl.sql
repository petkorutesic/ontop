--
-- PostgreSQL database dump
--

SET statement_timeout = 0;
SET lock_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;

--
-- Name: plpgsql; Type: EXTENSION; Schema: -; Owner: 
--

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;


--
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


SET search_path = public, pg_catalog;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- Name: edge; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE edge (
    node1 integer NOT NULL,
    node2 integer NOT NULL
);


ALTER TABLE public.edge OWNER TO postgres;

--
-- Name: node; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE node (
    id integer NOT NULL,
    name character varying(10) NOT NULL
);


ALTER TABLE public.node OWNER TO postgres;

--
-- Data for Name: edge; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO edge VALUES (1, 2);
INSERT INTO edge VALUES (2, 3);
INSERT INTO edge VALUES (3, 4);
INSERT INTO edge VALUES (3, 5);


--
-- Data for Name: node; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO node VALUES (1, 'a');
INSERT INTO node VALUES (2, 'b');
INSERT INTO node VALUES (3, 'c');
INSERT INTO node VALUES (4, 'd');
INSERT INTO node VALUES (5, 'e');
INSERT INTO node VALUES (6, 'f');


--
-- Name: unique_id; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY node
    ADD CONSTRAINT unique_id PRIMARY KEY (id);


--
-- Name: public; Type: ACL; Schema: -; Owner: postgres
--

REVOKE ALL ON SCHEMA public FROM PUBLIC;
REVOKE ALL ON SCHEMA public FROM postgres;
GRANT ALL ON SCHEMA public TO postgres;
GRANT ALL ON SCHEMA public TO PUBLIC;


--
-- PostgreSQL database dump complete
--

