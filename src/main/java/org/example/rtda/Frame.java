package org.example.rtda;

import org.example.classFile.InstructionReader;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class Frame {

    public static final int FRAME_RUNNING = 1;
    public static final int FRAME_END = 2;

    // 状态 FRAME_RUNNING = 1; FRAME_END = 2;
    public static int stat;

    private final LocalVars localVars;

    private final OperandStack operandStack;

    public final byte[] byteCode;

    public int pc = 0;

    public int nextPc;

    /**
     * slot 基本存储单元
     */
    public class Slot {
        public Integer num;

        public Slot(int num) {
            this.num = num;
        }


        @Override
        public String toString() {
            String sb = "Slot{" + "num=" + num + '}';
            return sb;
        }
    }

    /**
     * 本地变量
     */
    public class LocalVars {
        private final Slot[] slots;

        public LocalVars(int size) {
            this.slots = new Slot[size];
        }

        public String toString() {
            return Arrays.stream(slots).map(t -> t == null ? "" : t.toString()).collect(Collectors.joining("\n"));
        }

        public int getInt(int index) {
            return slots[index].num;
        }

        public void setInt(int index, int val) {
            slots[index] = new Slot(val);
        }
    }
    /**
     * 操作数栈
     */
    public class OperandStack {
        private int top;
        private final Slot[] slots;

        public OperandStack(Integer size) {
            slots = new Slot[size];
            top = 0;
        }

        public void pushInt(int val) {
            this.slots[top++] = new Slot(val);
        }

        public Slot pop() {
            top--;
            final Slot slot = this.slots[top];
            this.slots[top] = null; // gc
            return slot;
        }

        public int popInt() {
            return this.pop().num;
        }
    }

    public Frame(int maxLocals,  int maxStacks, byte[] byteCode) {
        this.localVars = new LocalVars(maxLocals);
        this.operandStack = new OperandStack(maxStacks);
        this.byteCode = byteCode;
        this.pc = 0;
    }


    public String getLocalVars() {
        return this.localVars.toString();
    }


    public int getInt(int index) {
        return this.localVars.getInt(index);
    }

    public void setInt(int index, int val) {
        this.localVars.setInt(index, val);
    }

    public void pushInt(int val) {
        this.operandStack.pushInt(val);
    }

    public int popInt() {
        return this.operandStack.popInt();
    }
}
