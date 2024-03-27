package com.github.vjuranek.camel;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.apache.camel.Converter;
import org.apache.kafka.connect.data.Struct;

@Converter
public class Converters {

    @Converter
    public static InputStream structToFile(Struct struct) {
        String msg = struct.getInt32("id") + " " + struct.getString("first_name");
        return new ByteArrayInputStream(msg.getBytes(StandardCharsets.UTF_8));
    }
}

