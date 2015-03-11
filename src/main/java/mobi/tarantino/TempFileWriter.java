package mobi.tarantino;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by kolipass on 28.12.14.
 */
public class TempFileWriter<N> {
    private String aFileName;
    private N points;

    public TempFileWriter(String aFileName, N points) {
        this.aFileName = aFileName;
        this.points = points;
    }

    void write() throws IOException {
        Path path = Paths.get(aFileName);
        try (BufferedWriter writer = Files.newBufferedWriter(path)) {
            writer.write(points.toString());
        }
    }
}
