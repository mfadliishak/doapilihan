package com.mfadli.doapilihan.activities;

import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Menu;
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
    public boolean onPrepareOptionsMenu(Menu menu) {
        boolean isDarkTheme = ((DoaPilihanApp) DoaPilihanApp.getContext()).isDarkTheme();

        MenuItem menuItem = menu.findItem(R.id.action_change_mode);

        if (menuItem != null) {
            // change Mode Label according to theme.
            menuItem.setTitle(isDarkTheme ? R.string.menu_label_light_mode : R.string.menu_label_dark_mode);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.action_change_background:
                showPattern();
                break;
            default:
                break;
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
