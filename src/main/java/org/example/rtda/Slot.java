package org.example.rtda;

public class Slot {
    public Integer num;
    public Instance ref;

    public Slot(int num) {
        this.num = num;
        this.ref = null;
    }

    public Slot(Instance ref) {
        this.num = null;
        this.ref = ref;
    }

    @Override
    public String toString() {
        String sb = "Slot{" + "num=" + num +
                ", ref=" + ref +
                '}';
        return sb;
    }

    public class Instance implements Cloneable{

    }
}
