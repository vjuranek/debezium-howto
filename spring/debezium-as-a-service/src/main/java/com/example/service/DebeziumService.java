package com.example.service;

import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import io.debezium.engine.ChangeEvent;
import io.debezium.engine.DebeziumEngine;
import io.debezium.engine.format.Json;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

@Service
public class DebeziumService {

    @PostConstruct()
    public void init() throws IOException, InterruptedException  {
        final Properties props = new Properties();
        props.setProperty("name", "engine");
        props.setProperty("connector.class", "io.debezium.connector.postgresql.PostgresConnector");
        props.setProperty("offset.storage", "org.apache.kafka.connect.storage.FileOffsetBackingStore");
        props.setProperty("offset.storage.file.filename", "offsets.dat");
        props.setProperty("offset.flush.interval.ms", "60000");
        props.setProperty("schema.history.internal", "io.debezium.storage.file.history.FileSchemaHistory");
        props.setProperty("schema.history.internal.file.filename", "schemahistory.dat");

        props.setProperty("database.hostname", "localhost");
        props.setProperty("database.port", "5432");
        props.setProperty("database.user", "postgres");
        props.setProperty("database.password", "postgres");
        props.setProperty("database.dbname", "postgres");
        props.setProperty("topic.prefix", "test");


        try (DebeziumEngine<ChangeEvent<String, String>> engine = DebeziumEngine.create(Json.class)
                .using(props)
                .using(Thread.currentThread().getContextClassLoader())
                .notifying(record -> {
                    System.out.println(record);
                }).build()
        ) {
            ExecutorService executor = Executors.newWorkStealingPool(1);
            executor.execute(engine);
            Thread.sleep(10000);
        }
    }
}
