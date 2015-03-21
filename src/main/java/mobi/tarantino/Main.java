package mobi.tarantino;

import mobi.tarantino.collection.ObjFigure;

import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        if (args.length != 0) {
            for (String filepath : args) {
                File source = new File(filepath);
                if (source.exists()) {
                    ObjFigure pointList = PlateUtils.cylindrate(new TempFileReader(source).read(), 12, 0.1f, PlateUtils.NODE_TYPE.ISO_SPHERE);
                    new TempFileWriter<>(filepath + ".obj", pointList).write();
                } else System.out.println("file not found");
            }
        }
        System.out.println("finish");
    }
}
