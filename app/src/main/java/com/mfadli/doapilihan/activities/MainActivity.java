package com.mfadli.doapilihan.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.mfadli.doapilihan.BuildConfig;
import com.mfadli.doapilihan.DoaPilihanApp;
import com.mfadli.doapilihan.R;
import com.mfadli.doapilihan.event.GeneralEvent;
import com.mfadli.doapilihan.fragments.AboutFragment;
import com.mfadli.doapilihan.fragments.AdsSettingFragment;
import com.mfadli.doapilihan.fragments.MainActivityFragment;
import com.mfadli.utils.Analytic;
import com.mfadli.utils.iap.IabHelper;
import com.mfadli.utils.iap.IabResult;
import com.mfadli.utils.iap.Inventory;
import com.mfadli.utils.iap.Purchase;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity implements MainActivityFragment.OnMainFragmentItemClickListener,
        AdsSettingFragment.OnAdsSettingFragmentListener {
    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private static final String STATE_SELECTED_DRAWER = "state_selected_drawer";
    private static final String SKU_PREMIUM = DoaPilihanApp.getContext().getString(R.string.skuPremium1);

    private static final int PURCHASE_REQUEST = 10001;
    public static final int IAB_PURCHASE_FAILED = 101;
    public static final int IAB_PURCHASE_FAILED_PAYLOAD_PROBLEM = 102;

    private int mCurrentSelectedPosition = 0;

    private Bundle mReenterState;
    private static IabHelper mHelper;
    private boolean mIsPremium = false;
    private boolean mDestroyed = false;

    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @Bind(R.id.main_nav_view)
    NavigationView mNavigationView;

    /**
     * Listener which will be called when finish query the premium item they have.
     */

    IabHelper.QueryInventoryFinishedListener mGotInventoryListener = new IabHelper.QueryInventoryFinishedListener() {
        public void onQueryInventoryFinished(IabResult result, Inventory inventory) {
            Log.d(LOG_TAG, "Query inventory finished: result: " + result + ", inventory: " + inventory.toString());

            if (mHelper == null) return;

            // Is it a failure?
            if (result.isFailure()) {
                complain("Failed to query inventory: " + result);
                onIabSetupFailed();
                return;
            }

            Log.d(LOG_TAG, "Query inventory is successful.");

            // callback when succeed
            onIabSetupSucceeded(result, inventory);

        }
    };

    /**
     * Listener called after consuming item purchased.
     */
    IabHelper.OnConsumeFinishedListener mConsumeFinishedListener = new IabHelper.OnConsumeFinishedListener() {

        @Override
        public void onConsumeFinished(Purchase purchase, IabResult result) {
            Log.d(LOG_TAG, "Consumption finished. Purchase: " + purchase + ", result: " + result);

            if (mHelper == null) return;

            if (result.isSuccess()) {

                // Use a synchronized block so there is only one update at a time.
                synchronized (mHelper) {
                    onIabConsumeItemSucceeded(purchase, result);
                }
            } else {
                complain("Did not handle purchase: " + purchase + " result: " + result);
                onIabConsumeItemFailed();
            }

            Log.d(LOG_TAG, "End IAB consumption flow.");
        }
    };

    /**
     * Listener which called when purchase is finished.
     */

    IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {

        @Override
        public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
            Log.d(LOG_TAG, "Purchase finished: " + result + ", purchase: " + purchase);

            if (mHelper == null) return;

            if (result.isFailure()) {
                complain("Sorry, purchase did not go through. " + result);
                onIabPurchaseFailed(IAB_PURCHASE_FAILED);
                return;
            }
            if (!verifyDeveloperPayload(purchase)) {
                complain("Sorry, purchase did not go through. Authenticity verification failed.");
                onIabPurchaseFailed(IAB_PURCHASE_FAILED_PAYLOAD_PROBLEM);
                return;
            }

            Log.d(LOG_TAG, "Purchase successful.");

            if (purchase.getSku().equals(SKU_PREMIUM)) {
                Log.d(LOG_TAG, "Premium Purchase completed...");
                onIabPremiumItemSucceeded(purchase, result);
            }
            // Commented code is for consumable item. Premium item shouldn't be consumed
            /*
            if (purchase.getSku().equals(SKU_CONSUMABLE)) {
                Log.d(LOG_TAG, "Consumable Purchase finished. Consuming it ...");

                try {
                    mHelper.consumeAsync(purchase, mConsumeFinishedListener);
                } catch (IabHelper.IabAsyncInProgressException e) {
                    complain("Consume process failed " + e.getMessage());

                }
            }
            */
            else {
                complain("Sorry, purchase did not go through, wrong item information received.");
                onIabConsumeItemFailed();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mDestroyed = false;

        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        setUpNavDrawer();

        if (savedInstanceState != null) {
            mCurrentSelectedPosition = savedInstanceState.getInt(STATE_SELECTED_DRAWER, 0);
            Menu menu = mNavigationView.getMenu();
            menu.getItem(mCurrentSelectedPosition).setChecked(true);
            TextView tvTitle = (TextView) mToolbar.findViewById(R.id.toolbar_title);
            tvTitle.setText(menu.getItem(mCurrentSelectedPosition).getTitle());
        } else {

            MainActivityFragment fragment = MainActivityFragment.newInstance();

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.main_content, fragment)
                    .commit();

            TextView tvTitle = (TextView) mToolbar.findViewById(R.id.toolbar_title);
            tvTitle.setText(R.string.drawer_title_home);
        }

        mNavigationView.setNavigationItemSelectedListener(menuItem -> {
            processSelectedItem(menuItem);
            return true;
        });

        setupIabHelper(true, true);

    }

    public static void start(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mHelper != null) {
            try {
                mHelper.dispose();
            } catch (IabHelper.IabAsyncInProgressException e) {
                e.printStackTrace();
            }
        }
        mHelper = null;
        mDestroyed = true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_SELECTED_DRAWER, mCurrentSelectedPosition);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            // IabHelper will call onActivityResult when finish purchased!!
            case PURCHASE_REQUEST:
                //
                // Handle the purchase request here.
                //
                Log.d(LOG_TAG, "onActivityResult(" + requestCode + "," + resultCode + "," + data);

                if (mHelper != null) {
                    if (!mHelper.handleActivityResult(requestCode, resultCode, data)) {
                        // Not handled by the helper, so let other code do it.
                        super.onActivityResult(requestCode, resultCode, data);
                    }
                }
                break;

            default:
                // Anything else should be handled by other handlers.
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }

        super.onActivityResult(requestCode, resultCode, data);

        mReenterState = new Bundle(data.getExtras());

        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.main_content);

        // refresh list layout if the screen is at main home screen
        if (fragment != null && fragment instanceof MainActivityFragment) {

            int originalPosition = mReenterState.getInt(DetailActivity.EXTRA_ORIGINAL_POSITION);
            int currentPosition = mReenterState.getInt(DetailActivity.EXTRA_CURRENT_POSITION);

            if (originalPosition != currentPosition) {
                ((MainActivityFragment) fragment).scrollToPosition(currentPosition);
            }

            ((MainActivityFragment) fragment).refresh();
        }
    }

    @Override
    public void onMainFragmentItemClick(int position, FrameLayout titleFrame) {
        DetailActivity.start(this, position, titleFrame);
    }

    @Override
    public void onClickUpgrade() {
        Log.d(LOG_TAG, "Upgrade button clicked, launching purchase flow for upgrade.");
        setWaitScreen(true);

        Analytic.sendEvent(Analytic.EVENT_IAP, "ClickUpgrade", "Begin");

        launchInAppPurchaseFlow();
    }

    /**
     * Show alert dialog box for IAB.
     *
     * @param message String
     */
    private void alert(String message) {
        if (mDestroyed) {
            Log.d(LOG_TAG, "NOTE: IabActivity.alert called after onDestroy. Message: " + message);
            return;
        }

        AlertDialog.Builder bld = new AlertDialog.Builder(this);
        bld.setMessage(message);
        bld.setNeutralButton("OK", null);
        Log.d(LOG_TAG, "Showing alert dialog: " + message);
        bld.create().show();
    }

    /**
     * Calling alert function, show log to console.
     * The function that is actually called by various codes instead of alert.
     *
     * @param message String
     */
    private void complain(String message) {
        Log.e(LOG_TAG, "**** In-App Billing Error: " + message);
        alert("Error: " + message);
    }

    /**
     * Setup IAB for making purchase.
     * Part of the setup is query for purchase and query product list.
     * If setup success, call onIabSetupSuceeded with inventory object.
     * If fail, onIabSetupFailed is called.
     * Set showErrors to true to prompt toast error message.
     *
     * @param showListedSkus boolean true to include the listed SKUs in the inventory.
     * @param showErrors     boolean true to show error in toast
     */
    private void setupIabHelper(final boolean showListedSkus, final boolean showErrors) {

        try {
            // Start IAP
            mHelper = new IabHelper(this, getString(R.string.license));
            mHelper.enableDebugLogging(true);
            mHelper.startSetup((IabResult result) -> {
                if (result.isFailure()) {
                    // Oh noes, there was a problem.
                    complain("Problem setting up in-app billing: " + result);
                    onIabSetupFailed();
                    return;
                }

                // Have we been disposed of in the meantime? If so, quit.
                if (mHelper == null) return;

                // Hooray, IAB is fully set up! Get the inventory if showListedSkus is true
                Log.d(LOG_TAG, "Setup successful. Querying inventory.");

                // Build up a list of the SKUs so we can get information on prices if we need to.
                ArrayList<String> skusToBeListed = null;
                if (showListedSkus) {
                    skusToBeListed = new ArrayList<String>();
                    skusToBeListed.add(SKU_PREMIUM);
                }

                try {
                    mHelper.queryInventoryAsync(false, skusToBeListed, mGotInventoryListener);
                } catch (IabHelper.IabAsyncInProgressException ex) {

                    // If we are debugging ...
                    complain("Exception while setting up in-app billing: " + ex.toString());
                    onIabSetupFailed();

                }

            });
        } catch (Throwable ex) {
            // If we are debugging ...
            complain("Exception while setting up in-app billing: " + ex.toString());
            onIabSetupFailed();
        }
    }

    /**
     * Called when setup fail and inventory items is not available.
     */
    void onIabSetupFailed() {
        setWaitScreen(false);
    }

    /**
     * Callback when setup of IAB is succeed and the inventory item is available.
     * Checking if the premium item is purchased.
     *
     * @param result    IabResult
     * @param inventory Inventory
     */
    private void onIabSetupSucceeded(IabResult result, Inventory inventory) {

        // Do player have premium item
        Purchase premiumPurchase = inventory.getPurchase(SKU_PREMIUM);
        mIsPremium = (premiumPurchase != null && verifyDeveloperPayload(premiumPurchase));

        Log.d(LOG_TAG, "User is " + (mIsPremium ? "PREMIUM" : "NOT PREMIUM"));

        if (mIsPremium) {
            DoaPilihanApp app = (DoaPilihanApp) DoaPilihanApp.getContext();
            if (!app.isPremium()) {
                app.savePremium(mIsPremium);
            }
        }

        // IAB success event broadcast
        if (mRxBus.hasObservers()) {
            mRxBus.send(new GeneralEvent.SuccessIabSetup(mIsPremium));
        }

        setWaitScreen(false);
    }

    /**
     * Callback when purchasing failed
     *
     * @param errorNum int
     */
    private void onIabPurchaseFailed(int errorNum) {
        if (errorNum != 0) setWaitScreen(false);

        Analytic.sendEvent(Analytic.EVENT_IAP, "ClickUpgrade", "Failed");
    }

    /**
     * Callback when consuming item purchased failed
     */
    private void onIabConsumeItemFailed() {
        setWaitScreen(false);
    }

    /**
     * Callback after purchased Premium Item.
     *
     * @param purchase Purchase
     * @param result   IabResult
     */
    private void onIabPremiumItemSucceeded(Purchase purchase, IabResult result) {

        String purchaseSku = purchase.getSku();

        // Double check it is premium item
        if (purchaseSku.equals(SKU_PREMIUM)) {
            if (result.isSuccess()) {
                Log.d(LOG_TAG, "Premium item Provisioning.");
                ((DoaPilihanApp) DoaPilihanApp.getContext()).savePremium(true);

                // Broadcast SuccessPurchase
                if (mRxBus.hasObservers()) {
                    mRxBus.send(new GeneralEvent.SuccessPurchased(true, true));
                }

                Analytic.sendEvent(Analytic.EVENT_IAP, "ClickUpgrade", "Success");

            } else {
                complain("Error while provisioning premium item: " + result);
            }
        }

        // Broadcast SuccessPurchase
        if (mRxBus.hasObservers()) {
            boolean isPremium = ((DoaPilihanApp) DoaPilihanApp.getContext()).isPremium();
            mRxBus.send(new GeneralEvent.SuccessPurchased(result.isSuccess(), isPremium));
        }
    }

    /**
     * Callback after purchased and consumed the item (Consumable item only)
     *
     * @param purchase Purchase
     * @param result   IabResult
     */
    private void onIabConsumeItemSucceeded(Purchase purchase, IabResult result) {
        /*
        String purchaseSku = purchase.getSku();

        if (purchaseSku.equals(SKU_CONSUMABLE)) {
            if (result.isSuccess()) {
                Log.d(LOG_TAG, "Consumption successful. Provisioning.");

            } else {
                complain("Error while consuming premium item: " + result);
            }
        }
        */
        setWaitScreen(false);
    }

    /**
     * Verifies the developer payload of a purchase. Refer to:
     * http://stackoverflow.com/questions/17196562/token-that-identify-the-user/17205999#17205999
     * and
     * http://stackoverflow.com/a/15641171
     *
     * @param purchase Purchase
     * @return boolean true means the purchase has been verified
     */
    boolean verifyDeveloperPayload(Purchase purchase) {
        String payload = "";

        if (purchase != null)
            payload = purchase.getDeveloperPayload();

    /*
     * TODO: verify that the developer payload of the purchase is correct. It will be
     * the same one that you sent when initiating the purchase.
     *
     * WARNING: Locally generating a random string when starting a purchase and
     * verifying it here might seem like a good approach, but this will fail in the
     * case where the user purchases an item on one device and then uses your app on
     * a different device, because on the other device you will not have access to the
     * random string you originally generated.
     *
     * So a good developer payload has these characteristics:
     *
     * 1. If two different users purchase an item, the payload is different between them,
     *    so that one user's purchase can't be replayed to another user.
     *
     * 2. The payload must be such that you can verify it even when the app wasn't the
     *    one who initiated the purchase flow (so that items purchased by the user on
     *    one device work on other devices owned by the user).
     *
     * Using your own server to store and verify developer payloads across app
     * installations is recommended.
     */

        return true;
    }

    /**
     * Look at verifyDeveloperPayload() for details.
     * This will start the purchase event
     */
    private void launchInAppPurchaseFlow() {
        String payload = "";

        if (mHelper == null) return;

        try {
            mHelper.launchPurchaseFlow(this, SKU_PREMIUM, PURCHASE_REQUEST, mPurchaseFinishedListener, payload);
        } catch (IabHelper.IabAsyncInProgressException ex) {
            complain("Exception while purchasing: " + ex.toString());
            setWaitScreen(false);
        }
    }

    /**
     * Show or Hide Waiting/Loading layer while IAB is progressing
     *
     * @param show boolean
     */
    private void setWaitScreen(boolean show) {
        if (show)
            Log.d(LOG_TAG, "Wait Screen.... open");
        else
            Log.d(LOG_TAG, "Wait Screen.... close");
    }

    /**
     * Configure Toolbar and Drawer Layout.
     * Add burger icon and click listener to open the drawer.
     * Add app version to header.
     */
    private void setUpNavDrawer() {
        if (mToolbar != null) {
            IconicsDrawable icon = new IconicsDrawable(this)
                    .icon(GoogleMaterial.Icon.gmd_menu)
                    .color(getResources().getColor(R.color.md_white_1000))
                    .sizeDp(24);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            mToolbar.setNavigationIcon(icon);
            mToolbar.setNavigationOnClickListener(v -> mDrawerLayout.openDrawer(GravityCompat.START));

            View headerLayout = mNavigationView.getHeaderView(0);
            TextView tvVersion = (TextView) headerLayout.findViewById(R.id.drawer_header_version);
            tvVersion.setText(BuildConfig.VERSION_NAME);
        }
    }

    /**
     * From menu item provided, will check for which item is being click,
     * ignore item which is already selected.
     * Replace fragment according to selected item and close the drawer.
     *
     * @param menuItem {@link MenuItem}
     */
    private void processSelectedItem(MenuItem menuItem) {
        Fragment fragment = null;
        Class fragmentClass;
        int position = 0;

        switch (menuItem.getItemId()) {
            case R.id.navigation_item_home:
                position = 0;
                fragmentClass = MainActivityFragment.class;
                break;
            case R.id.navigation_item_ads_setting:
                position = 1;
                fragmentClass = AdsSettingFragment.class;
                break;
            case R.id.navigation_item_about:
                position = 2;
                fragmentClass = AboutFragment.class;
                break;
            default:
                fragmentClass = MainActivityFragment.class;
        }

        if (mCurrentSelectedPosition == position) {
            return;
        } else {
            mCurrentSelectedPosition = position;
        }

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_content, fragment)
                .commit();

        menuItem.setChecked(true);
        mDrawerLayout.closeDrawer(GravityCompat.START);

        TextView tvTitle = (TextView) mToolbar.findViewById(R.id.toolbar_title);
        tvTitle.setText(menuItem.getTitle());
    }

}
