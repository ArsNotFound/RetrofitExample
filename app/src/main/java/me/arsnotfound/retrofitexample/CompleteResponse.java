package me.arsnotfound.retrofitexample;

import java.util.List;
import java.util.Objects;

public class CompleteResponse {
    private boolean endOfWord;
    private int pos;
    private List<String> text;

    public CompleteResponse(boolean endOfWord, int pos, List<String> text) {
        this.endOfWord = endOfWord;
        this.pos = pos;
        this.text = text;
    }

    public boolean isEndOfWord() {
        return endOfWord;
    }

    public void setEndOfWord(boolean endOfWord) {
        this.endOfWord = endOfWord;
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    public List<String> getText() {
        return text;
    }

    public void setText(List<String> text) {
        this.text = text;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CompleteResponse that = (CompleteResponse) o;
        return endOfWord == that.endOfWord && pos == that.pos && text.equals(that.text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(endOfWord, pos, text);
    }
}
