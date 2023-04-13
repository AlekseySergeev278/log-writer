import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;

public class LogFileWriter {

    private static final String MAX_SIZE_KEY = "maxFileSizeKB";
    private static final String LOG_EXTENSION_KEY = "logFileExtension";
    private static final String DATE_FORMAT_KEY = "dateFormat";
    private final int maxFileSizeKB;
    private final String logFileExtension;
    private final DateTimeFormatter dateFormat;

    private File logFile;
    private BufferedWriter writer;
    private long fileSize;

    public LogFileWriter(String logFileName) throws IOException {
        this.maxFileSizeKB = Integer.parseInt(PropertyUtil.get(MAX_SIZE_KEY));
        this.logFileExtension = PropertyUtil.get(LOG_EXTENSION_KEY);
        this.dateFormat = DateTimeFormatter.ofPattern(PropertyUtil.get(DATE_FORMAT_KEY));

        this.logFile = new File(logFileName + logFileExtension);
        this.writer = new BufferedWriter(new FileWriter(logFile, true));
        this.fileSize = logFile.length();
    }

    public void writeFromStandardInput() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String line;
        while (true) {
            line = reader.readLine();
            if (line == null) {
                break;
            }

            if (fileSize >= maxFileSizeKB * 1024) {
                rollOverLogFile();
            }

            writer.write(line);
            writer.newLine();
            writer.flush();

            fileSize = logFile.length();
        }
    }

    private void rollOverLogFile() throws IOException {
        writer.close();

        String timestamp = LocalDateTime.now().format(dateFormat);
        String rolledOverLogFileName = logFile.getName() + "-" + timestamp;

        File rolledOverLogFile = new File(logFile.getParentFile(), rolledOverLogFileName + logFileExtension);

        boolean success = logFile.renameTo(rolledOverLogFile);

        if (!success) {
            throw new IOException("Failed to rename log file: " + logFile.getName());
        }

        writer = new BufferedWriter(new FileWriter(logFile, true));
    }

    public void close() throws IOException {
        writer.close();
    }

}
