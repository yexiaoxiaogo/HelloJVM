package org.example;

import org.example.classFile.ClassFile;
import org.example.classFile.ClassReader;
import org.example.classFile.InstructionReader;
import org.example.rtda.Frame;

import java.io.IOException;
import java.util.Objects;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main(String[] args) throws IOException {
        //.class文件的路径和name测试数据写死
        Args cmd = new Args();

        // 读取文件
        ClassFile classFile = ClassReader.read(cmd.classPath + "/" + cmd.className + ".class");

        // 只找主方法,获得指令集，new frame
        Frame frame = null;
        for (ClassFile.MethodInfo methodInfo : classFile.methods.methodInfos) {
            if (Objects.equals("main", methodInfo.name)) {
                ClassFile.Code code = methodInfo.getCode();
                frame = new Frame(code.maxLocals, code.maxStacks, code.getInstructions());
            }
        }

        // 运行主函数
        runMain(frame);

    }

    /**
     * 解释器
     * @param frame
     */
    private static void runMain(Frame frame) {
        //执行
        frame.stat = Frame.FRAME_RUNNING;
        do {
            InstructionReader.Instruction instruction = frame.getInst();
            frame.nextPc += instruction.offset();

            instruction.execute(frame);

        } while (frame.stat == Frame.FRAME_RUNNING);

        System.out.println("execute result:" + frame.getLocalVars());

    }

    public static class Args {
        public String classPath = "/Users/yxx/Downloads/mini-jvm-master/example/target/classes";

        public String className = "Hello2";
    }
}
