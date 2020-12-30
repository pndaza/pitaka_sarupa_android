package mm.pndaza.pitakasarupa.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.ListFragment;

import java.util.Arrays;

import mm.pndaza.pitakasarupa.R;
import mm.pndaza.pitakasarupa.utils.MDetect;
import mm.pndaza.pitakasarupa.utils.SharePref;

public class SettingFragment extends ListFragment {

    private Context context;

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getActivity().setTitle(MDetect.getDeviceEncodedText(getString(R.string.setting_mm)));

        context = getContext();
        String[] settingList = MDetect.isUnicode() ?
                new String[] {"အရောင်စုဖွဲ့မှု", "စာလုံးအရွယ်အစား"}: new String[]{"အေရာင္စုဖြဲ႕မႈ", "စာလုံးအ႐ြယ္အစား"};
        ArrayAdapter adapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_list_item_1, settingList);
        setListAdapter(adapter);
    }

    @Override
    public void onListItemClick(ListView listView, View view, int position, long id) {
        if (position == 0) {
            saveTheme();
        } else if (position == 1) {
            saveFontSize();
        }
    }

    private void saveTheme() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        final String[] themeNames = MDetect.isUnicode() ?
                new String[] {"နေ့", "ည"}: new String[]{"ေန႔", "ည"};
        final Boolean[] themeValues = {false, true};
        int current = Arrays.asList(themeValues).
                indexOf(SharePref.getInstance(context).getPrefNightModeState());
        alertDialog.
                setSingleChoiceItems(themeNames, current, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        SharePref.getInstance(context).setPrefNightModeState(themeValues[i]);
                        dialogInterface.dismiss();
                        if (SharePref.getInstance(context).getPrefNightModeState()) {
                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                        } else {
                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                        }
                        getActivity().recreate();
                    }
                });
        alertDialog.show();
    }

    private void saveFontSize() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        final String[] fontSizeNames = MDetect.isUnicode() ?
                new String[] {"အသေး", "အလတ်" , "အကြီး"}: new String[]{"အေသး", "အလတ္" , "အႀကီး"};
        final Integer[] fontSizeValues = {15, 18, 21};
        int current = Arrays.asList(fontSizeValues).
                indexOf(SharePref.getInstance(context).getPrefFontSize());
        alertDialog.
                setSingleChoiceItems(fontSizeNames, current, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        SharePref.getInstance(context).setPrefFontSize(fontSizeValues[i]);
                        dialogInterface.dismiss();
                    }
                });
        alertDialog.show();

    }


}
