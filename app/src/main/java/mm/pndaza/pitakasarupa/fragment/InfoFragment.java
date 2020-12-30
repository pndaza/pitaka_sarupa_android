package mm.pndaza.pitakasarupa.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import mm.pndaza.pitakasarupa.R;
import mm.pndaza.pitakasarupa.utils.MDetect;
import mm.pndaza.pitakasarupa.utils.SharePref;

public class InfoFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().setTitle(MDetect.getDeviceEncodedText(getString(R.string.info_mm)));
        return inflater.inflate(R.layout.fragment_info, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        WebView webView = view.findViewById(R.id.webView_info);
        if(SharePref.getInstance(getContext()).getPrefNightModeState()) {
            webView.loadUrl("file:///android_asset/web/info-night.html");
        } else {
            webView.loadUrl("file:///android_asset/web/info.html");
        }
    }
}
