package org.example.rtda;

import org.example.classFile.ClassFile;
import org.example.classFile.InstructionReader;

import java.util.Map;

public class Method {
    /**
     * 方法访问标识
     */
    public final int accessFlags;
    /**
     * 方法名称索引项
     */
    public final String name;
    public final String descriptor;
    public final int maxLocals;
    public final int maxStacks;

    public final Map<Integer, InstructionReader.Instruction> instructionMap;

    public final ClassFile.LineNumberTable lineNumberTable;
    public Class clazz;

    public Method(int accessFlags, String name, String descriptor, int maxStacks, int maxLocals,
                  Map<Integer, InstructionReader.Instruction> instructionMap,
                  ClassFile.LineNumberTable lineNumberTable) {
        this.accessFlags = accessFlags;
        this.name = name;
        this.descriptor = descriptor;
        this.maxStacks = maxStacks;
        this.maxLocals = maxLocals;
        this.instructionMap = instructionMap;
        this.lineNumberTable = lineNumberTable;
    }
}
