package mobi.tarantino;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * Created by kolipass on 28.12.14.
 */
public class TempFileWriter<N extends AbstractModel> {
    private String aFileName;
    private List<N> points;

    public TempFileWriter(String aFileName, List<N> points) {
        this.aFileName = aFileName;
        this.points = points;
    }

    void write() throws IOException {
        Path path = Paths.get(aFileName);
        try (BufferedWriter writer = Files.newBufferedWriter(path)) {
            for (N line : points) {
                writer.write(line.toString());
                writer.newLine();
            }
        }
    }
}
