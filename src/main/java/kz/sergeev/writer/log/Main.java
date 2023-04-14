package kz.sergeev.writer.log;

import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        if (args.length < 1) {
            System.err.println("Usage: java main.java.kz.sergeev.writer.log.LogFileWriter <log-file-name>");
            System.exit(1);
        }

        String logFileName = args[0];

        try {

            // Override properties with command line arguments
            if (args.length > 1) {
                for (int i = 1; i < args.length; i += 2) {
                    PropertyUtil.set(args[i], args[i + 1]);
                }
            }

            LogFileWriter logFileWriter = new LogFileWriter(logFileName);
            logFileWriter.writeFromStandardInput();
            logFileWriter.close();
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
            System.exit(1);
        }
    }
}