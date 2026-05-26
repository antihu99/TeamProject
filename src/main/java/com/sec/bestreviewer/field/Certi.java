package com.sec.bestreviewer.field;

import com.sec.bestreviewer.util.TertiaryOptionEnum;

public class Certi extends Field{
    private String certi;

    public Certi(String certi) {
        this.certi = certi;
    }

    public int getCerti(){
        if(certi.equals("ADV")) return 0;
        else if(certi.equals("PRO")) return 1;
        else return 2;
    }

    @Override
    public String toString() {
        return certi;
    }

    @Override
    public boolean compare(Field field, int subfieldIndex, TertiaryOptionEnum tertiaryOptionEnum) {
        return compareInt(getCerti(), ((Certi) field).getCerti(), tertiaryOptionEnum);
    }

    @Override
    public int compareTo(Field o) {
        if(this.getCerti() == ((Certi) o).getCerti()) return 0;
        return this.getCerti() > ((Certi) o).getCerti() ? 1 : -1;
    }

    @Override
    public boolean equals(String string) {
        return certi.equals(string);
    }

    @Override
    public boolean equals(Object obj) {
        if (getClass() != obj.getClass()) return false;
        Certi that = (Certi) obj;
        return certi.equals(that.toString());
    }
}
