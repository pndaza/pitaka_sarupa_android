package mm.pndaza.pitakasarupa.model;

public class Word {
    private int _id;
    private String word;

    public Word(int _id, String word){
        this._id = _id;
        this.word = word;
    }

    public int get_id() {
        return _id;
    }

    public String getWord() {
        return word;
    }
}
