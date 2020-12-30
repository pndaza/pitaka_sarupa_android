package mm.pndaza.pitakasarupa.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import mm.pndaza.pitakasarupa.R;
import mm.pndaza.pitakasarupa.database.DBOpenHelper;
import mm.pndaza.pitakasarupa.model.Bookmark;
import mm.pndaza.pitakasarupa.utils.MDetect;
import mm.pndaza.pitakasarupa.utils.Rabbit;
import mm.pndaza.pitakasarupa.utils.SharePref;

public class BookmarkAdapter extends RecyclerView.Adapter<BookmarkAdapter.ViewHolder> {

    private OnBookmarksChangeListener changeListener;
    public interface OnBookmarksChangeListener {
        void onChange(boolean isBookmarksEmpty);
    }

    public void setOnBookmarksChangeListener(OnBookmarksChangeListener listener){
        changeListener = listener;
    }

    private Context context;
    private ArrayList<Bookmark> bookmarks;
    private View.OnClickListener onItemClickListener;

    public BookmarkAdapter(ArrayList<Bookmark> bookmarks){
        this.bookmarks = bookmarks;
    }

    @NonNull
    @Override
    public BookmarkAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View bookmark_row_item = layoutInflater.inflate(R.layout.bookmark_row_item, parent, false);
        return new ViewHolder(bookmark_row_item);
    }

    @Override
    public void onBindViewHolder(@NonNull BookmarkAdapter.ViewHolder viewHolder, final int position) {
        final Bookmark bookmark = bookmarks.get(position);
        final int word_id = bookmark.getWord_id();
        TextView tv_word = viewHolder.tv_word;
        tv_word.setText(bookmark.getWord());

        ImageButton btnDelete = viewHolder.bgn_delete;
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteBookmark(position,word_id);
            }
        });

        CheckBox checkBox = viewHolder.chk_selected;
        if(Bookmark.isCheckboxShow){
            checkBox.setVisibility(View.VISIBLE);
        } else {
            checkBox.setVisibility(View.GONE);
        }

        checkBox.setChecked(bookmark.isSelected());
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean flag) {
                bookmark.setSelected(flag);
            }
        });

    }

    @Override
    public int getItemCount() {
        return bookmarks.size();
    }

    public void setOnItemClickListener(View.OnClickListener clickListener){
        onItemClickListener = clickListener;
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView tv_word;
        ImageButton bgn_delete;
        CheckBox chk_selected;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_word = itemView.findViewById(R.id.tv_word);
            tv_word.setTextSize(SharePref.getInstance(itemView.getContext()).getPrefFontSize());
            bgn_delete = itemView.findViewById(R.id.btn_delete);
            chk_selected = itemView.findViewById(R.id.chk_selected);
            itemView.setTag(this);
            itemView.setOnClickListener(onItemClickListener);
        }
    }

    // confirmation dialog box to delete an unit
    private void deleteBookmark(final int position, final int word_id) {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context,R.style.AlertDialogTheme);
        String message = "ဖျက်ရန် သေချာပြီလား";
        String comfirm = "သေချာတယ်";
        String cancel = "မဖျက်တော့ဘူး";
        if(!MDetect.isUnicode()){
            message = Rabbit.uni2zg(message);
            comfirm = Rabbit.uni2zg(comfirm);
            cancel = Rabbit.uni2zg(cancel);
        }

        alertDialog.setMessage(message)
                .setCancelable(true)
                .setPositiveButton(comfirm,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                removeFromDatabase(word_id);
                                bookmarks.remove(position);
                                notifyDataSetChanged();
                                changeListener.onChange(bookmarks.isEmpty());
                            }
                        })
                .setNegativeButton(cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
        alertDialog.show();
    }

    private void removeFromDatabase(int word_id){
        DBOpenHelper.getInstance(context).removeBookmarkById(word_id);

    }

}
