package org.example.rtda;

import org.example.classFile.InstructionReader;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class Frame {

    public static final int FRAME_RUNNING = 1;
    public static final int FRAME_END = 2;
    public final Method method;

    public final Thread thread;

    private final LocalVars localVars;

    private final OperandStack operandStack;

    private final Map<Integer, InstructionReader.Instruction> instructionMap;

    private int pc;

    public int nextPc;

    // 状态 FRAME_RUNNING = 1; FRAME_END = 2;
    public int stat;

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
        public Slot.Instance getRef(int index) {
            return slots[index].ref;
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

        public void push(Slot slot) {
            this.slots[top++] = slot;
        }
        public void pushInt(int val) {
            this.slots[top++] = new Slot(val);
        }

        public void pushRef(Slot.Instance val) {
            this.push(new Slot(val));
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

    public Frame(Method method) {
        this.method = method;
        this.localVars = new LocalVars(method.maxLocals);
        this.operandStack = new OperandStack(method.maxStacks);
        this.thread = MetaSpace.getMainEnv();
        this.instructionMap = method.instructionMap;
    }

    public InstructionReader.Instruction getInst() {
        this.pc = nextPc;
        return this.instructionMap.get(this.pc);
    }

    public String getLocalVars() {
        return this.localVars.toString();
    }

    public Slot.Instance getRef(int index) {
        return this.localVars.getRef(index);
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

    public void pushRef(Slot.Instance val) {
        this.operandStack.pushRef(val);
    }

    public int popInt() {
        return this.operandStack.popInt();
    }
}
