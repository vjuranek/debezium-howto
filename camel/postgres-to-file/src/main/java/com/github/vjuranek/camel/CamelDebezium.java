package com.github.vjuranek.camel;

import org.apache.camel.main.Main;

public class CamelDebezium {
    public static void main(String[] args) throws Exception {
        Main main = new Main();
        main.configure().addRoutesBuilder(new PostgresToFileRoute(args[0]));
        main.run();
    }
}
