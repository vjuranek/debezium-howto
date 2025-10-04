create table test_table (
    id bigint not null,
    pollOption varchar(255),
    primary key (id)
);

CREATE SUBSCRIPTION poll_sub CONNECTION 'dbname=postgres host=postgres_primary user=replicator password=replicator' PUBLICATION poll_pub;
