package mobi.tarantino;

import mobi.tarantino.collection.ObjFigure;
import mobi.tarantino.collection.OptimizedObjFigure;

import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        if (args.length != 0) {
            for (String filepath : args) {
                File source = new File(filepath);
                if (source.exists()) {

                    Config config = Config.getInstance();

                    ObjFigure pointList = PlateUtils.cylindrate(new TempFileReader(source).read(), config);

                    pointList.addComment(config.toObjComment());

                    new TempFileWriter<>(filepath + ".obj", pointList).write();
                    new TempFileWriter<>(filepath + "_optimized" + ".obj", new OptimizedObjFigure(pointList)).write();
                } else System.out.println("file not found");
            }
        }
        System.out.println("finish");
    }
}
