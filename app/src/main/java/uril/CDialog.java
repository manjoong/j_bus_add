package uril;

import android.app.Dialog;
import android.content.Context;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.myapplication.R;


/**
 * Created by MARUSOFT-CHOI on 2016-10-11.
 */
public class CDialog {
    private static Dialog m_loadingDialog = null;

    public static void showLoading(Context context) {
        if (m_loadingDialog == null) {
            m_loadingDialog = new Dialog(context, R.style.TransDialog);
            ProgressBar pb = new ProgressBar(context);
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            m_loadingDialog.addContentView(pb, params);
            m_loadingDialog.setCancelable(false);
        }

        m_loadingDialog.show();
    }

    public static void hideLoading() {
        if (m_loadingDialog != null) {
            m_loadingDialog.dismiss();
            m_loadingDialog = null;
        }
    }

}
