package org.example.rtda;

public class Thread {
    public int top;

    public Frame[] frames;

    public Thread(int maxStackSize) {
        this.frames = new Frame[maxStackSize];
    }

    // 新增一个栈帧
    public void pushFrame(Frame frame) {

        if (top >= this.frames.length) {
            throw new IllegalStateException("stackoverflow");
        }
        frames[top++] = frame;
    }

    // 当前栈顶栈帧
    public Frame topFrame() {
        return this.frames[top - 1];
    }

    public Frame popFrame() {
        if (top < 1) {
            throw new IllegalStateException("empty stack");
        }
        Frame frame = frames[--top];
        // 从数组中移除引用
        frames[top] = null;

        return frame;
    }
}
