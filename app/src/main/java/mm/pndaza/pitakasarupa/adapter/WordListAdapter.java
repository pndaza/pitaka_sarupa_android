package mm.pndaza.pitakasarupa.adapter;

import android.content.Context;
import android.graphics.Color;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.simplecityapps.recyclerview_fastscroll.views.FastScrollRecyclerView;

import java.util.ArrayList;

import mm.pndaza.pitakasarupa.R;
import mm.pndaza.pitakasarupa.model.Word;
import mm.pndaza.pitakasarupa.utils.SharePref;

public class WordListAdapter extends RecyclerView.Adapter<WordListAdapter.ViewHolder>
implements FastScrollRecyclerView.SectionedAdapter {

    private View.OnClickListener onItemClickListener;

    private ArrayList<Word> wordList;
    private String filterText;

    public WordListAdapter(ArrayList<Word> list){
        this.wordList = list;
    }

    @NonNull
    @Override
    public WordListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View wordListItemView = inflater.inflate(R.layout.wordlist_row_item, viewGroup, false);
        return new ViewHolder(wordListItemView);
    }

    @Override
    public void onBindViewHolder(@NonNull WordListAdapter.ViewHolder viewHolder, int i) {
        Word word = wordList.get(i);
        TextView textView = viewHolder.textView;
        if(filterText.isEmpty()) {
            textView.setText(word.getWord());
        } else {
            textView.setText(setHighlight(word.getWord()));
        }
    }

    @Override
    public int getItemCount() {
        return wordList.size();
    }

    public void setOnItemClickListener(View.OnClickListener listener) {
        this.onItemClickListener = listener;
    }

    public void setFilteredWordList(ArrayList<Word> filteredList){
        wordList = filteredList;
        notifyDataSetChanged();
    }

    public void setFilterText(String filterText){
        this.filterText = filterText;
    }

    private SpannableString setHighlight(String word){

        SpannableString highlightedText = new SpannableString(word);
        int start_index = word.indexOf(filterText);
        int end_index = start_index + filterText.length();

        highlightedText.setSpan(
                new ForegroundColorSpan(Color.RED), start_index, end_index,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        );

        // set background color for query words

        highlightedText.setSpan(
                new BackgroundColorSpan(Color.YELLOW), start_index, end_index,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        );
        return highlightedText;
    }

    @NonNull
    @Override
    public String getSectionName(int position) {
        char firstChar = wordList.get(position).getWord().charAt(0);
        // check thawethoe
        if(firstChar == '\u1031'){
            firstChar = wordList.get(position).getWord().charAt(1);
        }

        return String.valueOf(firstChar);
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView textView;

        ViewHolder(@NonNull final View itemView) {
            super(itemView);
            itemView.setTag(this);
            textView = itemView.findViewById(R.id.tv_list_item);
            textView.setTextSize(SharePref.getInstance(itemView.getContext()).getPrefFontSize());
            itemView.setOnClickListener(onItemClickListener);
        }
    }

}