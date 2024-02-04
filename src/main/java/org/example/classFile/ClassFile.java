package org.example.classFile;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 顺序是固定的，不可改变。classfile读取字节
 */
public class ClassFile {
    public final int magic;//魔数
    public final int minorVersion;//jvm的次版本号
    public final int majorVersion;//jvm的主版本号
    public final int constantPoolSize;//常量池大小(常量池常量个数) 用来解析后面的常量池
    public final ConstantPool cpInfo;//常量池结构
    public final int accessFlags;//类访问标志,16位的“bitmask”指出class文件定义的是类还是接口，访问级别是public还是private，
    public final int thisClass;//类索引,class文件存储的类名类似完全限定名
    public final int superClass;//父类索引,同上
    public final int interfacesCount;//接口索引数量
    public final int fieldCount;//字段数
    public final int methodsCount;//方法数
    public final Methods methods;//方法表，
    public final int attributesCount;//属性数
    public final Attributes attributes;//属性结构，存储属性信息

    public ClassFile(int magic, int minorVersion, int majorVersion, int constantPoolSize,
                     ConstantPool cpInfo, int accessFlags, int thisClass, int superClass, int interfacesCount,
                     int fieldCount, int methodsCount, Methods methods, int attributesCount,
                     Attributes attributes) {
        this.magic = magic;
        this.minorVersion = minorVersion;
        this.majorVersion = majorVersion;
        this.constantPoolSize = constantPoolSize;
        this.cpInfo = cpInfo;
        this.accessFlags = accessFlags;
        this.thisClass = thisClass;
        this.superClass = superClass;
        this.interfacesCount = interfacesCount;
        this.fieldCount = fieldCount;
        this.methodsCount = methodsCount;
        this.methods = methods;
        this.attributesCount = attributesCount;
        this.attributes = attributes;
    }

    /**
     * 常量池结构
     */
    public static class ConstantPool{
        public final ConstantInfo[] infos;

        public ConstantPool(int size) {
            this.infos = new ConstantInfo[size];
        }

    }
    /**
     * 常量信息
     */
    public static class ConstantInfo {
        public final int infoEnum;

        public ConstantInfo(int infoEnum) {
            this.infoEnum = infoEnum;
        }
    }

    // 不同的常量信息类型
    public static class ClassCp extends ConstantInfo {
        public final int nameIndex;

        public ClassCp(int infoEnum, int nameIndex) {
            super(infoEnum);
            this.nameIndex = nameIndex;
        }
    }

    public static class MethodDef extends ConstantInfo {
        public final int classIndex;
        public final int nameAndTypeIndex;

        public MethodDef(int infoEnum, int classIndex, int nameAndTypeIndex) {
            super(infoEnum);
            this.classIndex = classIndex;
            this.nameAndTypeIndex = nameAndTypeIndex;
        }
    }

    public static class NameAndType extends ConstantInfo {
        public final int nameIndex;
        public final int descriptionIndex;

        public NameAndType(int infoEnum, int nameIndex, int descriptionIndex) {
            super(infoEnum);
            this.nameIndex = nameIndex;
            this.descriptionIndex = descriptionIndex;
        }
    }

    public static class Utf8 extends ConstantInfo {
        public final byte[] bytes;

        public Utf8(int infoEnum, byte[] bytes) {
            super(infoEnum);
            this.bytes = bytes;
        }

        public final String getString() {

            return new String(bytes);
        }
    }

    /**
     * 方法结构
     */
    public static class Methods {
        public final MethodInfo[] methodInfos;

        public Methods(int methodCount) {
            this.methodInfos = new MethodInfo[methodCount];
        }
    }
    /**
     * 方法信息
     */
    public static class MethodInfo {
        public final String name;

        public final Attributes attributes;

        public MethodInfo(String name, Attributes attributes) {
            this.name = name;
            this.attributes = attributes;
        }

        public Code getCode() {
            for (Attribute attribute : attributes.attributes) {
                if (attribute instanceof Code) {
                    return ((Code) attribute);
                }
            }
            return null;
        }
    }

    /**
     * 属性结构
     */
    public static class Attributes {
        public final Attribute[] attributes;

        public Attributes(int size) {
            this.attributes = new Attribute[size];
        }
    }
    /**
     * 属性信息
     */
    public static class Attribute {
    }

    // 不同的属性信息类型
    public static class Code extends Attribute {

        public final int maxStacks;
        public final int maxLocals;
        public final InstructionReader.Instruction[] instructions;
        public final Attributes attributes;

        public Code(int maxStacks, int maxLocals, InstructionReader.Instruction[] instructions, Attributes attributes) {
            this.maxStacks = maxStacks;
            this.maxLocals = maxLocals;
            this.instructions = instructions;
            this.attributes = attributes;
        }

        public Map<Integer, InstructionReader.Instruction> getInstructions() {
            Map<Integer, InstructionReader.Instruction> map = new LinkedHashMap<>(instructions.length);
            int pc = 0;
            for (InstructionReader.Instruction instruction : instructions) {
                map.put(pc, instruction);
                pc += instruction.offset();
            }
            return map;
        }

    }
}
