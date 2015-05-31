package mobi.tarantino;

import mobi.tarantino.model.ObjComment;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static mobi.tarantino.Config.Field.*;

public class Config implements Cloneable {

    public static final String CONFIG = "/build.properties";
    private static final String DEFAULT_EDGE_COUNT = "4";
    private static final String DEFAULT_SCALE = "3";
    private static final String DEFAULT_RADIUS = "0.1f";
    private static final String DEFAULT_PRINT_CONFIG = "false";
    private static final OptimizeType DEFAULT_OPTIMIZE = OptimizeType.DEFAULT;
    private static final PlateUtils.NODE_TYPE DEFAULT_NODE_TYPE = PlateUtils.NODE_TYPE.NONE;
    private static final String DEFAULT_POSTFIX = null;
    private static Config instance;
    Type type;
    HashMap<Field, String> fieldMap;

    protected Config() {
        fieldMap = new HashMap<>();
    }

    private static Config makeDefault() {
        Config config = new Config();
        config.type = Type.NONE;
        config.fieldMap.put(EDGE_COUNT, DEFAULT_EDGE_COUNT);
        config.fieldMap.put(RADIUS, DEFAULT_RADIUS);
        config.fieldMap.put(NODE_TYPE, String.valueOf(DEFAULT_NODE_TYPE));
        return config;
    }

    private static Config makeFromConfig() {
        Config config = makeDefault();

        try (InputStream inputStream = Config.class.getResourceAsStream(CONFIG)) {

            if (inputStream != null) {

                Properties props = new java.util.Properties();
                props.load(inputStream);

                config.type = Type.FILE;
                for (Field field : values()) {
                    config.add(field, props.getProperty(field.toString()));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return config;
    }

    public static Config getInstance() {
        return getInstance(Type.NONE);
    }

    /**
     * Todo  в будущем придумать, как оно должно быть
     *
     * @param type от круда грузить конфиг (файл, анотации. может из консоли диалогом..)
     * @return инстенс конфига
     */
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

    public Config add(Field field, String value) {
        if (value != null)
            fieldMap.put(field, value);
        return this;
    }

    public String get(Field field) {
        return fieldMap.get(field);
    }

    public int getEdgeCount() {
        return Integer.parseInt(fieldMap.containsKey(EDGE_COUNT) ? fieldMap.get(EDGE_COUNT) : DEFAULT_EDGE_COUNT);
    }

    public int getScale() {
        return Integer.parseInt(fieldMap.containsKey(SCALE) ? fieldMap.get(SCALE) : DEFAULT_SCALE);
    }

    public String getPostfix() {
        return fieldMap.containsKey(POSTFIX) ? fieldMap.get(POSTFIX) : DEFAULT_POSTFIX;
    }

    public float getRadius() {
        return Float.parseFloat(fieldMap.containsKey(RADIUS) ? fieldMap.get(RADIUS) : DEFAULT_RADIUS);
    }

    public OptimizeType getOptimizeType() {
        return fieldMap.containsKey(OPTIMIZE) ? OptimizeType.valueOf(fieldMap.get(OPTIMIZE)) : DEFAULT_OPTIMIZE;
    }

    public PlateUtils.NODE_TYPE getNodeType() {
        return fieldMap.containsKey(NODE_TYPE) ? PlateUtils.NODE_TYPE.valueOf(fieldMap.get(NODE_TYPE)) : DEFAULT_NODE_TYPE;
    }

    public ObjComment toObjComment() {
        ObjComment objComment = new ObjComment();
        for (Map.Entry<Field, String> entry : fieldMap.entrySet()) {
            objComment.add(entry.getKey() + " : " + entry.getValue());
        }
        return objComment;
    }

    public Config clone() throws CloneNotSupportedException {
        Config clone = (Config) super.clone();
        clone.fieldMap = (HashMap<Field, String>) fieldMap.clone();
        clone.type = type;
        return clone;
    }

    public enum Type {NONE, FILE, CUSTOM}

    public enum OptimizeType {NONE, DEFAULT}

    public enum Field {EDGE_COUNT, SCALE, PRINT_CONFIG, PRINT_EXTRACTS, RADIUS, NODE_TYPE, OPTIMIZE, POSTFIX}

}
