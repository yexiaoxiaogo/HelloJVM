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
    public static void main( String[] args ) throws IOException {
        //解析入参
        Args cmd = parseArgs(args);

        //搜索类路径
        ClassLoader classLoader = new ClassLoader("boot", cmd.classPath);

        //初始化
        MetaSpace.main = new Thread(1024);

        //加载主类
        String mainClass = cmd.className;
        classLoader.loadClass(mainClass);

        // 查找主函数
        Class clazz = Heap.findClass(mainClass);
        Method method = clazz.getMainMethod();

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

    // 解析入参 入参固定格式为 -cp classpath classname
    private static Args parseArgs(String[] args){
        Args args1 = new Args();
        args1.MINUS_CP = args[0];
        args1.classPath = args[1];
        args1.className = args[2];
        return args1;
    }

    public static class Args {
        public String MINUS_CP;

        public String classPath;

        public String className;
    }
}
