package mobi.tarantino;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by kolipass on 12.04.15.
 */
public class Config {

    private static final int EDGE_COUNT = 4;
    private static final float RADIUS = 0.1f;
    private static final PlateUtils.NODE_TYPE NODE_TYPE = PlateUtils.NODE_TYPE.NONE;
    private static Config instance;
    int edgeCount;
    float radius;
    Type type;
    PlateUtils.NODE_TYPE nodeType;

    protected Config() {
    }

    private static Config makeDefault() {
        Config config = new Config();
        config.type = Type.NONE;
        config.edgeCount = EDGE_COUNT;
        config.radius = RADIUS;
        config.nodeType = NODE_TYPE;
        return config;
    }

    private static Config makeFromConfig() {
        Config config = new Config();

        try {
            InputStream inputStream = Config.class.getResourceAsStream("/build.properties");
            if (inputStream != null) {

                Properties props = new java.util.Properties();
                props.load(inputStream);

                config.type = Type.FILE;
                config.edgeCount = Integer.parseInt(props.getProperty("EDGE_COUNT", String.valueOf(EDGE_COUNT)));
                config.radius = Float.parseFloat(props.getProperty("RADIUS", String.valueOf(RADIUS)));
                config.nodeType = PlateUtils.NODE_TYPE.valueOf(props.getProperty("NODE_TYPE", PlateUtils.NODE_TYPE.NONE.name()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return config;
    }

    public static Config getInstance() {
        return getInstance(Type.NONE);
    }

    public static Config getInstance(Type type) {
        if (instance == null || instance.type != type) {
            switch (type) {
                case NONE:
                    instance = makeDefault();
                    break;
                case FILE:
                    instance = makeFromConfig();
                    break;
                case CUSTOM:
                    instance = makeDefault();
                    break;
            }
        }
        return instance;
    }

    enum Type {NONE, FILE, CUSTOM}

}
