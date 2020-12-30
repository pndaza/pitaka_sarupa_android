package mm.pndaza.pitakasarupa.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import mm.pndaza.pitakasarupa.R;
import mm.pndaza.pitakasarupa.model.Recent;
import mm.pndaza.pitakasarupa.utils.SharePref;

public class RecentAdapter extends RecyclerView.Adapter<RecentAdapter.ViewHolder> {

    private ArrayList<Recent> recentList;
    private View.OnClickListener onClickListener;

    public RecentAdapter(ArrayList<Recent> recentList) {
        this.recentList = recentList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        // reuse layout because layout are same
        View wordListItemView = inflater.inflate(R.layout.wordlist_row_item, parent, false);
        return new ViewHolder(wordListItemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Recent recent = recentList.get(position);
        holder.textView.setText(recent.getWord());
    }

    @Override
    public int getItemCount() {
        return recentList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView textView;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.tv_list_item);
            textView.setTextSize(
                    SharePref.getInstance(itemView.getContext()).getPrefFontSize());

            itemView.setTag(this);
            itemView.setOnClickListener(onClickListener);
        }
    }

    public void setOnClickListener(View.OnClickListener clickListener) {
        onClickListener = clickListener;
    }

}
