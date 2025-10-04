create table test_table (
    id bigint not null,
    pollOption varchar(255),
    primary key (id)
);
alter table test_table replica identity full;

CREATE PUBLICATION poll_pub FOR ALL TABLES;
CREATE ROLE replicator WITH REPLICATION LOGIN PASSWORD 'replicator';
GRANT ALL PRIVILEGES ON DATABASE postgres TO replicator;
GRANT SELECT ON ALL TABLES IN SCHEMA public TO replicator;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT SELECT ON TABLES TO replicator;

