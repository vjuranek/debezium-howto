package io.github.vjuranek.engine;

import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import io.debezium.engine.ChangeEvent;
import io.debezium.engine.DebeziumEngine;
import io.debezium.engine.format.Json;

/**
 * Debezium embedded engine example from the documentation using MySQL connector.
 */
public class AsyncEngineMysql {
    public static void main( String[] args ) {
        final Properties props = new Properties();
        props.setProperty("name", "mysql-engine");
        props.setProperty("connector.class", "io.debezium.connector.mysql.MySqlConnector");
        props.setProperty("offset.storage", "org.apache.kafka.connect.storage.FileOffsetBackingStore");
        props.setProperty("offset.storage.file.filename", "/tmp/offsets.dat");
        props.setProperty("offset.flush.interval.ms", "5000");
        // connector properties
        props.setProperty("database.hostname", "localhost");
        props.setProperty("database.port", "3306");
        props.setProperty("database.user", "debezium");
        props.setProperty("database.password", "dbz");
        props.setProperty("database.server.id", "12345");
        props.setProperty("topic.prefix", "test-topic");
        props.setProperty("schema.history.internal", "io.debezium.storage.file.history.FileSchemaHistory");
        props.setProperty("schema.history.internal.file.filename", "/tmp/schema_history.dat");

        try (DebeziumEngine<ChangeEvent<String, String>> engine = DebeziumEngine
                .create(Json.class, Json.class, Json.class, "io.debezium.embedded.async.ConvertingAsyncEngineBuilderFactory")
                .using(props)
                .notifying(record -> {
                    System.out.println(record);
                }).build()
        ) {
            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.execute(engine);
            executor.shutdown();
            try {
                executor.awaitTermination(1, TimeUnit.HOURS);
            }
            catch (InterruptedException e) {
                System.out.println("Interrupted, shitting down now.");
                executor.shutdownNow();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
