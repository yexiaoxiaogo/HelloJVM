package org.example.classLoader;

import org.example.classFile.ClassFile;
import org.example.classFile.ClassReader;
import org.example.rtda.Class;
import org.example.rtda.Heap;
import org.example.rtda.Method;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ClassLoader {
    private final String name;

    private final String dirPath;

    public ClassLoader(String name, String dirPath) {
        this.name = name;
        this.dirPath = dirPath;
    }

    //加载class
    public Class loadClass(String interfaceName) throws IOException {
        Class cache = Heap.findClass(interfaceName);
        if (cache != null) {
            return cache;
        }
        // load & register
        Class clazz = doLoadClass(interfaceName);
        Heap.registerClass(clazz.name, clazz);
        return clazz;
    }

    private Class doLoadClass(String name) throws IOException {
        // 此处添加 java.lang.Object.class 的空实现
        if (name.equals("java/lang/Object")) {
            return new Class(1,"java/lang/Object", null);
        }
        // 读取文件
        ClassFile classFile = ClassReader.read(dirPath + "/" + name + ".class");
        // 加载文件
        List<Method> methods = new ArrayList<>();

        for (ClassFile.MethodInfo methodInfo : classFile.methods.methodInfos) {
            methods.add(this.map(methodInfo));
        }

        int scIdx = classFile.superClass;
        String superClassName = null;
        if (scIdx != 0) {
            superClassName = getClassName(classFile.cpInfo, scIdx);
        }
        Class aClass = new Class(classFile.accessFlags, name, superClassName, methods,
                classFile.cpInfo, this, classFile);
        // superclass
        if (aClass.superClassName != null) {
            aClass.setSuperClass(this.loadClass(aClass.superClassName));
        }

        return aClass;
    }

    public Method map(ClassFile.MethodInfo cfMethodInfo) {
        ClassFile.Code code = cfMethodInfo.getCode();
        if (code == null) {
            return new Method(cfMethodInfo.accessFlags, cfMethodInfo.name, cfMethodInfo.descriptor, 0, 0,
                    null, cfMethodInfo.getLineNumber());
        }
        return new Method(cfMethodInfo.accessFlags, cfMethodInfo.name, cfMethodInfo.descriptor,
                code.maxStacks, code.maxLocals, code.getInstructions(),
                cfMethodInfo.getLineNumber());
    }


    public static String getClassName(ClassFile.ConstantPool cp, int classIndex) {
        int nameIndex = ((ClassFile.ClassCp) cp.infos[classIndex - 1]).nameIndex;
        return ((ClassFile.Utf8) cp.infos[nameIndex - 1]).getString();
    }

}
