package org.example.rtda;

import org.example.classFile.ClassFile;
import org.example.classLoader.ClassLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Class {

    public final int accessFlags;
    public final String name;

    public final String superClassName;
    public final ClassFile classFile;

    public final List<Method> methods;
    public final ClassFile.ConstantPool constantPool;
    public final ClassLoader classLoader;

    private Class superClass;
    public int stat = 0;

    public Class(int accessFlags, String name, ClassLoader classLoader) {
        this.accessFlags = accessFlags;
        this.name = name;
        this.superClassName = null;
        this.constantPool = null;
        this.classFile = null;
        this.methods = new ArrayList<>();
        this.classLoader = classLoader;
        this.stat = 2;
    }

    public Class(
            int accessFlags,
            String name,
            String superClassName,
            List<Method> methods,
            ClassFile.ConstantPool constantPool,
            ClassLoader classLoader,
            ClassFile classFile) {
        this.accessFlags = accessFlags;
        this.name = name;
        this.superClassName = superClassName;
        this.classFile = classFile;
        this.methods = methods;
        this.constantPool = constantPool;
        this.classLoader = classLoader;

        methods.forEach(it -> it.clazz = this);
    }

    public void setSuperClass(Class superClass) {
        this.superClass = superClass;
    }

    public Method getMainMethod() {
        for (Method method : methods) {
            if (Objects.equals("main", method.name)) {
                return method;
            }
        }
        return null;
    }
}
