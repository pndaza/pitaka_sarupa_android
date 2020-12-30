package mm.pndaza.pitakasarupa.model;

public class Recent {

    private int word_id;
    private String word;

    public Recent(int word_id, String word){
        this.word_id = word_id;
        this.word = word;
    }

    public int getWord_id() { return word_id;}

    public void setWord_id(int word_id){ this.word_id = word_id; }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }


}
