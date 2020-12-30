package mm.pndaza.pitakasarupa.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import mm.pndaza.pitakasarupa.R;
import mm.pndaza.pitakasarupa.adapter.RecentAdapter;
import mm.pndaza.pitakasarupa.database.DBOpenHelper;
import mm.pndaza.pitakasarupa.model.Recent;
import mm.pndaza.pitakasarupa.utils.MDetect;
import mm.pndaza.pitakasarupa.utils.Rabbit;

public class RecentFragment extends Fragment {

    public interface OnRecentSelectedListener {
        void onRecentSelected(int word_id);
    }

    private Context context;
    private RecyclerView recentListView;
    private ArrayList<Recent> recents;
    private TextView emptyInfoView;
    private OnRecentSelectedListener callbackListener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // set title
        String title = getString(R.string.recent_mm);
        if(!MDetect.isUnicode()){
            title = Rabbit.uni2zg(title);
        }
        getActivity().setTitle(title);

        setHasOptionsMenu(true);
        //reuse layout as they are same
        return inflater.inflate(R.layout.fragment_bookmark, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = view.getContext();
        //bind view
        recentListView = view.findViewById(R.id.recyclerViewBookmark);
        recentListView.setLayoutManager(new LinearLayoutManager(context));
        applyRecentList();
        emptyInfoView = view.findViewById(R.id.empty_info);
        applyEmptyInfoView(emptyInfoView);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_recent, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.menu_clearAll) {
            clearRecent();
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            callbackListener = (OnRecentSelectedListener) context;
        } catch (ClassCastException e){
            throw new ClassCastException(context.toString() + " must implemented OnWordlistSelectedListener");

        }
    }

    private void applyRecentList() {
        recents = DBOpenHelper.getInstance(getContext()).getAllRecentWord();
        final RecentAdapter adapter = new RecentAdapter(recents);
        recentListView.setAdapter(adapter);
        adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder) view.getTag();
                int position = viewHolder.getAdapterPosition();
                int word_id = recents.get(position).getWord_id();

                callbackListener.onRecentSelected(word_id);
            }
        });
    }

    private void clearRecent() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context,R.style.AlertDialogTheme);

        String message = "လက်တလော ကြည့်ရှုထားသည်များကို ဖယ်ရှားမှာလား";
        String comfirm = "ဖယ်ရှားမယ်";
        String cancel = "မလုပ်တော့ဘူး";
        if (!MDetect.isUnicode()) {
            message = Rabbit.uni2zg(message);
            comfirm = Rabbit.uni2zg(comfirm);
            cancel = Rabbit.uni2zg(cancel);
        }

        alertDialog.setMessage(message)
                .setCancelable(true)
                .setPositiveButton(comfirm,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                DBOpenHelper.getInstance(context).removeAllRecent();
                                applyRecentList();
                                applyEmptyInfoView(emptyInfoView);
                            }
                        })
                .setNegativeButton(cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
        alertDialog.show();
    }

    private void applyEmptyInfoView(TextView emptyInfoView){

        String info = getString(R.string.recent_empty);
        if (!MDetect.isUnicode()) {
            info = Rabbit.uni2zg(info);
        }
        emptyInfoView.setText(info);
        emptyInfoView.setVisibility(recents.isEmpty() ? View.VISIBLE : View.INVISIBLE);
    }



}
