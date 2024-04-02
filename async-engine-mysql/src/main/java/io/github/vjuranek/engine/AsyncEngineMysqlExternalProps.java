package io.github.vjuranek.engine;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import io.debezium.engine.ChangeEvent;
import io.debezium.engine.DebeziumEngine;
import io.debezium.engine.format.Json;
import io.debezium.engine.format.KeyValueHeaderChangeEventFormat;

/**
 * Debezium embedded engine example with loading external properties.
 */
public class AsyncEngineMysqlExternalProps {
    public static void main( String[] args ) {
        InputStream propsInputStream = AsyncEngineMysqlExternalProps.class.getClassLoader().getResourceAsStream("config.properties");
        Properties props = new Properties();
        try {
            props.load(propsInputStream);
        }
        catch (IOException e) {
            System.err.println("Couldn't load connector properties.");
        }

        DebeziumEngine<ChangeEvent<String, String>> engine = DebeziumEngine
                .create(KeyValueHeaderChangeEventFormat.of(Json.class, Json.class, Json.class),
                        "io.debezium.embedded.async.ConvertingAsyncEngineBuilderFactory")
                .using(props)
                .notifying(record -> {
                    System.out.println("Key = '" + record.key() + "' value = '" + record.value() + "'");
                }).build();

        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(engine);
        executor.shutdown();
        try {
            TimeUnit.SECONDS.sleep(10);
            engine.close();
            executor.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
        finally {
            executor.shutdownNow();
        }
    }
}
