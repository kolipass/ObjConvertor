package mobi.tarantino;

import mobi.tarantino.collection.ObjFigure;
import mobi.tarantino.collection.OptimizedObjFigure;

import java.io.File;
import java.io.IOException;

public class Main {
    private String[] args;
    private Config[] configs;

    private Main(String[] args, Config[] configs) {
        this.args = args;
        this.configs = configs;
    }

    private Main(String[] args, Config config) {
        this(args, new Config[]{config});
    }

    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            Config instance = Config.getInstance();
            instance.add(Config.Field.PRINT_CONFIG, "true");
            instance.add(Config.Field.PRINT_EXTRACTS, "true");
            instance.add(Config.Field.EDGE_COUNT, "4");
            instance.add(Config.Field.RADIUS, "0.1");

            new Main(args, new Config[]{instance}).calculateAll();
        } else {
            args = new String[]{
//                    "Lorenc.txt"
                    "rect.txt", "cube.txt"
            };
            Config config = Config.getInstance()
                    .add(Config.Field.PRINT_CONFIG, "true")
                    .add(Config.Field.PRINT_EXTRACTS, "true")
                    .add(Config.Field.EDGE_COUNT, "4")
                    .add(Config.Field.RADIUS, "0.1")
                    .add(Config.Field.OPTIMIZE, String.valueOf(Config.OptimizeType.DEFAULT))
                    .add(Config.Field.NODE_TYPE, String.valueOf(PlateUtils.NODE_TYPE.NONE))
                    .add(Config.Field.IS_CLOSING_PLATE, String.valueOf("false"));

            try {
                Config clone4 = config.clone()
                        .add(Config.Field.OPTIMIZE, String.valueOf(Config.OptimizeType.DEFAULT))
                        .add(Config.Field.SCALE, "4")
                        .add(Config.Field.POSTFIX, "_optimized");
//                Config clone3 = clone4.clone().add(Config.Field.SCALE, "2");
//                Config clone2 = clone4.clone().add(Config.Field.SCALE, "1");
                new Main(args, new Config[]{config
//                        , clone4, clone3, clone2
                }).calculateAll();
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }


        }
        System.out.println("finish");
    }

    private void calculateAll() throws IOException {
        processing(args);
    }

    private void processing(String[] args) throws IOException {
        for (String filepath : args) {
            System.out.println("\n" + filepath + "\n");
            processing(filepath);
        }
    }

    private void processing(String filepath) throws IOException {
        File source = new File(filepath);
        if (source.exists()) {
            if (source.isDirectory()) {
                for (String path : source.list()) {
                    processing(path);
                }
            } else {
                calculate(source);
            }
        } else System.out.println("file not found");
    }

    private void calculate(File source) throws IOException {
        for (Config config : configs) {

            ObjFigure pointList = PlateUtils.cylindrate(new TempFileReader(source).read(), config);

            switch (config.getOptimizeType()) {
                case DEFAULT:
                    pointList = new OptimizedObjFigure(pointList);
                    break;
                case NONE:
                    break;
            }

            System.out.println(pointList.getComments());
            System.out.println("lenght " + pointList.toString().length());
            new TempFileWriter<>(source.getAbsolutePath() + config.getPostfix() + ".obj", pointList).write();
        }
    }
}
