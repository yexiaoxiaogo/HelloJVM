package org.example.rtda;

import org.example.classFile.ClassFile;

import java.util.List;
import java.util.Objects;

public class Class {

    public final int accessFlags;
    public final String name;
    public final ClassFile classFile;

    public final List<Method> methods;
    public final ClassFile.ConstantPool constantPool;

    public Class(
            int accessFlags,
            String name,
            List<Method> methods,
            ClassFile.ConstantPool constantPool,
            ClassFile classFile) {
        this.accessFlags = accessFlags;
        this.name = name;
        this.classFile = classFile;
        this.methods = methods;
        this.constantPool = constantPool;

        methods.forEach(it -> it.clazz = this);
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
