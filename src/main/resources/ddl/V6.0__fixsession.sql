-- Table: public.fixsession

DROP TABLE IF EXISTS fixsession;
DROP SEQUENCE IF EXISTS fixsession_seq;

CREATE TABLE IF NOT EXISTS fixsession
(
    sessionid character varying(100) COLLATE pg_catalog."default" NOT NULL,
    firmname character varying(100) COLLATE pg_catalog."default" NOT NULL,
    sendercompid character varying(100) COLLATE pg_catalog."default" NOT NULL,
    connectiontype character varying(100) COLLATE pg_catalog."default" NOT NULL,
    targetcompid character varying(100) COLLATE pg_catalog."default" NOT NULL,
    starttime character varying(50) COLLATE pg_catalog."default" NOT NULL,
    endtime character varying(50) COLLATE pg_catalog."default" NOT NULL,
    ipaddress character varying(50) COLLATE pg_catalog."default" NOT NULL,
    port bigint NOT NULL,
    fixuser character varying(100) COLLATE pg_catalog."default" NOT NULL,
    fixpassword character varying(100) COLLATE pg_catalog."default" NOT NULL,
    heartbeatinterval bigint NOT NULL,
    fixversion character varying(10) COLLATE pg_catalog."default" NOT NULL,
    sendersubid character varying(100) COLLATE pg_catalog."default",
    senderlocationid character varying(100) COLLATE pg_catalog."default",
    targetsubid character varying(100) COLLATE pg_catalog."default",
    targetlocationid character varying(100) COLLATE pg_catalog."default",
    applicationtype character varying(50) COLLATE pg_catalog."default" NOT NULL,
    datadictionary character varying(100) COLLATE pg_catalog."default" NOT NULL,
    counterparty_type character varying(20) COLLATE pg_catalog."default" NOT NULL,
    routingtag character varying(50) COLLATE pg_catalog."default",
    "timestamp" timestamp with time zone,
                              session_status character varying(5) COLLATE pg_catalog."default",
    usedatadictionary character varying COLLATE pg_catalog."default",
    instance character varying(200) COLLATE pg_catalog."default",
    region character varying(100) COLLATE pg_catalog."default",
    hub character varying(100) COLLATE pg_catalog."default",
    timezone character varying(10) COLLATE pg_catalog."default",
    displaystarttime character varying(50) COLLATE pg_catalog."default",
    displayendtime character varying(50) COLLATE pg_catalog."default",
    normalization boolean,
    normalization_clients character varying(500),
    last_updated timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fixsession_pkey PRIMARY KEY (sessionid, fixversion)
    )
    TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.fixsession
    OWNER to postgres;

COMMENT ON COLUMN public.fixsession.counterparty_type
    IS 'Broker or Vendor';
