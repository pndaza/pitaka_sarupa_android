package mm.pndaza.pitakasarupa.utils;

import android.content.Context;
import android.graphics.Color;
import android.text.Layout;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.AlignmentSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;

import androidx.core.content.ContextCompat;

import mm.pndaza.pitakasarupa.R;
import mm.pndaza.pitakasarupa.model.Detail;

public class TextStyle {
    public static SpannableStringBuilder getStyle(Detail detail, Context context){

        String word = detail.word;
        String content = detail.content;
        String reference = detail.reference;

        if(MDetect.isUnicode()){
            word = Rabbit.zg2uni(word);
            content = Rabbit.zg2uni(content);
            reference = Rabbit.zg2uni(reference);
        }

        SpannableStringBuilder builder = new SpannableStringBuilder();

        SpannableString str1= new SpannableString(word+"\n\n");
        str1.setSpan(new ForegroundColorSpan(
                ContextCompat.getColor(context,R.color.color_secondary)), 0, str1.length(), 0);
        str1.setSpan(new RelativeSizeSpan(1.4f), 0,str1.length(), 0);
        builder.append(str1);

        content = content.replace("\\n", "\n\n");
        SpannableString str2= new SpannableString(content + "\n\n");
        str2.setSpan(new ForegroundColorSpan(
                ContextCompat.getColor(context,R.color.color_on_background)), 0, str2.length(), 0);
        str2.setSpan(new RelativeSizeSpan(1.3f), 0,str2.length(), 0);
        builder.append(str2);

        SpannableString str3= new SpannableString(reference);
        str3.setSpan(new ForegroundColorSpan(Color.GRAY), 0, str3.length(), 0);
        str3.setSpan(new RelativeSizeSpan(1.1f), 0,str3.length(), 0);
        str3.setSpan(new AlignmentSpan.Standard(Layout.Alignment.ALIGN_OPPOSITE),
                0,str3.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.append(str3);

        return builder;
    }
}
