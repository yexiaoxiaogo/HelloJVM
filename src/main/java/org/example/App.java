package org.example;

import org.example.classFile.InstructionReader;
import org.example.classLoader.ClassLoader;
import org.example.rtda.Class;
import org.example.rtda.Thread;
import org.example.rtda.*;

import java.io.IOException;

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

        //加载主类
        Class aClass = ClassLoader.loadClass(cmd);

        // 查找主函数
        Method method = aClass.getMainMethod();

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

        System.out.println("execute result: \n" + frame.getLocalVars());

    }

    public static class Args {
        public String classPath = "/Users/yxx/Downloads/mini-jvm-master/example/target/classes";

        public String className = "Hello2";
    }
}
