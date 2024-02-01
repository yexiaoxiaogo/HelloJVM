package org.example.classLoader;

import org.example.App;
import org.example.classFile.ClassFile;
import org.example.classFile.ClassReader;
import org.example.rtda.Class;
import org.example.rtda.Method;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ClassLoader {

    //加载class
    public static Class loadClass(App.Args cmd) throws IOException {
        // 读取文件
        ClassFile classFile = ClassReader.read(cmd.classPath + "/" + cmd.className + ".class");
        // 加载文件
        List<Method> methods = new ArrayList<>();
        for (ClassFile.MethodInfo methodInfo : classFile.methods.methodInfos) {
            methods.add(map(methodInfo));
        }
        Class aClass = new Class(classFile.accessFlags, cmd.className, methods,
                classFile.cpInfo, classFile);

        return aClass;
    }

    public static Method map(ClassFile.MethodInfo cfMethodInfo) {
        ClassFile.Code code = cfMethodInfo.getCode();
        return new Method(cfMethodInfo.accessFlags, cfMethodInfo.name, cfMethodInfo.descriptor, code.maxStacks, code.maxLocals, code.getInstructions());
    }

}
