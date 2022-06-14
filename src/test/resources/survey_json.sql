create table if not exists SURVEY_JSON
(
    id        INT  NULL,
    json      text NULL,
    survey_id INT  NOT NULL
);