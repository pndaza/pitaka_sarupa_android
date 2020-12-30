package mm.pndaza.pitakasarupa.model;

public class Bookmark {

    private int word_id;
    private String word;
    private boolean isSelected;
    public static boolean isCheckboxShow;


    public Bookmark(int word_id, String word){
        this.word_id = word_id;
        this.word = word;
    }

    public int getWord_id() { return word_id;}

    public String getWord() {
        return word;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (!(obj instanceof Bookmark)) return false;
        Bookmark temp = (Bookmark) obj;
        return temp.word_id == this.word_id;
    }

}
