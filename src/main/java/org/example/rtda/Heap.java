package org.example.rtda;

import java.util.HashMap;
import java.util.Map;

public class Heap {
    private static final Map<String, Class> STRING_K_CLASS_MAP;
    static {
        STRING_K_CLASS_MAP = new HashMap<>();
    }

    public static void registerClass(String name, Class clazz) {
        STRING_K_CLASS_MAP.put(name, clazz);
    }

    public static Class findClass(String name) {
        return STRING_K_CLASS_MAP.get(name);
    }
}
