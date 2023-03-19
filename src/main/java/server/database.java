package server;

import java.io.Serializable;

public class database implements Serializable {

    private String text;

    private int index;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
