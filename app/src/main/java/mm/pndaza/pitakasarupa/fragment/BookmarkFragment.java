package mm.pndaza.pitakasarupa.fragment;

import android.content.Context;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sdsmdg.tastytoast.TastyToast;

import java.util.ArrayList;
import java.util.Iterator;

import mm.pndaza.pitakasarupa.R;
import mm.pndaza.pitakasarupa.adapter.BookmarkAdapter;
import mm.pndaza.pitakasarupa.database.DBOpenHelper;
import mm.pndaza.pitakasarupa.model.Bookmark;
import mm.pndaza.pitakasarupa.utils.MDetect;
import mm.pndaza.pitakasarupa.utils.MyanNumber;

public class BookmarkFragment extends Fragment {

    public interface OnBookmarkSelectedListener {
        void onBookmarkSelected(int word_id);
    }

    private OnBookmarkSelectedListener callbackListener;
    private boolean isAllMenuItemShowed = false;
    final private ArrayList<Bookmark> bookmarks =
            DBOpenHelper.getInstance(getContext()).getAllBookmarks(MDetect.isUnicode());
    private BookmarkAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().setTitle(MDetect.getDeviceEncodedText(getString(R.string.bookmark_mm)));

        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_bookmark, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final RecyclerView bookmarkListView = view.findViewById(R.id.recyclerViewBookmark);
        adapter = new BookmarkAdapter(bookmarks);
        bookmarkListView.setAdapter(adapter);
        bookmarkListView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        adapter.setOnItemClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder) view.getTag();
                int position = viewHolder.getAdapterPosition();
                int word_id = bookmarks.get(position).getWord_id();
                callbackListener.onBookmarkSelected(word_id);
            }
        });

        setupEmptyInfoView(view, bookmarks, adapter);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            callbackListener = (OnBookmarkSelectedListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implemented OnWordlistSelectedListener");

        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_bookmark, menu);

        MenuItem itemSelectAll = menu.findItem(R.id.menu_select_all);
        MenuItem itemDelete = menu.findItem(R.id.menu_delete);
        MenuItem itemEdit = menu.findItem(R.id.menu_edit);

        if (isAllMenuItemShowed) {
            itemEdit.setIcon(R.drawable.ic_cancel);
            itemSelectAll.setVisible(true);
            itemDelete.setVisible(true);
        } else {
            itemEdit.setIcon(R.drawable.ic_edit);
            itemSelectAll.setVisible(false);
            itemDelete.setVisible(false);

            if (Bookmark.isCheckboxShow) {
                Bookmark.isCheckboxShow = false;
            }
        }

        super.onCreateOptionsMenu(menu, inflater);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_edit:
                if (!isAllMenuItemShowed) {
                    Bookmark.isCheckboxShow = true;
                    isAllMenuItemShowed = true;
                } else {
                    Bookmark.isCheckboxShow = false;
                    isAllMenuItemShowed = false;
                    item.setIcon(R.drawable.ic_edit);
                    for (Bookmark bookmark : bookmarks) {
                        bookmark.setSelected(false);
                    }
                }
                getActivity().invalidateOptionsMenu();
                adapter.notifyDataSetChanged();
                break;

            case R.id.menu_delete:

                Iterator<Bookmark> iterator = bookmarks.iterator();
                int deleteCount = 0;
                while (iterator.hasNext()) {
                    Bookmark bookmark = iterator.next();
                    if (bookmark.isSelected()) {
                        iterator.remove();
                        DBOpenHelper.getInstance(getContext()).removeBookmarkById(bookmark.getWord_id());
                        deleteCount++;
                    }
                }

                if (deleteCount == 0) {
                    String msg = MDetect.getDeviceEncodedText("ဖျက်လိုသည်များကို အမှန်ခြစ် အရင်ပေးပါ။");
                    TastyToast.makeText(getContext(),
                            msg, TastyToast.LENGTH_LONG, TastyToast.ERROR);
                } else {
                    Bookmark.isCheckboxShow = false;
                    adapter.notifyDataSetChanged();

                    String msg = MDetect.getDeviceEncodedText(
                            "သိမ်းဆည်းချက် (" + MyanNumber.toMyanmar(deleteCount) + ") ခုကို ဖျက်လိုက်ပါပြီ။");
                    TastyToast.makeText(getContext(),
                            msg, TastyToast.LENGTH_LONG, TastyToast.INFO);

                    isAllMenuItemShowed = false;
                    item.setIcon(R.drawable.ic_edit);
                    getActivity().invalidateOptionsMenu();
                }
                break;

            case R.id.menu_select_all:
                for (Bookmark bookmark : bookmarks) {
                    if (bookmark.isSelected()) {
                        bookmark.setSelected(false);
                    } else {
                        bookmark.setSelected(true);
                    }
                }
                adapter.notifyDataSetChanged();
                break;
        }

        return super.onOptionsItemSelected(item);
    }


    private void setupEmptyInfoView(View view, ArrayList<Bookmark> bookmarks, BookmarkAdapter adapter) {
        final TextView emptyInfoView = view.findViewById(R.id.empty_info);
        String info = MDetect.getDeviceEncodedText(getString(R.string.bookmark_empty));
        emptyInfoView.setText(info);
        emptyInfoView.setVisibility(bookmarks.isEmpty() ? View.VISIBLE : View.INVISIBLE);
        adapter.setOnBookmarksChangeListener(new BookmarkAdapter.OnBookmarksChangeListener() {
            @Override
            public void onChange(boolean isBookmarksEmpty) {
                emptyInfoView.setVisibility(isBookmarksEmpty ? View.VISIBLE : View.INVISIBLE);
            }
        });
    }
}
