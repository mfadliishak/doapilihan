package com.mfadli.doapilihan.activities;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ArrayAdapter;

import com.mfadli.doapilihan.DoaPilihanApp;
import com.mfadli.doapilihan.R;
import com.mfadli.doapilihan.adapter.BGPatternListArrayAdapter;
import com.mfadli.doapilihan.event.GeneralEvent;
import com.mfadli.doapilihan.event.RxBus;
import com.mfadli.doapilihan.model.BGPattern;
import com.mfadli.utils.Analytic;
import com.mfadli.utils.BitmapCacher;

import java.util.List;

/**
 * Created by mfad on 24/03/2016.
 */
public class BaseActivity extends AppCompatActivity {

    private static final String LOG_TAG = BaseActivity.class.getSimpleName();
    protected RxBus mRxBus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mRxBus = ((DoaPilihanApp) DoaPilihanApp.getContext()).getRxBusSingleton();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_change_background) {
            showPattern();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Show Dialog Alert of BGPattern list.
     */
    private void showPattern() {
        final List<BGPattern> patternList = ((DoaPilihanApp) DoaPilihanApp.getContext()).getBgPatterns();
        final int patternIndex = ((DoaPilihanApp) DoaPilihanApp.getContext()).getPatternIndex();
        final ArrayAdapter<BGPattern> adapter = new BGPatternListArrayAdapter(this, patternList);

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle(R.string.dialog_pattern_list_label);
        dialogBuilder.setNegativeButton(R.string.action_cancel, null);
        dialogBuilder.setSingleChoiceItems(adapter, patternIndex, (dialog, which) -> {

            ((DoaPilihanApp) DoaPilihanApp.getContext()).saveBgPattern(which);
            BGPattern bgPattern = patternList.get(which);
            Analytic.sendEvent(Analytic.EVENT_BUTTON, getString(R.string.dialog_pattern_list_label), bgPattern.getName());

            // Cache the pattern
            BitmapCacher.cacheBitmap(bgPattern.getDrawable(), bgPattern.getName());

            if (mRxBus.hasObservers()) {
                mRxBus.send(new GeneralEvent.SuccessSaveBGPattern(patternList.get(which)));
            }

            dialog.dismiss();
        });
        dialogBuilder.create().show();

    }

}
