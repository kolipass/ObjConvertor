package mobi.tarantino;

import mobi.tarantino.model.Point;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * Created by kolipass on 28.12.14.
 */
public class TempFileReader {
    private File file;

    public TempFileReader(File file) {
        this.file = file;
    }

    public static void read(String path) {
        try (Scanner in = new Scanner(new File(path))) {
            while (in.hasNext()) {
                System.out.println(in.next());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public List<Point> read() throws FileNotFoundException {
        List<Point> points = new ArrayList<>();
        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                points.add(processLine(scanner.nextLine()));
            }
        }
        return points;
    }

    protected Point processLine(String aLine) {
        //use a second Scanner to parse the content of each line
        Scanner scanner = new Scanner(aLine);
        scanner.useDelimiter(Pattern.compile("\\s+"));
        int i = 0;
        Point point = new Point();
        while (scanner.hasNext()) {
            switch (i++) {
                case Point.X:
                    point.x = Float.parseFloat(scanner.next());
                    break;
                case Point.Y:
                    point.y = Float.parseFloat(scanner.next());
                    break;
                case Point.Z:
                    point.z = Float.parseFloat(scanner.next());
                    break;
            }
        }
        return point;
    }
}
