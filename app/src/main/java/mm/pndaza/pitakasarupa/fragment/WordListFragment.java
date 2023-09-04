package mm.pndaza.pitakasarupa.fragment;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import mm.pndaza.pitakasarupa.R;
import mm.pndaza.pitakasarupa.adapter.WordListAdapter;
import mm.pndaza.pitakasarupa.database.DBOpenHelper;
import mm.pndaza.pitakasarupa.model.Word;
import mm.pndaza.pitakasarupa.utils.MDetect;

public class WordListFragment extends Fragment {

    public interface OnWordlistSelectedListener {
        void onWordlistSelected(int _id);
    }

    private ArrayList<Word> allWordList;
    private ArrayList<Word> filteredList;
    private WordListAdapter wordListAdapter;
    private OnWordlistSelectedListener callbackListener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        getActivity().setTitle(MDetect.getDeviceEncodedText(getString(R.string.app_name_uni)));
        return inflater.inflate(R.layout.fragment_wordlist, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        MDetect.init(this.getContext());
        RecyclerView listView = view.findViewById(R.id.wordListView);
        allWordList = DBOpenHelper.getInstance(this.getContext()).getAllWordList(MDetect.isUnicode());
        filteredList = new ArrayList<>();
        wordListAdapter = new WordListAdapter(allWordList);
        listView.setAdapter(wordListAdapter);
        listView.setLayoutManager(new LinearLayoutManager(this.getContext()));

        // handle click event
        wordListAdapter.setOnItemClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder) view.getTag();
                int position = viewHolder.getAdapterPosition();
                int size = wordListAdapter.getItemCount();
                int word_id;
                if(size == allWordList.size()) {
                     word_id = allWordList.get(position).get_id();
                } else {
                    word_id = filteredList.get(position).get_id();
                }
                callbackListener.onWordlistSelected(word_id);
            }
        });

        wordListAdapter.setFilterText("");

        final EditText searchInput = view.findViewById(R.id.search);
        final ImageButton btnClear = view.findViewById(R.id.btn_clear);
        searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                setShowHide(btnClear, editable.toString().isEmpty());
                doFilter(editable.toString());
            }
        });

        setupClearButton(btnClear, searchInput);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            callbackListener = (OnWordlistSelectedListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implemented OnWordlistSelectedListener");
        }
    }

    private void doFilter(String filter) {
        filteredList.clear();
        if (filter.isEmpty()) {
            wordListAdapter.setFilteredWordList(allWordList);
            wordListAdapter.setFilterText("");
        } else {
            for (Word word : allWordList) {
                if (word.getWord().contains(filter)) {
                    filteredList.add(word);
                }
            }
            wordListAdapter.setFilteredWordList(filteredList);
            wordListAdapter.setFilterText(filter);
            if(filteredList.size() == 0 ){
                Toast.makeText(getContext(), MDetect.getDeviceEncodedText("ရှာမတွေ့ပါ!"),
                        Toast.LENGTH_SHORT
                ).show();
            }
        }

    }

    private void setupClearButton(ImageButton btnClear, final EditText search) {

        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search.setText("");
            }
        });
    }

    private void setShowHide(ImageButton imageButton, boolean isEmpty) {

        if (isEmpty) {
            imageButton.setVisibility(View.INVISIBLE);
        } else {
            imageButton.setVisibility(View.VISIBLE);
        }
    }
}
