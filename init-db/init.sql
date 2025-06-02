--
-- PostgreSQL database dump
--

-- Dumped from database version 16.4 (Ubuntu 16.4-0ubuntu0.24.04.2)
-- Dumped by pg_dump version 16.4 (Ubuntu 16.4-0ubuntu0.24.04.2)

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: convert_type; Type: TABLE; Schema: public; Owner: daniel
--

CREATE TABLE public.convert_type (
    dtype character varying(31) NOT NULL,
    id bigint NOT NULL,
    created_on timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    max_value numeric(38,2),
    min_value numeric(38,2)
);


ALTER TABLE public.convert_type OWNER TO daniel;

--
-- Name: convert_type_sequence; Type: SEQUENCE; Schema: public; Owner: daniel
--

CREATE SEQUENCE public.convert_type_sequence
    START WITH 1
    INCREMENT BY 20
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.convert_type_sequence OWNER TO daniel;

--
-- Name: diet; Type: TABLE; Schema: public; Owner: daniel
--

CREATE TABLE public.diet (
    id bigint NOT NULL,
    created_on timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    finish date NOT NULL,
    start date NOT NULL,
    title character varying(255) NOT NULL,
    health_id bigint NOT NULL
);


ALTER TABLE public.diet OWNER TO daniel;

--
-- Name: diet_seq; Type: SEQUENCE; Schema: public; Owner: daniel
--

CREATE SEQUENCE public.diet_seq
    START WITH 1
    INCREMENT BY 5
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.diet_seq OWNER TO daniel;

--
-- Name: exercise; Type: TABLE; Schema: public; Owner: daniel
--

CREATE TABLE public.exercise (
    id bigint NOT NULL,
    created_on timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    title character varying(1000) NOT NULL,
    health_id bigint NOT NULL
);


ALTER TABLE public.exercise OWNER TO daniel;

--
-- Name: exercise_seq; Type: SEQUENCE; Schema: public; Owner: daniel
--

CREATE SEQUENCE public.exercise_seq
    START WITH 1
    INCREMENT BY 5
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.exercise_seq OWNER TO daniel;

--
-- Name: float_number; Type: TABLE; Schema: public; Owner: daniel
--

CREATE TABLE public.float_number (
    range integer NOT NULL,
    id bigint NOT NULL,
    CONSTRAINT ck_min_range CHECK ((range >= 1))
);


ALTER TABLE public.float_number OWNER TO daniel;

--
-- Name: food; Type: TABLE; Schema: public; Owner: daniel
--

CREATE TABLE public.food (
    id bigint NOT NULL,
    created_on timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    title character varying(255) NOT NULL,
    weight character varying(255),
    convert_type_id bigint,
    health_id bigint NOT NULL,
    measure_id bigint
);


ALTER TABLE public.food OWNER TO daniel;

--
-- Name: food_seq; Type: SEQUENCE; Schema: public; Owner: daniel
--

CREATE SEQUENCE public.food_seq
    START WITH 1
    INCREMENT BY 20
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.food_seq OWNER TO daniel;

--
-- Name: food_set; Type: TABLE; Schema: public; Owner: daniel
--

CREATE TABLE public.food_set (
    id bigint NOT NULL,
    created_on timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    title character varying(255) NOT NULL,
    health_id bigint NOT NULL
);


ALTER TABLE public.food_set OWNER TO daniel;

--
-- Name: food_set_food; Type: TABLE; Schema: public; Owner: daniel
--

CREATE TABLE public.food_set_food (
    food_set_id bigint NOT NULL,
    food_id bigint NOT NULL
);


ALTER TABLE public.food_set_food OWNER TO daniel;

--
-- Name: food_set_seq; Type: SEQUENCE; Schema: public; Owner: daniel
--

CREATE SEQUENCE public.food_set_seq
    START WITH 1
    INCREMENT BY 5
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.food_set_seq OWNER TO daniel;

--
-- Name: food_value; Type: TABLE; Schema: public; Owner: daniel
--

CREATE TABLE public.food_value (
    id bigint NOT NULL,
    created_on timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    value character varying(25) NOT NULL,
    food_id bigint NOT NULL,
    nutrition_value_id bigint NOT NULL,
    CONSTRAINT food_value_value_check CHECK (((value)::text ~ '^[0-9][0-9]*\.?[0-9]+$'::text))
);


ALTER TABLE public.food_value OWNER TO daniel;

--
-- Name: food_value_seq; Type: SEQUENCE; Schema: public; Owner: daniel
--

CREATE SEQUENCE public.food_value_seq
    START WITH 1
    INCREMENT BY 20
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.food_value_seq OWNER TO daniel;

--
-- Name: health; Type: TABLE; Schema: public; Owner: daniel
--

CREATE TABLE public.health (
    id bigint NOT NULL,
    user_id bigint NOT NULL
);


ALTER TABLE public.health OWNER TO daniel;

--
-- Name: health_sequence; Type: SEQUENCE; Schema: public; Owner: daniel
--

CREATE SEQUENCE public.health_sequence
    START WITH 1
    INCREMENT BY 20
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.health_sequence OWNER TO daniel;

--
-- Name: indicator; Type: TABLE; Schema: public; Owner: daniel
--

CREATE TABLE public.indicator (
    id bigint NOT NULL,
    created_on timestamp(6) with time zone NOT NULL,
    value character varying(100) NOT NULL,
    health_id bigint NOT NULL,
    indicator_type_id bigint NOT NULL,
    CONSTRAINT indicator_value_check CHECK (((value)::text ~ '^[0-9][0-9]*\.?[0-9]+$'::text))
);


ALTER TABLE public.indicator OWNER TO daniel;

--
-- Name: indicator_seq; Type: SEQUENCE; Schema: public; Owner: daniel
--

CREATE SEQUENCE public.indicator_seq
    START WITH 1
    INCREMENT BY 20
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.indicator_seq OWNER TO daniel;

--
-- Name: indicator_type; Type: TABLE; Schema: public; Owner: daniel
--

CREATE TABLE public.indicator_type (
    id bigint NOT NULL,
    created_on timestamp(6) with time zone NOT NULL,
    creator bigint NOT NULL,
    name character varying(500) NOT NULL,
    convert_type_id bigint NOT NULL,
    measure_id bigint NOT NULL,
    CONSTRAINT ck_indicator_type_name CHECK (((name)::text ~ '^[А-Я][а-я 0-9]+$'::text))
);


ALTER TABLE public.indicator_type OWNER TO daniel;

--
-- Name: indicator_type_sequence; Type: SEQUENCE; Schema: public; Owner: daniel
--

CREATE SEQUENCE public.indicator_type_sequence
    START WITH 1
    INCREMENT BY 5
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.indicator_type_sequence OWNER TO daniel;

--
-- Name: intake; Type: TABLE; Schema: public; Owner: daniel
--

CREATE TABLE public.intake (
    id bigint NOT NULL,
    day integer NOT NULL,
    "time" time(6) without time zone NOT NULL,
    weight character varying(255),
    convert_type_id bigint,
    measure_id bigint,
    plan_id bigint NOT NULL,
    CONSTRAINT intake_day_check CHECK (((day >= 0) AND (day <= 6)))
);


ALTER TABLE public.intake OWNER TO daniel;

--
-- Name: intake_result; Type: TABLE; Schema: public; Owner: daniel
--

CREATE TABLE public.intake_result (
    id bigint NOT NULL,
    created_on date NOT NULL,
    intake_id bigint NOT NULL
);


ALTER TABLE public.intake_result OWNER TO daniel;

--
-- Name: intake_result_seq; Type: SEQUENCE; Schema: public; Owner: daniel
--

CREATE SEQUENCE public.intake_result_seq
    START WITH 1
    INCREMENT BY 20
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.intake_result_seq OWNER TO daniel;

--
-- Name: intake_seq; Type: SEQUENCE; Schema: public; Owner: daniel
--

CREATE SEQUENCE public.intake_seq
    START WITH 1
    INCREMENT BY 20
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.intake_seq OWNER TO daniel;

--
-- Name: integer_number; Type: TABLE; Schema: public; Owner: daniel
--

CREATE TABLE public.integer_number (
    id bigint NOT NULL
);


ALTER TABLE public.integer_number OWNER TO daniel;

--
-- Name: meal; Type: TABLE; Schema: public; Owner: daniel
--

CREATE TABLE public.meal (
    id bigint NOT NULL,
    created_on timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    day smallint NOT NULL,
    "time" time(6) without time zone NOT NULL,
    diet_id bigint NOT NULL
);


ALTER TABLE public.meal OWNER TO daniel;

--
-- Name: meal_food; Type: TABLE; Schema: public; Owner: daniel
--

CREATE TABLE public.meal_food (
    id bigint NOT NULL,
    created_on timestamp(6) with time zone NOT NULL,
    weight real NOT NULL,
    food_id bigint NOT NULL,
    meal_id bigint NOT NULL,
    measure_id bigint NOT NULL
);


ALTER TABLE public.meal_food OWNER TO daniel;

--
-- Name: meal_food_seq; Type: SEQUENCE; Schema: public; Owner: daniel
--

CREATE SEQUENCE public.meal_food_seq
    START WITH 1
    INCREMENT BY 20
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.meal_food_seq OWNER TO daniel;

--
-- Name: meal_seq; Type: SEQUENCE; Schema: public; Owner: daniel
--

CREATE SEQUENCE public.meal_seq
    START WITH 1
    INCREMENT BY 5
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.meal_seq OWNER TO daniel;

--
-- Name: measure; Type: TABLE; Schema: public; Owner: daniel
--

CREATE TABLE public.measure (
    id bigint NOT NULL,
    type character varying(255) NOT NULL
);


ALTER TABLE public.measure OWNER TO daniel;

--
-- Name: measure_sequence; Type: SEQUENCE; Schema: public; Owner: daniel
--

CREATE SEQUENCE public.measure_sequence
    START WITH 1
    INCREMENT BY 20
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.measure_sequence OWNER TO daniel;

--
-- Name: medicine; Type: TABLE; Schema: public; Owner: daniel
--

CREATE TABLE public.medicine (
    id bigint NOT NULL,
    created_on timestamp(6) with time zone NOT NULL,
    name character varying(255) NOT NULL,
    weight character varying(255),
    convert_type_id bigint,
    health_id bigint NOT NULL,
    measure_id bigint
);


ALTER TABLE public.medicine OWNER TO daniel;

--
-- Name: medicine_seq; Type: SEQUENCE; Schema: public; Owner: daniel
--

CREATE SEQUENCE public.medicine_seq
    START WITH 1
    INCREMENT BY 20
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.medicine_seq OWNER TO daniel;

--
-- Name: nutrition_value; Type: TABLE; Schema: public; Owner: daniel
--

CREATE TABLE public.nutrition_value (
    id bigint NOT NULL,
    created_on timestamp(6) with time zone NOT NULL,
    value character varying(255) NOT NULL,
    convert_type_id bigint NOT NULL,
    measure_id bigint NOT NULL,
    CONSTRAINT nutrition_value_value_check CHECK (((value)::text = ANY ((ARRAY['CALORIE'::character varying, 'FAT'::character varying, 'PROTEIN'::character varying, 'SUGAR'::character varying, 'CARBOHYDRATE'::character varying])::text[])))
);


ALTER TABLE public.nutrition_value OWNER TO daniel;

--
-- Name: nutrition_value_sequence; Type: SEQUENCE; Schema: public; Owner: daniel
--

CREATE SEQUENCE public.nutrition_value_sequence
    START WITH 1
    INCREMENT BY 20
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.nutrition_value_sequence OWNER TO daniel;

--
-- Name: plan; Type: TABLE; Schema: public; Owner: daniel
--

CREATE TABLE public.plan (
    id bigint NOT NULL,
    finish date NOT NULL,
    sequence character varying(255),
    start date NOT NULL,
    health_id bigint NOT NULL,
    medicine_id bigint NOT NULL,
    treatment_id bigint NOT NULL,
    CONSTRAINT plan_sequence_check CHECK (((sequence)::text = ANY ((ARRAY['BEFORE_EAT'::character varying, 'AFTER_EAT'::character varying])::text[])))
);


ALTER TABLE public.plan OWNER TO daniel;

--
-- Name: plan_seq; Type: SEQUENCE; Schema: public; Owner: daniel
--

CREATE SEQUENCE public.plan_seq
    START WITH 1
    INCREMENT BY 20
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.plan_seq OWNER TO daniel;

--
-- Name: set; Type: TABLE; Schema: public; Owner: daniel
--

CREATE TABLE public.set (
    id bigint NOT NULL,
    count integer NOT NULL,
    repeat integer NOT NULL,
    exercise_id bigint NOT NULL,
    train_id bigint NOT NULL
);


ALTER TABLE public.set OWNER TO daniel;

--
-- Name: set_result; Type: TABLE; Schema: public; Owner: daniel
--

CREATE TABLE public.set_result (
    id bigint NOT NULL,
    actual_repeat integer NOT NULL,
    created_on timestamp(6) with time zone NOT NULL,
    executed_date date NOT NULL,
    set_number integer NOT NULL,
    set_id bigint NOT NULL
);


ALTER TABLE public.set_result OWNER TO daniel;

--
-- Name: set_result_seq; Type: SEQUENCE; Schema: public; Owner: daniel
--

CREATE SEQUENCE public.set_result_seq
    START WITH 1
    INCREMENT BY 20
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.set_result_seq OWNER TO daniel;

--
-- Name: set_seq; Type: SEQUENCE; Schema: public; Owner: daniel
--

CREATE SEQUENCE public.set_seq
    START WITH 1
    INCREMENT BY 20
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.set_seq OWNER TO daniel;

--
-- Name: sport; Type: TABLE; Schema: public; Owner: daniel
--

CREATE TABLE public.sport (
    id bigint NOT NULL,
    description character varying(1000) NOT NULL,
    finish date NOT NULL,
    start date NOT NULL,
    health_id bigint NOT NULL
);


ALTER TABLE public.sport OWNER TO daniel;

--
-- Name: sport_seq; Type: SEQUENCE; Schema: public; Owner: daniel
--

CREATE SEQUENCE public.sport_seq
    START WITH 1
    INCREMENT BY 20
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.sport_seq OWNER TO daniel;

--
-- Name: step; Type: TABLE; Schema: public; Owner: daniel
--

CREATE TABLE public.step (
    id bigint NOT NULL,
    description character varying(2000) NOT NULL,
    exercise_id bigint NOT NULL
);


ALTER TABLE public.step OWNER TO daniel;

--
-- Name: step_seq; Type: SEQUENCE; Schema: public; Owner: daniel
--

CREATE SEQUENCE public.step_seq
    START WITH 1
    INCREMENT BY 20
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.step_seq OWNER TO daniel;

--
-- Name: train; Type: TABLE; Schema: public; Owner: daniel
--

CREATE TABLE public.train (
    id bigint NOT NULL,
    day integer NOT NULL,
    description character varying(1000),
    "time" time(6) without time zone NOT NULL,
    sport_id bigint NOT NULL
);


ALTER TABLE public.train OWNER TO daniel;

--
-- Name: train_seq; Type: SEQUENCE; Schema: public; Owner: daniel
--

CREATE SEQUENCE public.train_seq
    START WITH 1
    INCREMENT BY 20
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.train_seq OWNER TO daniel;

--
-- Name: treatment; Type: TABLE; Schema: public; Owner: daniel
--

CREATE TABLE public.treatment (
    id bigint NOT NULL,
    created_on timestamp(6) with time zone NOT NULL,
    description character varying(1000) NOT NULL,
    health_id bigint NOT NULL
);


ALTER TABLE public.treatment OWNER TO daniel;

--
-- Name: treatment_seq; Type: SEQUENCE; Schema: public; Owner: daniel
--

CREATE SEQUENCE public.treatment_seq
    START WITH 1
    INCREMENT BY 20
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.treatment_seq OWNER TO daniel;

--
-- Data for Name: convert_type; Type: TABLE DATA; Schema: public; Owner: daniel
--

COPY public.convert_type (dtype, id, created_on, max_value, min_value) FROM stdin;
float_numb	1	2025-04-04 21:43:46.74468	\N	\N
\.


--
-- Data for Name: diet; Type: TABLE DATA; Schema: public; Owner: daniel
--

COPY public.diet (id, created_on, finish, start, title, health_id) FROM stdin;
37	2025-04-11 21:14:46.401518	2025-04-20	2025-04-01	Монохромная	1
42	2025-04-11 21:43:06.434518	2025-04-03	2025-03-31	Тест диета 1	1
47	2025-04-12 22:07:30.817281	2025-04-16	2025-04-02	Тестовая диета	1
52	2025-04-15 18:30:13.368673	2025-04-28	2025-04-21	Test2	1
57	2025-04-15 19:44:40.361718	2025-05-04	2025-04-26	newDiet	1
62	2025-05-22 20:49:15.620933	2025-06-08	2025-05-19	Кето	1
67	2025-05-26 21:34:30.88177	2025-06-01	2025-05-19	Диета Аткинса	1
\.


--
-- Data for Name: exercise; Type: TABLE DATA; Schema: public; Owner: daniel
--

COPY public.exercise (id, created_on, title, health_id) FROM stdin;
1	2025-04-16 20:02:58.757933	Подтягивания	1
2	2025-04-16 20:10:43.333484	Отжимания от пола	1
3	2025-04-16 20:14:27.787706	Скакалка	1
7	2025-04-17 10:39:27.723021	Жим штанги лёжа	1
8	2025-04-17 10:39:58.167892	Жим с гантелями под углом 30°	1
9	2025-04-17 10:40:33.121822	Жим в тренажёре типа "Хаммер" 4 подхода по 12 повторений в режиме прямой пирамиды	1
10	2025-04-17 10:41:06.768189	"Разводка" с гантелями на горизонтальной скамье	1
11	2025-04-17 10:41:46.218627	Приседания	1
12	2025-04-17 10:42:00.407555	Выпады со штангой	1
13	2025-04-17 10:42:20.042189	Сгибания ног в тренажёре	1
14	2025-04-17 10:43:40.511677	Сгибания ног в тренажёре	1
15	2025-04-17 10:43:54.271558	Жим для ног в тренажёре	1
16	2025-04-17 10:44:08.840456	Подъёмы на голень	1
17	2025-04-17 10:45:11.942721	Тяга "Т-грифа"	1
18	2025-04-17 10:45:26.944412	Тяга гантели одной рукой к поясу в наклоне	1
19	2025-04-17 10:45:39.265516	Рычажная тяга сверху	1
20	2025-04-17 10:45:54.558029	Армейский жим со штангой стоя	1
21	2025-04-17 10:46:06.917177	Протяжка со штангой	1
22	2025-04-17 10:46:28.364786	Махи с гантелями в наклоне	1
23	2025-04-17 10:46:45.557701	Подъём штанги на бицепс стоя	1
24	2025-04-17 10:47:02.360117	"Молоты" стоя попеременно:	1
25	2025-04-17 10:47:15.169728	Сгибания рук с гантелями сидя попеременно	1
\.


--
-- Data for Name: float_number; Type: TABLE DATA; Schema: public; Owner: daniel
--

COPY public.float_number (range, id) FROM stdin;
1	1
\.


--
-- Data for Name: food; Type: TABLE DATA; Schema: public; Owner: daniel
--

COPY public.food (id, created_on, title, weight, convert_type_id, health_id, measure_id) FROM stdin;
42	2025-04-10 06:50:59.354569	Плов	\N	\N	1	\N
62	2025-04-10 07:29:57.277731	Борщ	\N	\N	1	\N
63	2025-04-10 07:37:42.171004	Test	\N	\N	1	\N
64	2025-04-10 07:38:09.943083	Test2	\N	\N	1	\N
65	2025-04-10 07:39:33.386704	test3	\N	\N	1	\N
66	2025-04-10 07:41:21.029616	test55	\N	\N	1	\N
67	2025-04-10 07:52:00.970192	test78	\N	\N	1	\N
68	2025-04-10 07:52:51.91797	teee	\N	\N	1	\N
69	2025-04-10 07:53:09.257854	gdfgfd	\N	\N	1	\N
70	2025-04-10 07:54:00.933063	dsdsfd	\N	\N	1	\N
82	2025-04-10 07:59:35.253692	Gtttt	\N	\N	1	\N
83	2025-04-10 08:01:04.63339	tessss	\N	\N	1	\N
84	2025-04-10 08:01:36.794157	dwadwadawd	\N	\N	1	\N
85	2025-04-10 08:02:15.176008	fdfdvfv	\N	\N	1	\N
86	2025-04-10 08:03:02.636492	vvvcvc	\N	\N	1	\N
87	2025-04-10 08:04:07.272483	ghgjghj	\N	\N	1	\N
88	2025-04-10 08:05:10.749737	newww	\N	\N	1	\N
89	2025-04-10 08:05:51.439025	вес	\N	\N	1	\N
102	2025-05-22 20:08:15.460325	Рис	\N	\N	1	\N
122	2025-05-22 20:18:00.277616	Банан	\N	\N	1	\N
123	2025-05-22 20:22:33.206921	Омлет с сыром	\N	\N	1	\N
124	2025-05-22 20:23:47.816376	Салат с рукколой	\N	\N	1	\N
127	2025-05-22 20:33:06.435037	Авокадо	\N	\N	1	\N
128	2025-05-22 20:34:30.610567	Запечённые овощи	\N	\N	1	\N
129	2025-05-22 20:35:26.810838	Котлета из говядины	\N	\N	1	\N
133	2025-05-22 20:43:41.562412	мини бургер с листьями салата	\N	\N	1	\N
\.


--
-- Data for Name: food_set; Type: TABLE DATA; Schema: public; Owner: daniel
--

COPY public.food_set (id, created_on, title, health_id) FROM stdin;
\.


--
-- Data for Name: food_set_food; Type: TABLE DATA; Schema: public; Owner: daniel
--

COPY public.food_set_food (food_set_id, food_id) FROM stdin;
\.


--
-- Data for Name: food_value; Type: TABLE DATA; Schema: public; Owner: daniel
--

COPY public.food_value (id, created_on, value, food_id, nutrition_value_id) FROM stdin;
22	2025-04-10 06:50:59.420793	12	42	2
23	2025-04-10 06:50:59.451657	17	42	5
24	2025-04-10 06:50:59.466368	325.3	42	1
42	2025-04-10 07:29:57.377998	57.7	62	1
43	2025-04-10 07:29:57.399449	3.8	62	4
44	2025-04-10 07:29:57.412802	2.9	62	2
45	2025-04-10 07:29:57.423737	4.3	62	3
46	2025-04-10 07:37:42.192649	12	63	1
47	2025-04-10 07:38:09.966816	14	64	1
48	2025-04-10 07:39:33.413685	10	65	1
49	2025-04-10 07:41:21.052438	14	66	1
50	2025-04-10 07:52:00.990387	123	67	4
51	2025-04-10 07:52:51.944098	12	68	1
52	2025-04-10 07:53:09.280664	14	69	1
53	2025-04-10 07:54:00.945917	123	70	1
62	2025-04-10 07:59:35.35642	147	82	1
63	2025-04-10 08:01:04.652864	144	83	1
64	2025-04-10 08:01:36.811711	15	84	1
65	2025-04-10 08:02:15.192378	1444	85	1
66	2025-04-10 08:03:03.091222	1414	86	1
67	2025-04-10 08:04:07.287028	12	87	1
68	2025-04-10 08:05:10.762686	142	88	1
69	2025-04-10 08:05:51.458237	23	89	2
82	2025-05-22 20:08:15.597102	360	102	1
83	2025-05-22 20:08:15.745325	79.34	102	3
84	2025-05-22 20:08:15.790372	6.61	102	4
102	2025-05-22 20:18:00.433046	89	122	1
103	2025-05-22 20:18:00.487373	23	122	3
104	2025-05-22 20:18:00.521539	1.1	122	4
105	2025-05-22 20:22:33.235362	154	123	1
106	2025-05-22 20:22:33.258531	12.2	123	4
107	2025-05-22 20:22:33.281345	18.4	123	2
108	2025-05-22 20:23:47.836817	79.5	124	1
109	2025-05-22 20:23:47.856061	7.2	124	2
110	2025-05-22 20:23:47.875509	3.1	124	3
117	2025-05-22 20:33:06.457352	200	127	1
118	2025-05-22 20:33:06.479026	14.7	127	2
119	2025-05-22 20:33:06.495703	1.83	127	3
120	2025-05-22 20:34:30.618997	33.4	128	1
121	2025-05-22 20:34:30.628024	5.32	128	3
122	2025-05-22 20:34:30.637759	5.8	128	2
123	2025-05-22 20:35:26.837593	250	129	1
124	2025-05-22 20:35:26.851549	15	129	2
125	2025-05-22 20:35:26.865052	3.4	129	3
135	2025-05-22 20:43:41.575987	230	133	1
136	2025-05-22 20:43:41.59704	15	133	2
137	2025-05-22 20:43:41.610336	5.3	133	3
\.


--
-- Data for Name: health; Type: TABLE DATA; Schema: public; Owner: daniel
--

COPY public.health (id, user_id) FROM stdin;
1	1
2	22
6	23
\.


--
-- Data for Name: indicator; Type: TABLE DATA; Schema: public; Owner: daniel
--

COPY public.indicator (id, created_on, value, health_id, indicator_type_id) FROM stdin;
1	2025-04-05 00:45:07.095+03	80	1	1
2	2025-04-01 00:45:07.095+03	75	1	1
22	2025-04-01 00:49:03.008+03	175	1	2
23	2025-04-05 00:49:03.008+03	180	1	2
24	2025-03-25 12:54:49.919+03	4.2	1	3
42	2025-04-25 03:25:10.614+03	85	1	1
62	2025-04-06 00:58:55.612+03	4.0	1	3
82	2025-04-06 02:23:39.005+03	190	1	2
83	2025-04-30 02:24:14.165+03	190	1	2
102	2025-04-06 22:21:36.609+03	50	1	2
122	2025-04-06 22:28:12.753+03	4.5	1	3
142	2025-04-22 22:29:01.014+03	4.6	1	3
143	2025-05-29 22:29:01.014+03	98	1	1
144	2025-05-05 01:29:01.014+03	85	1	1
145	2025-07-07 02:08:14.537+03	5.6	1	3
146	2025-07-07 02:09:03.823+03	5.6	1	3
162	2025-05-22 22:19:26.658+03	5.9	2	3
182	2025-05-27 19:06:39.411+03	5.9	1	3
\.


--
-- Data for Name: indicator_type; Type: TABLE DATA; Schema: public; Owner: daniel
--

COPY public.indicator_type (id, created_on, creator, name, convert_type_id, measure_id) FROM stdin;
1	2025-04-05 00:43:46.776864+03	1	Вес	1	2
2	2025-04-05 00:43:46.792034+03	1	Рост	1	1
3	2025-04-05 00:43:46.801675+03	1	Уровень сахара	1	3
\.


--
-- Data for Name: intake; Type: TABLE DATA; Schema: public; Owner: daniel
--

COPY public.intake (id, day, "time", weight, convert_type_id, measure_id, plan_id) FROM stdin;
1	1	12:30:00	250	\N	163	1
2	4	20:00:00	500	\N	163	1
22	1	08:30:00	300	\N	163	2
23	3	13:00:00	150	\N	163	2
24	1	15:00:00	100	\N	163	2
25	5	10:00:00	150	\N	163	2
26	5	15:30:00	130	\N	163	2
27	1	08:30:00	250	\N	163	3
42	1	08:00:00	12	\N	163	22
43	1	15:00:00	12	\N	163	22
44	3	12:00:00	12	\N	163	22
45	3	18:00:00	12	\N	163	22
46	5	12:00:00	12	\N	163	22
47	5	15:00:00	12	\N	163	22
48	2	10:00:00	12	\N	163	22
49	2	16:00:00	12	\N	163	22
50	6	08:00:00	12	\N	163	22
51	6	19:00:00	12	\N	163	22
28	2	13:30:00	12	\N	163	3
29	6	14:00:00	18	\N	163	3
30	4	23:00:00	24	\N	163	3
\.


--
-- Data for Name: intake_result; Type: TABLE DATA; Schema: public; Owner: daniel
--

COPY public.intake_result (id, created_on, intake_id) FROM stdin;
5	2025-05-15	2
22	2025-05-15	30
42	2025-05-16	26
43	2025-05-16	25
44	2025-05-14	23
45	2025-05-13	28
46	2025-05-12	24
62	2025-05-27	28
\.


--
-- Data for Name: integer_number; Type: TABLE DATA; Schema: public; Owner: daniel
--

COPY public.integer_number (id) FROM stdin;
\.


--
-- Data for Name: meal; Type: TABLE DATA; Schema: public; Owner: daniel
--

COPY public.meal (id, created_on, day, "time", diet_id) FROM stdin;
22	2025-04-11 21:14:46.467762	1	09:20:00	37
23	2025-04-11 21:14:46.524509	1	12:20:00	37
24	2025-04-11 21:14:46.567875	1	15:50:00	37
25	2025-04-11 21:14:46.592239	3	08:20:00	37
26	2025-04-11 21:14:46.62592	3	13:20:00	37
27	2025-04-11 21:14:46.647285	4	04:30:00	37
28	2025-04-11 21:14:46.664375	4	13:25:00	37
32	2025-04-11 21:43:06.568986	1	12:30:00	42
33	2025-04-11 21:43:06.640381	4	12:45:00	42
37	2025-04-12 22:07:30.963974	1	04:00:00	47
38	2025-04-12 22:07:31.057011	1	08:50:00	47
39	2025-04-12 22:07:31.084914	3	15:00:00	47
40	2025-04-12 22:07:31.105857	7	08:30:00	47
41	2025-04-12 22:07:31.142124	7	13:30:00	47
42	2025-04-12 22:07:31.177091	7	18:50:00	47
50	2025-04-15 18:30:13.662337	2	08:40:00	52
51	2025-04-15 18:30:13.695212	2	18:30:00	52
52	2025-04-15 18:30:13.718114	5	14:40:00	52
53	2025-04-15 18:30:13.738134	5	19:40:00	52
57	2025-04-15 19:44:40.488883	1	08:35:00	57
58	2025-04-15 19:44:40.555355	1	14:00:00	57
59	2025-04-15 19:44:40.590323	5	08:00:00	57
60	2025-04-15 19:44:40.620467	5	15:00:00	57
47	2025-04-15 18:30:13.504541	1	08:20:00	52
48	2025-04-15 18:30:13.61235	1	15:20:00	52
49	2025-04-15 18:30:13.639044	1	18:30:00	52
62	2025-05-22 20:49:15.830865	1	08:30:00	62
63	2025-05-22 20:49:15.965302	1	15:30:00	62
64	2025-05-22 20:49:16.003207	1	18:30:00	62
65	2025-05-22 20:49:16.06395	2	10:00:00	62
66	2025-05-22 20:49:16.102392	2	15:00:00	62
67	2025-05-22 20:49:16.154602	2	18:00:00	62
72	2025-05-26 21:34:31.209644	1	08:30:00	67
73	2025-05-26 21:34:31.476754	1	15:30:00	67
74	2025-05-26 21:34:31.541549	1	19:00:00	67
75	2025-05-26 21:34:31.650227	2	10:00:00	67
76	2025-05-26 21:34:31.73183	2	13:00:00	67
77	2025-05-26 21:34:31.801678	2	18:00:00	67
78	2025-05-26 21:34:31.867931	3	06:00:00	67
79	2025-05-26 21:34:31.950612	3	15:00:00	67
80	2025-05-26 21:34:32.011522	3	20:00:00	67
81	2025-05-26 21:34:32.067191	4	12:00:00	67
82	2025-05-26 21:34:32.108487	4	19:00:00	67
83	2025-05-26 21:34:32.202437	5	12:00:00	67
84	2025-05-26 21:34:32.253925	5	15:20:00	67
85	2025-05-26 21:34:32.307378	5	18:00:00	67
86	2025-05-26 21:34:32.356084	6	08:10:00	67
87	2025-05-26 21:34:32.402145	6	12:30:00	67
88	2025-05-26 21:34:32.459202	7	09:00:00	67
89	2025-05-26 21:34:32.511086	7	15:00:00	67
\.


--
-- Data for Name: meal_food; Type: TABLE DATA; Schema: public; Owner: daniel
--

COPY public.meal_food (id, created_on, weight, food_id, meal_id, measure_id) FROM stdin;
42	2025-04-12 00:14:46.494739+03	120	42	22	42
43	2025-04-12 00:14:46.539338+03	50	89	23	42
44	2025-04-12 00:14:46.556782+03	120	66	23	42
45	2025-04-12 00:14:46.579039+03	240	85	24	42
46	2025-04-12 00:14:46.603297+03	430	42	25	42
47	2025-04-12 00:14:46.618613+03	180	83	25	42
48	2025-04-12 00:14:46.637912+03	200	67	26	42
49	2025-04-12 00:14:46.656265+03	180.3	83	27	42
50	2025-04-12 00:14:46.671421+03	150	42	28	42
51	2025-04-12 00:14:46.684365+03	150	86	28	42
62	2025-04-12 00:43:06.603608+03	150	62	32	42
63	2025-04-12 00:43:06.651987+03	256	86	33	42
82	2025-04-13 01:07:31.022351+03	123	42	37	42
83	2025-04-13 01:07:31.071129+03	300	64	38	42
84	2025-04-13 01:07:31.095523+03	160	62	39	42
85	2025-04-13 01:07:31.115351+03	250	42	40	42
86	2025-04-13 01:07:31.132616+03	150	89	40	42
87	2025-04-13 01:07:31.156315+03	400	62	41	42
88	2025-04-13 01:07:31.191567+03	125	83	42	42
89	2025-04-13 01:07:31.208532+03	400	84	42	42
102	2025-04-15 21:30:13.55324+03	500.8	63	47	42
103	2025-04-15 21:30:13.627216+03	450	62	48	42
104	2025-04-15 21:30:13.651103+03	300	42	49	42
105	2025-04-15 21:30:13.672031+03	325	42	50	42
106	2025-04-15 21:30:13.686491+03	200	62	50	42
107	2025-04-15 21:30:13.705724+03	600	66	51	42
108	2025-04-15 21:30:13.727559+03	210	42	52	42
109	2025-04-15 21:30:13.749387+03	400	83	53	42
122	2025-04-15 22:44:40.517593+03	700	62	57	42
123	2025-04-15 22:44:40.573211+03	1100	64	58	42
124	2025-04-15 22:44:40.605387+03	425	42	59	42
125	2025-04-15 22:44:40.638321+03	125	68	60	42
142	2025-05-22 23:49:15.893367+03	150	102	62	42
143	2025-05-22 23:49:15.93572+03	70	122	62	42
144	2025-05-22 23:49:15.980302+03	250	62	63	42
145	2025-05-22 23:49:16.022525+03	120	129	64	42
146	2025-05-22 23:49:16.053616+03	150	128	64	42
147	2025-05-22 23:49:16.083835+03	160	42	65	42
148	2025-05-22 23:49:16.127142+03	120	133	66	42
149	2025-05-22 23:49:16.175083+03	130	129	67	42
162	2025-05-27 00:34:31.302364+03	130	62	72	42
163	2025-05-27 00:34:31.419861+03	40	127	72	42
164	2025-05-27 00:34:31.510419+03	250	133	73	42
165	2025-05-27 00:34:31.575057+03	150	129	74	42
166	2025-05-27 00:34:31.624333+03	100	128	74	42
167	2025-05-27 00:34:31.698987+03	300	42	75	42
168	2025-05-27 00:34:31.768765+03	220	123	76	42
169	2025-05-27 00:34:31.828309+03	350	128	77	42
170	2025-05-27 00:34:31.889322+03	50	127	78	42
171	2025-05-27 00:34:31.931035+03	350	42	78	42
172	2025-05-27 00:34:31.98005+03	300	62	79	42
173	2025-05-27 00:34:32.039354+03	360	102	80	42
174	2025-05-27 00:34:32.082917+03	300	133	81	42
175	2025-05-27 00:34:32.137951+03	120	122	82	42
176	2025-05-27 00:34:32.176228+03	260	128	82	42
177	2025-05-27 00:34:32.223107+03	350	128	83	42
178	2025-05-27 00:34:32.281461+03	120	102	84	42
179	2025-05-27 00:34:32.327534+03	120	123	85	42
180	2025-05-27 00:34:32.378747+03	130	133	86	42
181	2025-05-27 00:34:32.4249+03	200	129	87	42
182	2025-05-27 00:34:32.485134+03	160	42	88	42
183	2025-05-27 00:34:32.534577+03	260	62	89	42
\.


--
-- Data for Name: measure; Type: TABLE DATA; Schema: public; Owner: daniel
--

COPY public.measure (id, type) FROM stdin;
1	CANTIMETRES
2	KG
3	MOLL_LITRES
42	GRAM
162	MILLILITRES
163	MILLIGRAM
\.


--
-- Data for Name: medicine; Type: TABLE DATA; Schema: public; Owner: daniel
--

COPY public.medicine (id, created_on, name, weight, convert_type_id, health_id, measure_id) FROM stdin;
22	2025-05-15 00:05:19.346604+03	Парацетамол	500	\N	1	42
23	2025-05-15 00:12:16.076456+03	Афобазол	10	\N	1	162
24	2025-05-15 00:13:51.305124+03	Тербинафин-Вертекс	30	\N	1	163
142	2025-05-23 13:58:24.401883+03	Валидол	60	\N	1	163
162	2025-05-23 14:03:20.251619+03	Кагоцел	12	\N	1	163
163	2025-05-23 14:04:44.241659+03	Називин	10	\N	1	162
164	2025-05-23 14:10:16.509915+03	Антигриппин	500	\N	1	163
802	2025-05-27 23:27:29.22873+03	Пустырник	10	\N	1	163
\.


--
-- Data for Name: nutrition_value; Type: TABLE DATA; Schema: public; Owner: daniel
--

COPY public.nutrition_value (id, created_on, value, convert_type_id, measure_id) FROM stdin;
1	2025-04-08 00:45:58.426276+03	CALORIE	1	42
2	2025-04-08 00:45:58.426299+03	FAT	1	42
3	2025-04-08 00:45:58.426301+03	CARBOHYDRATE	1	42
4	2025-04-08 00:45:58.426301+03	PROTEIN	1	42
5	2025-04-08 00:45:58.426302+03	SUGAR	1	42
\.


--
-- Data for Name: plan; Type: TABLE DATA; Schema: public; Owner: daniel
--

COPY public.plan (id, finish, sequence, start, health_id, medicine_id, treatment_id) FROM stdin;
1	2025-05-30	AFTER_EAT	2025-05-12	1	22	23
2	2025-05-31	AFTER_EAT	2025-05-05	1	23	42
22	2025-05-25	AFTER_EAT	2025-05-19	1	162	62
3	2025-06-08	AFTER_EAT	2025-05-05	1	162	42
\.


--
-- Data for Name: set; Type: TABLE DATA; Schema: public; Owner: daniel
--

COPY public.set (id, count, repeat, exercise_id, train_id) FROM stdin;
1	5	5	7	1
2	4	10	8	1
3	4	12	9	1
22	5	12	11	2
23	3	12	12	2
24	4	15	14	2
25	5	15	8	3
26	4	12	7	3
27	7	20	10	3
28	4	8	9	3
29	2	25	1	4
30	3	10	18	4
31	4	15	19	4
32	3	10	2	4
33	4	10	20	5
34	4	12	21	5
35	6	10	22	5
42	5	5	7	22
43	4	10	8	22
44	3	12	10	22
45	5	12	11	23
46	3	12	12	23
47	4	10	14	23
48	3	20	15	23
49	3	20	1	24
50	4	12	17	24
51	3	11	18	24
52	4	10	20	25
53	4	8	21	25
54	5	10	22	25
\.


--
-- Data for Name: set_result; Type: TABLE DATA; Schema: public; Owner: daniel
--

COPY public.set_result (id, actual_repeat, created_on, executed_date, set_number, set_id) FROM stdin;
1	20	2025-04-18 19:24:00.791485+03	2025-04-18	1	29
2	10	2025-04-19 23:43:35.033489+03	2025-04-14	1	22
3	12	2025-04-19 23:44:02.647575+03	2025-04-14	2	22
22	11	2025-04-19 23:53:50.615514+03	2025-04-21	1	22
23	12	2025-04-19 23:53:59.516629+03	2025-04-21	2	22
42	12	2025-04-20 15:20:07.424593+03	2025-04-23	1	26
43	11	2025-04-20 15:20:31.918233+03	2025-04-23	2	26
62	10	2025-05-23 13:38:22.281979+03	2025-05-19	1	42
63	5	2025-05-23 13:39:02.201781+03	2025-05-19	2	42
64	5	2025-05-23 13:39:08.801687+03	2025-05-19	3	42
65	5	2025-05-23 13:39:13.02478+03	2025-05-19	4	42
66	5	2025-05-23 13:41:35.567097+03	2025-05-19	5	42
67	10	2025-05-23 13:41:53.127301+03	2025-05-19	1	43
68	10	2025-05-23 13:42:03.129231+03	2025-05-19	2	43
69	10	2025-05-23 13:42:12.0489+03	2025-05-19	3	43
70	10	2025-05-23 13:42:38.910362+03	2025-05-19	4	43
71	13	2025-05-23 13:43:00.971875+03	2025-05-19	1	44
72	12	2025-05-23 13:43:08.093604+03	2025-05-19	2	44
73	10	2025-05-23 13:51:10.194179+03	2025-05-21	1	45
74	11	2025-05-23 13:51:26.105427+03	2025-05-21	2	45
75	11	2025-05-23 13:52:37.005796+03	2025-05-21	3	45
76	11	2025-05-23 13:52:46.399944+03	2025-05-21	4	45
77	11	2025-05-23 13:52:51.296337+03	2025-05-21	5	45
722	11	2025-05-27 04:47:15.610987+03	2025-05-14	1	45
723	15	2025-05-27 04:53:14.46246+03	2025-05-23	1	49
\.


--
-- Data for Name: sport; Type: TABLE DATA; Schema: public; Owner: daniel
--

COPY public.sport (id, description, finish, start, health_id) FROM stdin;
1	Программа сплит	2025-05-22	2025-05-10	1
2	Программа сплит	2025-05-22	2025-05-13	1
42	Программа сплит	2025-06-08	2025-05-19	1
62	Программа сплит	2025-06-08	2025-05-19	1
22	Сплит сплитом	2025-05-22	2025-05-19	1
\.


--
-- Data for Name: step; Type: TABLE DATA; Schema: public; Owner: daniel
--

COPY public.step (id, description, exercise_id) FROM stdin;
1	Полностью выпрямляйте руки в нижней точке. Стремитесь доводить подтягивание до конца в верхней точке	1
2	Держите корпус ровно	1
3	Сохраняйте тонус мышц в нижней точке подтягивания	1
4	Не задерживайте дыхание	1
22	Начиная сгибать руки, опустись вниз до касания грудью пола	2
23	Локти не разводи сильно в стороны, они должны находиться под углом 45 градусов к корпусу.	2
24	С умеренной скоростью возвращайся в обратное положение	2
25	Руки в верхней точке держи чуть согнутыми, так мышцы находятся в напряжении в течение всего подхода	2
26	Пресс напрягай во избежание осевой нагрузки на позвоночный столб, это предупреждает риск травмы	2
27	Помни о дыхании - опускайся на вдохе, отжимайся от пола на выдохе	2
28	Примите исходное положение, возьмите рукоятки скакалки в руки, трос за спиной, под коленями. Слегка согните руки в локтях и отведите их вперёд	3
29	Сделайте замах и прыжок. Снова примите исходное положение. Какое-то время выполняйте комбинацию «замах — прыжок — остановка». Это поможет вам отработать навык	3
30	Далее начинайте осваивать серию из двух, пяти и более прыжков	3
42	Лег	7
43	Обхвати	7
44	Поднимай	7
45	первый шаг	8
46	Второй шаг	8
47	Третий шаг	8
48	первое чтото	9
49	Второй чтото	9
50	Третий чтото	9
51	Первый шаг	10
52	ВТоро шаг	10
53	цфвфцв	11
54	вапавпавп	11
55	выфвфыв	12
56	мавмвамвапвп	12
57	вуфвфца	13
58	вамвамвапвп	13
59	выавыавыаыа	14
60	вцфвцфвфцвфцв	14
61	вфвцфвцф	15
62	впвапавпвап	15
63	ыаыаываыва	16
64	выфвфывфв	16
65	вцфвфвцфцв	17
66	ваыавыавыа	17
67	вфвфцвфцв	18
68	вцфвфцвфцвфвфвццфв	19
69	ыфвыфвфыв	20
70	авыаыаываыа	21
71	вывяывяывв	22
72	вцвфавыавыа	23
73	фвфвыфвфыв	24
74	вфвцфвфцв	25
\.


--
-- Data for Name: train; Type: TABLE DATA; Schema: public; Owner: daniel
--

COPY public.train (id, day, description, "time", sport_id) FROM stdin;
1	1	Грудь и трицепс	12:30:00	2
2	1	Ноги и пресс	12:00:00	22
3	3	Грудь и трицепс	16:30:00	22
4	5	Спина и трапеция	15:00:00	22
5	2	Дельта и бицепсы	16:00:00	22
22	1	Грудь и трицепс	12:00:00	62
23	3	Ноги	15:00:00	62
24	5	Спина и трапеция	18:00:00	62
25	7	Дельты и бицепсы	10:00:00	62
\.


--
-- Data for Name: treatment; Type: TABLE DATA; Schema: public; Owner: daniel
--

COPY public.treatment (id, created_on, description, health_id) FROM stdin;
23	2025-05-15 19:36:55.006117+03	Кишечная палочка	1
42	2025-05-15 21:47:34.775745+03	ОРВИ	1
62	2025-05-23 14:14:23.438285+03	Грипп	1
\.


--
-- Name: convert_type_sequence; Type: SEQUENCE SET; Schema: public; Owner: daniel
--

SELECT pg_catalog.setval('public.convert_type_sequence', 1, true);


--
-- Name: diet_seq; Type: SEQUENCE SET; Schema: public; Owner: daniel
--

SELECT pg_catalog.setval('public.diet_seq', 71, true);


--
-- Name: exercise_seq; Type: SEQUENCE SET; Schema: public; Owner: daniel
--

SELECT pg_catalog.setval('public.exercise_seq', 31, true);


--
-- Name: food_seq; Type: SEQUENCE SET; Schema: public; Owner: daniel
--

SELECT pg_catalog.setval('public.food_seq', 141, true);


--
-- Name: food_set_seq; Type: SEQUENCE SET; Schema: public; Owner: daniel
--

SELECT pg_catalog.setval('public.food_set_seq', 1, false);


--
-- Name: food_value_seq; Type: SEQUENCE SET; Schema: public; Owner: daniel
--

SELECT pg_catalog.setval('public.food_value_seq', 161, true);


--
-- Name: health_sequence; Type: SEQUENCE SET; Schema: public; Owner: daniel
--

SELECT pg_catalog.setval('public.health_sequence', 21, true);


--
-- Name: indicator_seq; Type: SEQUENCE SET; Schema: public; Owner: daniel
--

SELECT pg_catalog.setval('public.indicator_seq', 201, true);


--
-- Name: indicator_type_sequence; Type: SEQUENCE SET; Schema: public; Owner: daniel
--

SELECT pg_catalog.setval('public.indicator_type_sequence', 6, true);


--
-- Name: intake_result_seq; Type: SEQUENCE SET; Schema: public; Owner: daniel
--

SELECT pg_catalog.setval('public.intake_result_seq', 81, true);


--
-- Name: intake_seq; Type: SEQUENCE SET; Schema: public; Owner: daniel
--

SELECT pg_catalog.setval('public.intake_seq', 701, true);


--
-- Name: meal_food_seq; Type: SEQUENCE SET; Schema: public; Owner: daniel
--

SELECT pg_catalog.setval('public.meal_food_seq', 201, true);


--
-- Name: meal_seq; Type: SEQUENCE SET; Schema: public; Owner: daniel
--

SELECT pg_catalog.setval('public.meal_seq', 91, true);


--
-- Name: measure_sequence; Type: SEQUENCE SET; Schema: public; Owner: daniel
--

SELECT pg_catalog.setval('public.measure_sequence', 201, true);


--
-- Name: medicine_seq; Type: SEQUENCE SET; Schema: public; Owner: daniel
--

SELECT pg_catalog.setval('public.medicine_seq', 821, true);


--
-- Name: nutrition_value_sequence; Type: SEQUENCE SET; Schema: public; Owner: daniel
--

SELECT pg_catalog.setval('public.nutrition_value_sequence', 21, true);


--
-- Name: plan_seq; Type: SEQUENCE SET; Schema: public; Owner: daniel
--

SELECT pg_catalog.setval('public.plan_seq', 681, true);


--
-- Name: set_result_seq; Type: SEQUENCE SET; Schema: public; Owner: daniel
--

SELECT pg_catalog.setval('public.set_result_seq', 741, true);


--
-- Name: set_seq; Type: SEQUENCE SET; Schema: public; Owner: daniel
--

SELECT pg_catalog.setval('public.set_seq', 701, true);


--
-- Name: sport_seq; Type: SEQUENCE SET; Schema: public; Owner: daniel
--

SELECT pg_catalog.setval('public.sport_seq', 701, true);


--
-- Name: step_seq; Type: SEQUENCE SET; Schema: public; Owner: daniel
--

SELECT pg_catalog.setval('public.step_seq', 101, true);


--
-- Name: train_seq; Type: SEQUENCE SET; Schema: public; Owner: daniel
--

SELECT pg_catalog.setval('public.train_seq', 681, true);


--
-- Name: treatment_seq; Type: SEQUENCE SET; Schema: public; Owner: daniel
--

SELECT pg_catalog.setval('public.treatment_seq', 721, true);


--
-- Name: convert_type convert_type_pkey; Type: CONSTRAINT; Schema: public; Owner: daniel
--

ALTER TABLE ONLY public.convert_type
    ADD CONSTRAINT convert_type_pkey PRIMARY KEY (id);


--
-- Name: diet diet_pkey; Type: CONSTRAINT; Schema: public; Owner: daniel
--

ALTER TABLE ONLY public.diet
    ADD CONSTRAINT diet_pkey PRIMARY KEY (id);


--
-- Name: diet diet_title_idx; Type: CONSTRAINT; Schema: public; Owner: daniel
--

ALTER TABLE ONLY public.diet
    ADD CONSTRAINT diet_title_idx UNIQUE (title);


--
-- Name: exercise exercise_pkey; Type: CONSTRAINT; Schema: public; Owner: daniel
--

ALTER TABLE ONLY public.exercise
    ADD CONSTRAINT exercise_pkey PRIMARY KEY (id);


--
-- Name: float_number float_number_pkey; Type: CONSTRAINT; Schema: public; Owner: daniel
--

ALTER TABLE ONLY public.float_number
    ADD CONSTRAINT float_number_pkey PRIMARY KEY (id);


--
-- Name: food food_pkey; Type: CONSTRAINT; Schema: public; Owner: daniel
--

ALTER TABLE ONLY public.food
    ADD CONSTRAINT food_pkey PRIMARY KEY (id);


--
-- Name: food_set food_set_pkey; Type: CONSTRAINT; Schema: public; Owner: daniel
--

ALTER TABLE ONLY public.food_set
    ADD CONSTRAINT food_set_pkey PRIMARY KEY (id);


--
-- Name: food_set food_set_title_idx; Type: CONSTRAINT; Schema: public; Owner: daniel
--

ALTER TABLE ONLY public.food_set
    ADD CONSTRAINT food_set_title_idx UNIQUE (title);


--
-- Name: food_value food_value_pkey; Type: CONSTRAINT; Schema: public; Owner: daniel
--

ALTER TABLE ONLY public.food_value
    ADD CONSTRAINT food_value_pkey PRIMARY KEY (id);


--
-- Name: health health_pkey; Type: CONSTRAINT; Schema: public; Owner: daniel
--

ALTER TABLE ONLY public.health
    ADD CONSTRAINT health_pkey PRIMARY KEY (id);


--
-- Name: health health_user_id_idx; Type: CONSTRAINT; Schema: public; Owner: daniel
--

ALTER TABLE ONLY public.health
    ADD CONSTRAINT health_user_id_idx UNIQUE (user_id);


--
-- Name: indicator indicator_pkey; Type: CONSTRAINT; Schema: public; Owner: daniel
--

ALTER TABLE ONLY public.indicator
    ADD CONSTRAINT indicator_pkey PRIMARY KEY (id);


--
-- Name: indicator_type indicator_type_name_idx; Type: CONSTRAINT; Schema: public; Owner: daniel
--

ALTER TABLE ONLY public.indicator_type
    ADD CONSTRAINT indicator_type_name_idx UNIQUE (name);


--
-- Name: indicator_type indicator_type_pkey; Type: CONSTRAINT; Schema: public; Owner: daniel
--

ALTER TABLE ONLY public.indicator_type
    ADD CONSTRAINT indicator_type_pkey PRIMARY KEY (id);


--
-- Name: intake intake_pkey; Type: CONSTRAINT; Schema: public; Owner: daniel
--

ALTER TABLE ONLY public.intake
    ADD CONSTRAINT intake_pkey PRIMARY KEY (id);


--
-- Name: intake_result intake_result_pkey; Type: CONSTRAINT; Schema: public; Owner: daniel
--

ALTER TABLE ONLY public.intake_result
    ADD CONSTRAINT intake_result_pkey PRIMARY KEY (id);


--
-- Name: integer_number integer_number_pkey; Type: CONSTRAINT; Schema: public; Owner: daniel
--

ALTER TABLE ONLY public.integer_number
    ADD CONSTRAINT integer_number_pkey PRIMARY KEY (id);


--
-- Name: meal_food meal_food_pkey; Type: CONSTRAINT; Schema: public; Owner: daniel
--

ALTER TABLE ONLY public.meal_food
    ADD CONSTRAINT meal_food_pkey PRIMARY KEY (id);


--
-- Name: meal meal_pkey; Type: CONSTRAINT; Schema: public; Owner: daniel
--

ALTER TABLE ONLY public.meal
    ADD CONSTRAINT meal_pkey PRIMARY KEY (id);


--
-- Name: measure measure_pkey; Type: CONSTRAINT; Schema: public; Owner: daniel
--

ALTER TABLE ONLY public.measure
    ADD CONSTRAINT measure_pkey PRIMARY KEY (id);


--
-- Name: medicine medicine_name_health_id_idx; Type: CONSTRAINT; Schema: public; Owner: daniel
--

ALTER TABLE ONLY public.medicine
    ADD CONSTRAINT medicine_name_health_id_idx UNIQUE (name, health_id);


--
-- Name: medicine medicine_pkey; Type: CONSTRAINT; Schema: public; Owner: daniel
--

ALTER TABLE ONLY public.medicine
    ADD CONSTRAINT medicine_pkey PRIMARY KEY (id);


--
-- Name: nutrition_value nutrition_value_pkey; Type: CONSTRAINT; Schema: public; Owner: daniel
--

ALTER TABLE ONLY public.nutrition_value
    ADD CONSTRAINT nutrition_value_pkey PRIMARY KEY (id);


--
-- Name: nutrition_value nutrition_value_value_idx; Type: CONSTRAINT; Schema: public; Owner: daniel
--

ALTER TABLE ONLY public.nutrition_value
    ADD CONSTRAINT nutrition_value_value_idx UNIQUE (value);


--
-- Name: plan plan_pkey; Type: CONSTRAINT; Schema: public; Owner: daniel
--

ALTER TABLE ONLY public.plan
    ADD CONSTRAINT plan_pkey PRIMARY KEY (id);


--
-- Name: set set_pkey; Type: CONSTRAINT; Schema: public; Owner: daniel
--

ALTER TABLE ONLY public.set
    ADD CONSTRAINT set_pkey PRIMARY KEY (id);


--
-- Name: set_result set_result_pkey; Type: CONSTRAINT; Schema: public; Owner: daniel
--

ALTER TABLE ONLY public.set_result
    ADD CONSTRAINT set_result_pkey PRIMARY KEY (id);


--
-- Name: sport sport_pkey; Type: CONSTRAINT; Schema: public; Owner: daniel
--

ALTER TABLE ONLY public.sport
    ADD CONSTRAINT sport_pkey PRIMARY KEY (id);


--
-- Name: step step_pkey; Type: CONSTRAINT; Schema: public; Owner: daniel
--

ALTER TABLE ONLY public.step
    ADD CONSTRAINT step_pkey PRIMARY KEY (id);


--
-- Name: train train_pkey; Type: CONSTRAINT; Schema: public; Owner: daniel
--

ALTER TABLE ONLY public.train
    ADD CONSTRAINT train_pkey PRIMARY KEY (id);


--
-- Name: treatment treatment_pkey; Type: CONSTRAINT; Schema: public; Owner: daniel
--

ALTER TABLE ONLY public.treatment
    ADD CONSTRAINT treatment_pkey PRIMARY KEY (id);


--
-- Name: food uk1rl3yd4208yvb6vg83k3wtsc; Type: CONSTRAINT; Schema: public; Owner: daniel
--

ALTER TABLE ONLY public.food
    ADD CONSTRAINT uk1rl3yd4208yvb6vg83k3wtsc UNIQUE (title);


--
-- Name: measure ukliofjuow1jktxrbpnpnfkp9fv; Type: CONSTRAINT; Schema: public; Owner: daniel
--

ALTER TABLE ONLY public.measure
    ADD CONSTRAINT ukliofjuow1jktxrbpnpnfkp9fv UNIQUE (type);


--
-- Name: diet_start_finish_idx; Type: INDEX; Schema: public; Owner: daniel
--

CREATE INDEX diet_start_finish_idx ON public.diet USING btree (start, finish);


--
-- Name: exercise_title_idx; Type: INDEX; Schema: public; Owner: daniel
--

CREATE INDEX exercise_title_idx ON public.exercise USING btree (title);


--
-- Name: food_title_idx; Type: INDEX; Schema: public; Owner: daniel
--

CREATE INDEX food_title_idx ON public.food USING btree (title);


--
-- Name: food_value_food_id_idx; Type: INDEX; Schema: public; Owner: daniel
--

CREATE INDEX food_value_food_id_idx ON public.food_value USING btree (food_id);


--
-- Name: indicator_created_on_idx; Type: INDEX; Schema: public; Owner: daniel
--

CREATE INDEX indicator_created_on_idx ON public.indicator USING btree (created_on);


--
-- Name: indicator_indicator_type_id_idx; Type: INDEX; Schema: public; Owner: daniel
--

CREATE INDEX indicator_indicator_type_id_idx ON public.indicator USING btree (indicator_type_id);


--
-- Name: intake_day_idx; Type: INDEX; Schema: public; Owner: daniel
--

CREATE INDEX intake_day_idx ON public.intake USING btree (day);


--
-- Name: intake_plan_id_idx; Type: INDEX; Schema: public; Owner: daniel
--

CREATE INDEX intake_plan_id_idx ON public.intake USING btree (plan_id);


--
-- Name: intake_result_intake_id_idx; Type: INDEX; Schema: public; Owner: daniel
--

CREATE INDEX intake_result_intake_id_idx ON public.intake_result USING btree (intake_id);


--
-- Name: meal_time_day_idx; Type: INDEX; Schema: public; Owner: daniel
--

CREATE INDEX meal_time_day_idx ON public.meal USING btree ("time", day);


--
-- Name: plan_start_idx; Type: INDEX; Schema: public; Owner: daniel
--

CREATE INDEX plan_start_idx ON public.plan USING btree (start);


--
-- Name: set_result_set_id_idx; Type: INDEX; Schema: public; Owner: daniel
--

CREATE INDEX set_result_set_id_idx ON public.set_result USING btree (set_id);


--
-- Name: set_train_id_idx; Type: INDEX; Schema: public; Owner: daniel
--

CREATE INDEX set_train_id_idx ON public.set USING btree (train_id);


--
-- Name: sport_health_id_idx; Type: INDEX; Schema: public; Owner: daniel
--

CREATE INDEX sport_health_id_idx ON public.sport USING btree (health_id);


--
-- Name: sport_start_idx; Type: INDEX; Schema: public; Owner: daniel
--

CREATE INDEX sport_start_idx ON public.sport USING btree (start);


--
-- Name: step_exercise_id_idx; Type: INDEX; Schema: public; Owner: daniel
--

CREATE INDEX step_exercise_id_idx ON public.step USING btree (exercise_id);


--
-- Name: train_day_idx; Type: INDEX; Schema: public; Owner: daniel
--

CREATE INDEX train_day_idx ON public.train USING btree (day);


--
-- Name: treatment_description_idx; Type: INDEX; Schema: public; Owner: daniel
--

CREATE INDEX treatment_description_idx ON public.treatment USING btree (description);


--
-- Name: treatment_health_id_idx; Type: INDEX; Schema: public; Owner: daniel
--

CREATE INDEX treatment_health_id_idx ON public.treatment USING btree (health_id);


--
-- Name: meal_food fk19k3ufl4ns0xhrcdgtlsv9sl6; Type: FK CONSTRAINT; Schema: public; Owner: daniel
--

ALTER TABLE ONLY public.meal_food
    ADD CONSTRAINT fk19k3ufl4ns0xhrcdgtlsv9sl6 FOREIGN KEY (measure_id) REFERENCES public.measure(id);


--
-- Name: intake_result fk1x9rt7eavvgfqqmleu9m00h2m; Type: FK CONSTRAINT; Schema: public; Owner: daniel
--

ALTER TABLE ONLY public.intake_result
    ADD CONSTRAINT fk1x9rt7eavvgfqqmleu9m00h2m FOREIGN KEY (intake_id) REFERENCES public.intake(id);


--
-- Name: set fk2kdti8su7e7ln3g2y532vprce; Type: FK CONSTRAINT; Schema: public; Owner: daniel
--

ALTER TABLE ONLY public.set
    ADD CONSTRAINT fk2kdti8su7e7ln3g2y532vprce FOREIGN KEY (train_id) REFERENCES public.train(id);


--
-- Name: intake fk2t5pmai9l89dk1o6sotnvmk0r; Type: FK CONSTRAINT; Schema: public; Owner: daniel
--

ALTER TABLE ONLY public.intake
    ADD CONSTRAINT fk2t5pmai9l89dk1o6sotnvmk0r FOREIGN KEY (convert_type_id) REFERENCES public.convert_type(id);


--
-- Name: set fk2te7822b4avf2plco3kw8m6ep; Type: FK CONSTRAINT; Schema: public; Owner: daniel
--

ALTER TABLE ONLY public.set
    ADD CONSTRAINT fk2te7822b4avf2plco3kw8m6ep FOREIGN KEY (exercise_id) REFERENCES public.exercise(id);


--
-- Name: indicator fk39pswy1ke22cxr7i2rd0d0gei; Type: FK CONSTRAINT; Schema: public; Owner: daniel
--

ALTER TABLE ONLY public.indicator
    ADD CONSTRAINT fk39pswy1ke22cxr7i2rd0d0gei FOREIGN KEY (health_id) REFERENCES public.health(id);


--
-- Name: diet fk5nceubi1qae1qbom7mt1deml7; Type: FK CONSTRAINT; Schema: public; Owner: daniel
--

ALTER TABLE ONLY public.diet
    ADD CONSTRAINT fk5nceubi1qae1qbom7mt1deml7 FOREIGN KEY (health_id) REFERENCES public.health(id);


--
-- Name: step fk62fybfycel7ejpk4ay017vihd; Type: FK CONSTRAINT; Schema: public; Owner: daniel
--

ALTER TABLE ONLY public.step
    ADD CONSTRAINT fk62fybfycel7ejpk4ay017vihd FOREIGN KEY (exercise_id) REFERENCES public.exercise(id);


--
-- Name: train fk7pfgud9969yv647lxkvpn6yj6; Type: FK CONSTRAINT; Schema: public; Owner: daniel
--

ALTER TABLE ONLY public.train
    ADD CONSTRAINT fk7pfgud9969yv647lxkvpn6yj6 FOREIGN KEY (sport_id) REFERENCES public.sport(id);


--
-- Name: medicine fk86gi4a08scpvr02y1f06eohtb; Type: FK CONSTRAINT; Schema: public; Owner: daniel
--

ALTER TABLE ONLY public.medicine
    ADD CONSTRAINT fk86gi4a08scpvr02y1f06eohtb FOREIGN KEY (health_id) REFERENCES public.health(id);


--
-- Name: nutrition_value fk8c7b04kk2vs1pvb3ndye9x2b6; Type: FK CONSTRAINT; Schema: public; Owner: daniel
--

ALTER TABLE ONLY public.nutrition_value
    ADD CONSTRAINT fk8c7b04kk2vs1pvb3ndye9x2b6 FOREIGN KEY (convert_type_id) REFERENCES public.convert_type(id);


--
-- Name: set_result fk8ir4wqo276ynmffj57uo3w74v; Type: FK CONSTRAINT; Schema: public; Owner: daniel
--

ALTER TABLE ONLY public.set_result
    ADD CONSTRAINT fk8ir4wqo276ynmffj57uo3w74v FOREIGN KEY (set_id) REFERENCES public.set(id);


--
-- Name: meal_food fk9eo5x0xh1wkof5eybyeo8h16i; Type: FK CONSTRAINT; Schema: public; Owner: daniel
--

ALTER TABLE ONLY public.meal_food
    ADD CONSTRAINT fk9eo5x0xh1wkof5eybyeo8h16i FOREIGN KEY (meal_id) REFERENCES public.meal(id);


--
-- Name: plan fk9iade4052vb9heic8v190dfj0; Type: FK CONSTRAINT; Schema: public; Owner: daniel
--

ALTER TABLE ONLY public.plan
    ADD CONSTRAINT fk9iade4052vb9heic8v190dfj0 FOREIGN KEY (medicine_id) REFERENCES public.medicine(id);


--
-- Name: indicator_type fk9ysl4qm4n9j0khkvbrpw4p96j; Type: FK CONSTRAINT; Schema: public; Owner: daniel
--

ALTER TABLE ONLY public.indicator_type
    ADD CONSTRAINT fk9ysl4qm4n9j0khkvbrpw4p96j FOREIGN KEY (convert_type_id) REFERENCES public.convert_type(id);


--
-- Name: plan fkb5s820na80vhkq3ufq0m0tbrt; Type: FK CONSTRAINT; Schema: public; Owner: daniel
--

ALTER TABLE ONLY public.plan
    ADD CONSTRAINT fkb5s820na80vhkq3ufq0m0tbrt FOREIGN KEY (health_id) REFERENCES public.health(id);


--
-- Name: food fkbitapin3hwdwscfa57njl99p6; Type: FK CONSTRAINT; Schema: public; Owner: daniel
--

ALTER TABLE ONLY public.food
    ADD CONSTRAINT fkbitapin3hwdwscfa57njl99p6 FOREIGN KEY (measure_id) REFERENCES public.measure(id);


--
-- Name: food_set_food fkd8rp26m0u2ywrfns3j31o46jj; Type: FK CONSTRAINT; Schema: public; Owner: daniel
--

ALTER TABLE ONLY public.food_set_food
    ADD CONSTRAINT fkd8rp26m0u2ywrfns3j31o46jj FOREIGN KEY (food_set_id) REFERENCES public.food_set(id);


--
-- Name: medicine fkdlnof5g3xrhfhq1p33qlok9h0; Type: FK CONSTRAINT; Schema: public; Owner: daniel
--

ALTER TABLE ONLY public.medicine
    ADD CONSTRAINT fkdlnof5g3xrhfhq1p33qlok9h0 FOREIGN KEY (measure_id) REFERENCES public.measure(id);


--
-- Name: food_set fkdy700oesiek3svhu8172mops1; Type: FK CONSTRAINT; Schema: public; Owner: daniel
--

ALTER TABLE ONLY public.food_set
    ADD CONSTRAINT fkdy700oesiek3svhu8172mops1 FOREIGN KEY (health_id) REFERENCES public.health(id);


--
-- Name: indicator fkgamkx0cl8dtukro13huwd65yp; Type: FK CONSTRAINT; Schema: public; Owner: daniel
--

ALTER TABLE ONLY public.indicator
    ADD CONSTRAINT fkgamkx0cl8dtukro13huwd65yp FOREIGN KEY (indicator_type_id) REFERENCES public.indicator_type(id);


--
-- Name: sport fkgr64r8441em5cj9ftqdf6ago4; Type: FK CONSTRAINT; Schema: public; Owner: daniel
--

ALTER TABLE ONLY public.sport
    ADD CONSTRAINT fkgr64r8441em5cj9ftqdf6ago4 FOREIGN KEY (health_id) REFERENCES public.health(id);


--
-- Name: meal_food fkgra778wnc9jcsrtyu2ywbny38; Type: FK CONSTRAINT; Schema: public; Owner: daniel
--

ALTER TABLE ONLY public.meal_food
    ADD CONSTRAINT fkgra778wnc9jcsrtyu2ywbny38 FOREIGN KEY (food_id) REFERENCES public.food(id);


--
-- Name: meal fki0ejby99ebiarky9ri59h10lc; Type: FK CONSTRAINT; Schema: public; Owner: daniel
--

ALTER TABLE ONLY public.meal
    ADD CONSTRAINT fki0ejby99ebiarky9ri59h10lc FOREIGN KEY (diet_id) REFERENCES public.diet(id);


--
-- Name: food_value fkjxc695m0a6ry75u2tj224vgm1; Type: FK CONSTRAINT; Schema: public; Owner: daniel
--

ALTER TABLE ONLY public.food_value
    ADD CONSTRAINT fkjxc695m0a6ry75u2tj224vgm1 FOREIGN KEY (nutrition_value_id) REFERENCES public.nutrition_value(id);


--
-- Name: integer_number fkk5rm0mloda47lwtgr8xq2bqh; Type: FK CONSTRAINT; Schema: public; Owner: daniel
--

ALTER TABLE ONLY public.integer_number
    ADD CONSTRAINT fkk5rm0mloda47lwtgr8xq2bqh FOREIGN KEY (id) REFERENCES public.convert_type(id);


--
-- Name: float_number fkl59xh3r8pu3lrbt0kweysdaq0; Type: FK CONSTRAINT; Schema: public; Owner: daniel
--

ALTER TABLE ONLY public.float_number
    ADD CONSTRAINT fkl59xh3r8pu3lrbt0kweysdaq0 FOREIGN KEY (id) REFERENCES public.convert_type(id);


--
-- Name: medicine fkmi5gc7t7ij15ju4ui113t2o42; Type: FK CONSTRAINT; Schema: public; Owner: daniel
--

ALTER TABLE ONLY public.medicine
    ADD CONSTRAINT fkmi5gc7t7ij15ju4ui113t2o42 FOREIGN KEY (convert_type_id) REFERENCES public.convert_type(id);


--
-- Name: food_value fkov8f5r99n49c9787ndo2wi246; Type: FK CONSTRAINT; Schema: public; Owner: daniel
--

ALTER TABLE ONLY public.food_value
    ADD CONSTRAINT fkov8f5r99n49c9787ndo2wi246 FOREIGN KEY (food_id) REFERENCES public.food(id);


--
-- Name: indicator_type fkp100o8ag812wxigoshg8ypmqm; Type: FK CONSTRAINT; Schema: public; Owner: daniel
--

ALTER TABLE ONLY public.indicator_type
    ADD CONSTRAINT fkp100o8ag812wxigoshg8ypmqm FOREIGN KEY (measure_id) REFERENCES public.measure(id);


--
-- Name: nutrition_value fkpdq4jng7o79mxpw3j9ljvhxd3; Type: FK CONSTRAINT; Schema: public; Owner: daniel
--

ALTER TABLE ONLY public.nutrition_value
    ADD CONSTRAINT fkpdq4jng7o79mxpw3j9ljvhxd3 FOREIGN KEY (measure_id) REFERENCES public.measure(id);


--
-- Name: intake fkq6k5p4wc7nlhlwsknyigh04xl; Type: FK CONSTRAINT; Schema: public; Owner: daniel
--

ALTER TABLE ONLY public.intake
    ADD CONSTRAINT fkq6k5p4wc7nlhlwsknyigh04xl FOREIGN KEY (measure_id) REFERENCES public.measure(id);


--
-- Name: food_set_food fkqa4ttgrho2wjleauk79pge8tu; Type: FK CONSTRAINT; Schema: public; Owner: daniel
--

ALTER TABLE ONLY public.food_set_food
    ADD CONSTRAINT fkqa4ttgrho2wjleauk79pge8tu FOREIGN KEY (food_id) REFERENCES public.food(id);


--
-- Name: plan fkqwq6nx8k43bo7cx7e3qni2h14; Type: FK CONSTRAINT; Schema: public; Owner: daniel
--

ALTER TABLE ONLY public.plan
    ADD CONSTRAINT fkqwq6nx8k43bo7cx7e3qni2h14 FOREIGN KEY (treatment_id) REFERENCES public.treatment(id);


--
-- Name: food fksewnx680760rq1ewu2kylno52; Type: FK CONSTRAINT; Schema: public; Owner: daniel
--

ALTER TABLE ONLY public.food
    ADD CONSTRAINT fksewnx680760rq1ewu2kylno52 FOREIGN KEY (convert_type_id) REFERENCES public.convert_type(id);


--
-- Name: treatment fksrrn1ae0whrl70sv2fly1kt0y; Type: FK CONSTRAINT; Schema: public; Owner: daniel
--

ALTER TABLE ONLY public.treatment
    ADD CONSTRAINT fksrrn1ae0whrl70sv2fly1kt0y FOREIGN KEY (health_id) REFERENCES public.health(id);


--
-- Name: intake fksxe4fm40jl8u1arruocr0xogh; Type: FK CONSTRAINT; Schema: public; Owner: daniel
--

ALTER TABLE ONLY public.intake
    ADD CONSTRAINT fksxe4fm40jl8u1arruocr0xogh FOREIGN KEY (plan_id) REFERENCES public.plan(id);


--
-- Name: food fktjdxinffengoxjd4vpm221emb; Type: FK CONSTRAINT; Schema: public; Owner: daniel
--

ALTER TABLE ONLY public.food
    ADD CONSTRAINT fktjdxinffengoxjd4vpm221emb FOREIGN KEY (health_id) REFERENCES public.health(id);


--
-- Name: exercise fkwvgrspbmuxcsybyp00x03lnx; Type: FK CONSTRAINT; Schema: public; Owner: daniel
--

ALTER TABLE ONLY public.exercise
    ADD CONSTRAINT fkwvgrspbmuxcsybyp00x03lnx FOREIGN KEY (health_id) REFERENCES public.health(id);


--
-- PostgreSQL database dump complete
--

