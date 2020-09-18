package ch.math.spatial;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import picocli.CommandLine;
import java.io.*;

@SpringBootApplication
public class IntersectionApplication implements CommandLineRunner {

    private final IntersectionCommand command;

    public static void main(String[] args) {
        SpringApplication.run(IntersectionApplication.class, args);
    }

    public IntersectionApplication(IntersectionCommand command) {
        this.command = command;
    }

    @Override
    public void run(String[] args){
        System.exit(new CommandLine(command).execute(args));
    }
}
