package org.example.classFile;

import org.example.App;
import org.example.rtda.Frame;

import java.io.DataInputStream;
import java.io.IOException;

/**
 * 从classfile中读取二进制码，转换成指令，执行
 */
public class InstructionReader {

    public static Instruction read(int opCode, DataInputStream stm) throws IOException {
        switch (opCode) {
            case 0x10:
                return new BiPushInst(stm.readByte());

            case 0x1b:
                return new ILoad1Inst();
            case 0x1c:
                return new ILoad2Inst();

            case 0x3c:
                return new IStore1Inst();
            case 0x3d:
                return new IStore2Inst();
            case 0x3e:
                return new IStore3Inst();

            case 0x60:
                return new IAddInst();


            case 0xb1:
                return new ReturnInst();

            default:
                return null;
        }
    }

    /**
     * 指令执行接口
     */
    public interface Instruction {
        default int offset() {
            return 1;
        }

        void execute(Frame frame);
    }

    // constants
    public static class BiPushInst implements Instruction {

        public final byte val;

        public BiPushInst(byte val) {
            this.val = val;
        }

        @Override
        public int offset() {
            return 2;
        }

        @Override
        public void execute(Frame frame) {
            frame.pushInt(this.val);
        }

    }

    // control
    public static class ReturnInst implements Instruction {
        @Override
        public void execute(Frame frame) {
            // 解释器同步执行方法的结束条件
            if (frame.stat == Frame.FRAME_RUNNING) {
                frame.stat = Frame.FRAME_END;
            }
        }
    }

    // loads
    public static class ILoad1Inst implements Instruction {
        @Override
        public void execute(Frame frame) {
            Integer tmp = frame.getInt(1);
            frame.pushInt(tmp);
        }
    }

    public static class ILoad2Inst implements Instruction {
        @Override
        public void execute(Frame frame) {
            Integer tmp = frame.getInt(2);
            frame.pushInt(tmp);
        }

    }

    // math
    public static class IAddInst implements Instruction {
        @Override
        public void execute(Frame frame) {
            Integer a1 = frame.popInt();
            Integer a2 = frame.popInt();
            frame.pushInt(a1 + a2);
        }
    }

    // stores
    public static class IStore1Inst implements Instruction {
        @Override
        public void execute(Frame frame) {
            Integer tmp = frame.popInt();
            frame.setInt(1, tmp);
        }
    }

    public static class IStore2Inst implements Instruction {
        @Override
        public void execute(Frame frame) {
            Integer tmp = frame.popInt();
            frame.setInt(2, tmp);
        }
    }

    public static class IStore3Inst implements Instruction {
        @Override
        public void execute(Frame frame) {
            Integer tmp = frame.popInt();
            frame.setInt(3, tmp);
        }
    }
}
