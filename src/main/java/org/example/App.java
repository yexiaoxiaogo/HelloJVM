package org.example;

import org.example.classFile.ClassFile;
import org.example.classFile.ClassReader;
import org.example.classFile.InstructionReader;
import org.example.rtda.Frame;
import org.example.rtda.MetaSpace;
import org.example.rtda.Method;
import org.example.rtda.Thread;

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

        //初始化
        MetaSpace.main = new Thread(1024);

        // 查找主函数
        Method method = null;
        // 读取文件
        ClassFile classFile = ClassReader.read(cmd.classPath + "/" + cmd.className + ".class");
        // 只找主方法
        for (ClassFile.MethodInfo methodInfo : classFile.methods.methodInfos) {
            if (Objects.equals("main", methodInfo.name)) {
                ClassFile.Code code = methodInfo.getCode();
                method = new Method(methodInfo.accessFlags, methodInfo.name, methodInfo.descriptor, code.maxStacks, code.maxLocals, code.getInstructions());
            }
        }

        // 运行主函数
        runMain(method);

    }

    /**
     * 解释器
     * @param method
     */
    private static void runMain(Method method) {
        Frame frame = new Frame(method);
        //执行
        final Thread env = MetaSpace.getMainEnv();
        env.pushFrame(frame);

        frame.stat = Frame.FRAME_RUNNING;
        do {
            Frame topFrame = env.topFrame();
            InstructionReader.Instruction instruction = topFrame.getInst();
            topFrame.nextPc += instruction.offset();

            instruction.execute(topFrame);

        } while (frame.stat == Frame.FRAME_RUNNING);

        System.out.println("execute result:" + frame.getLocalVars());

    }

    public static class Args {
        public String classPath = "/Users/yxx/Downloads/mini-jvm-master/example/target/classes";

        public String className = "Hello2";
    }
}
