package com.github.vjuranek.camel;

import java.io.InputStream;

import org.apache.camel.builder.RouteBuilder;

public class PostgresToFileRoute extends RouteBuilder {

    private final String postgres;
    private final String file;

    public PostgresToFileRoute(String destination) {
        this.postgres = String.format("debezium-postgres:test?"
                + "databaseHostname=localhost"
                + "&databasePort=5432"
                + "&databaseUser=postgres"
                + "&databasePassword=postgres"
                + "&databaseDbname=postgres"
                + "&topicPrefix=test"
                + "&tableIncludeList=inventory.customers"
                + "&offsetStorageFileName=/tmp/dbz/offset.dat");
        this.file = String.format("file:%s", destination);
    }

    public void configure() {
        getContext().getTypeConverterRegistry().addTypeConverters(new Converters());

        from(postgres)
                .log(String.format("Got update ${body}"))
                .convertBodyTo(InputStream.class)
                .to(file)
                .log("Delivered");
    }
}
