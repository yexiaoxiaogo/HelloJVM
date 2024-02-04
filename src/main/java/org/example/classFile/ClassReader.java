package org.example.classFile;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ClassReader {
    // 常量信息的类型
    public static final int CONSTANT_Utf8 = 1;
    public static final int CONSTANT_Class = 7;
    public static final int CONSTANT_Methodref = 10;
    public static final int CONSTANT_NameAndType = 12;

    //属性信息的类型
    public static final String ATTRIBUTE_Code = "Code";


    public static ClassFile read(String path) throws IOException {
        try (InputStream is = new FileInputStream(path);
             DataInputStream dataInputStream = new DataInputStream(is)){
            // read
            return read(dataInputStream);
        }
    }

    /**
     * 按顺序读取.class文件的内容
     * @param dataInputStream
     * @return
     */
    private static ClassFile read(DataInputStream dataInputStream) throws IOException {

        int magic = dataInputStream.readInt();
        int minorVersion = dataInputStream.readUnsignedShort();
        int majorVersion = dataInputStream.readUnsignedShort();
        int cpSize = dataInputStream.readUnsignedShort();
        /**
         * 表头给出的常量池大小比实际大1。假设表头给出的值是n，那么常量池的实际大小是n–1。
         * 第二，有效的常量池索引是1~n–1。0是无效索引，表示不指向任何常量
         */
        ClassFile.ConstantPool constantPool = readConstantPool(dataInputStream, cpSize - 1);
        int accessFlag = dataInputStream.readUnsignedShort();
        int thisClass = dataInputStream.readUnsignedShort();
        int superClass = dataInputStream.readUnsignedShort();
        int interfaceCount = dataInputStream.readUnsignedShort();
        int fieldCount = dataInputStream.readUnsignedShort();
        int methodCount = dataInputStream.readUnsignedShort();
        ClassFile.Methods methods = readMethods(dataInputStream, methodCount, constantPool);
        int attributeCount = dataInputStream.readUnsignedShort();
        ClassFile.Attributes attributes =  readAttributes(dataInputStream, attributeCount, constantPool);

        return new ClassFile(
                magic,
                minorVersion,
                majorVersion,
                cpSize,
                constantPool,
                accessFlag,
                thisClass,
                superClass,
                interfaceCount,
                fieldCount,
                methodCount,
                methods,
                attributeCount,
                attributes
        );
    }

    private static ClassFile.Attributes readAttributes(DataInputStream dataInputStream, int attributeCount, ClassFile.ConstantPool constantPool) throws IOException {
        ClassFile.Attributes attributes = new ClassFile.Attributes(attributeCount);
        for (int i = 0; i < attributeCount; i++) {
            ClassFile.Attribute attribute = null;
            int attributeNameIndex = dataInputStream.readUnsignedShort();
            String attributeName = getString(constantPool, attributeNameIndex);
            int attributeLength = dataInputStream.readInt();
            if (ATTRIBUTE_Code.equals(attributeName)) {
                int maxStack = dataInputStream.readUnsignedShort();
                int maxLocals = dataInputStream.readUnsignedShort();

                int codeLength = dataInputStream.readInt();
                byte[] byteCode = readBytes(dataInputStream, codeLength);
                InstructionReader.Instruction[] instructions = readByteCode(byteCode);
                // exceptionTableLength 为空 消费掉 没有操作
                dataInputStream.readUnsignedShort();

                int codeAttributeCount = dataInputStream.readUnsignedShort();
                ClassFile.Attributes codeAttributes = readAttributes(dataInputStream, codeAttributeCount, constantPool);
                attribute = new ClassFile.Code(maxStack, maxLocals, instructions, codeAttributes);
            } else {
                // 只要code属性的拿到指令即可，其他的消费掉不要即可
                readBytes(dataInputStream, attributeLength);
            }
            attributes.attributes[i] = attribute;
        }
        return attributes;
    }

    private static InstructionReader.Instruction[] readByteCode(byte[] byteCode) throws IOException {
        List<InstructionReader.Instruction> instructions = new ArrayList<>();
        try (DataInputStream stm = new DataInputStream(new ByteArrayInputStream(byteCode))) {
            while (stm.available() > 0) {
                int opCode = stm.readUnsignedByte();
                try {
                    InstructionReader.Instruction instruction = InstructionReader.read(opCode, stm);
                    if (instruction == null) {
                        break;
                    }
                    instructions.add(instruction);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
        InstructionReader.Instruction[] ret = new InstructionReader.Instruction[instructions.size()];
        instructions.toArray(ret);
        return ret;
    }

    private static ClassFile.Methods readMethods(DataInputStream dataInputStream, int methodCount, ClassFile.ConstantPool constantPool) throws IOException {
        ClassFile.Methods methods = new ClassFile.Methods(methodCount);
        for (int i = 0; i < methodCount; i++) {
            // accessFlags，descriptorIndex 无用但是需要消费掉，顺序不能变
            int accessFlags = dataInputStream.readUnsignedShort();
            int nameIndex = dataInputStream.readUnsignedShort();
            int descriptorIndex = dataInputStream.readUnsignedShort();
            int attributesCount = dataInputStream.readUnsignedShort();
            ClassFile.Attributes attributes = readAttributes(dataInputStream, attributesCount, constantPool);

            ClassFile.ConstantInfo info = constantPool.infos[nameIndex -1];
            String name = ((ClassFile.Utf8) info).getString();

            methods.methodInfos[i] = new ClassFile.MethodInfo(name, attributes);
        }

        return methods;
    }

    private static ClassFile.ConstantPool readConstantPool(DataInputStream dataInputStream, int cpSize) throws IOException {
        ClassFile.ConstantPool constantPool = new ClassFile.ConstantPool(cpSize);
        for (int i = 0; i < cpSize; i++) {
            int tag = dataInputStream.readUnsignedByte();
            int infoEnum = tag;
            ClassFile.ConstantInfo info = null;
            switch (infoEnum) {
                case CONSTANT_Utf8:
                    int length = dataInputStream.readUnsignedShort();
                    byte[] bytes = readBytes(dataInputStream, length);
                    info = new ClassFile.Utf8(infoEnum, bytes);
                    break;
                case CONSTANT_Class:
                    info = new ClassFile.ClassCp(infoEnum, dataInputStream.readUnsignedShort());
                    break;
                case CONSTANT_Methodref:
                    info = new ClassFile.MethodDef(infoEnum, dataInputStream.readUnsignedShort(),dataInputStream.readUnsignedShort());
                    break;
                case CONSTANT_NameAndType:
                    info = new ClassFile.NameAndType(infoEnum, dataInputStream.readUnsignedShort(), dataInputStream.readUnsignedShort());
                    break;
            }
            constantPool.infos[i] = info;
        }
        return constantPool;
    }

    public static byte[] readBytes(DataInputStream stm, int length) throws IOException {
        byte[] bytes = new byte[length];
        for (int i = 0; i < length; i++) {
            bytes[i] = stm.readByte();
        }
        return bytes;
    }

    public static String getString(ClassFile.ConstantPool cp, int index) {

        return ((ClassFile.Utf8) cp.infos[index - 1]).getString();
    }

}
