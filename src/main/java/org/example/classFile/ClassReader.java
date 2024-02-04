package org.example.classFile;

import java.io.*;


public class ClassReader {
    // 常量信息的类型
    public static final int CONSTANT_Utf8 = 1;
    public static final int CONSTANT_Class = 7;
    public static final int CONSTANT_Methodref = 10;
    public static final int CONSTANT_NameAndType = 12;

    //属性信息的类型
    public static final String ATTRIBUTE_Code = "Code";


    /**
     * 从常量池中匹配属性名
     *
     * @param cp
     * @param index
     * @return
     */
    public static String getNameFromCP(ClassFile.ConstantPool cp, int index) {
        return ((ClassFile.Utf8) cp.infos[index - 1]).getString();
    }

    /**
     * 读取指定字节数量
     *
     * @param stm
     * @param length
     * @return
     * @throws IOException
     */
    public static byte[] readBytes(DataInputStream stm, int length) throws IOException {
        byte[] bytes = new byte[length];
        for (int i = 0; i < length; i++) {
            bytes[i] = stm.readByte();
        }
        return bytes;
    }


    /**
     * 读取.class文件
     *
     * @param path
     * @return
     */
    public static ClassFile read(String path) throws IOException {
        // JVM规范，读取顺序不能变。字节大小不能变。
        InputStream in = new FileInputStream(path);
        DataInputStream is = new DataInputStream(in);
        int magic = is.readInt();
        int minorVersion = is.readUnsignedShort();
        int majorVersion = is.readUnsignedShort();

        int constantPoolSize = is.readUnsignedShort();
        // 常量池大小包括了属性constantPoolSize本身，所以常量池真正的大小需要-1 减去constantPoolSize本身
        ClassFile.ConstantPool cpInfo = readConstantPool(is, constantPoolSize - 1);

        int accessFlags = is.readUnsignedShort();
        int thisClass = is.readUnsignedShort();
        int superClass = is.readUnsignedShort();
        // 接口数是0，省略接口表的读取
        int interfacesCount = is.readUnsignedShort();
        // 字段数是0，省略字段表的读取
        int fieldCount = is.readUnsignedShort();

        int methodsCount = is.readUnsignedShort();
        ClassFile.Methods methods = readMethods(is, methodsCount, cpInfo);

        int attributesCount = is.readUnsignedShort();
        ClassFile.Attributes attributes = readAttributes(is, attributesCount, cpInfo);

        return new ClassFile(magic, minorVersion, majorVersion, constantPoolSize,
                cpInfo, accessFlags, thisClass, superClass, interfacesCount,
                fieldCount, methodsCount, methods, attributesCount, attributes);
    }

    private static ClassFile.Attributes readAttributes(DataInputStream is, int attributesCount, ClassFile.ConstantPool cpInfo) throws IOException {
        ClassFile.Attributes attributes = new ClassFile.Attributes(attributesCount);
        for (int i = 0; i < attributesCount; i++) {
            ClassFile.Attribute attribute = null;
            int attrNameIndex = is.readUnsignedShort();
            int attrLength = is.readInt();
            String attrName = getNameFromCP(cpInfo, attrNameIndex);
            if (ATTRIBUTE_Code.equals(attrName)) {
                // 只处理code，里面有指令
                int maxStack = is.readUnsignedShort();
                int maxLocals = is.readUnsignedShort();
                int codeLength = is.readInt();
                // 读取转指令
                byte[] byteCode = readBytes(is, codeLength);

                // exceptionTalbeLength 无用但是要消费掉
                int exceptionTalbeLength = is.readUnsignedShort();
                // 属性里，code类型属性的属性，消费掉里面的字节
                int codeAttributeCount = is.readUnsignedShort();
                readAttributes(is, codeAttributeCount, cpInfo);
                attribute = new ClassFile.Code(maxStack, maxLocals, byteCode);

            } else {
                // 其他的不管，读取掉length长度的内容即可
                readBytes(is, attrLength);
            }

            attributes.attributes[i] = attribute;
        }
        return attributes;
    }

    private static ClassFile.Methods readMethods(DataInputStream is, int methodsCount, ClassFile.ConstantPool cpInfo) throws IOException {
        ClassFile.Methods methods = new ClassFile.Methods(methodsCount);
        for (int i = 0; i < methodsCount; i++) {
            // accessFlag,descIndex 这里无用但要读取掉。
            int accessFlag = is.readUnsignedShort();
            int nameIndex = is.readUnsignedShort();
            int descIndex = is.readUnsignedShort();

            int attrCount = is.readUnsignedShort();
            ClassFile.Attributes attributes = readAttributes(is, attrCount, cpInfo);

            // 从常量池中得到方法名，后面需要查找main方法
            String methodName = getNameFromCP(cpInfo, nameIndex);
            methods.methodInfos[i] = new ClassFile.MethodInfo(methodName, attributes);
        }
        return methods;
    }

    private static ClassFile.ConstantPool readConstantPool(DataInputStream is, int constantPoolSize) throws IOException {

        ClassFile.ConstantPool constantPool = new ClassFile.ConstantPool(constantPoolSize);
        for (int i = 0; i < constantPoolSize; i++) {
            ClassFile.ConstantInfo info = null;
            int constantPoolType = is.readUnsignedByte();
            switch (constantPoolType) {
                case CONSTANT_Methodref:
                case CONSTANT_NameAndType:
                    // 读走4个,不处理
                    readBytes(is, 4);
                    break;
                case CONSTANT_Class:
                    // 读走两个不处理
                    readBytes(is, 2);
                    break;
                case CONSTANT_Utf8:
                    int length = is.readUnsignedShort();
                    byte[] bytes = readBytes(is, length);
                    info = new ClassFile.Utf8(constantPoolType, bytes);
                    break;
            }
            constantPool.infos[i] = info;
        }
        return constantPool;
    }
}
