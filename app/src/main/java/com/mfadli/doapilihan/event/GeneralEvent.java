package com.mfadli.doapilihan.event;

import com.mfadli.doapilihan.model.BGPattern;
import com.mfadli.doapilihan.model.Font;

/**
 * Created by mfad on 30/03/2016.
 * Container class for all General Events.
 * This will be used with {@link RxBus} e.g.:
 * <p>
 * if (mRxBus.hasObservers()) mRxBus.send(new ProfileEvents.SuccessSaveFontSize(11.0f));
 */
public class GeneralEvent {

    /**
     * When Font Size is selected and successfully saved to SharedPreference.
     */
    public static class SuccessSaveFontSize {
        float mOriginalSize;

        public SuccessSaveFontSize(float originalSize) {
            mOriginalSize = originalSize;
        }

        public float getOriginalSize() {
            return mOriginalSize;
        }
    }

    /**
     * When Line Spacing Size is selected and successfully saved to SharedPreference.
     */
    public static class SuccessSaveLineSpacingSize {
        float mOriginalSize;

        public SuccessSaveLineSpacingSize(float originalSize) {
            mOriginalSize = originalSize;
        }

        public float getOriginalSize() {
            return mOriginalSize;
        }
    }

    /**
     * When Translation is clicked and successfully saved to SharedPreference.
     */
    public static class SuccessSaveTranslation {
        boolean mIsEnglish;

        public SuccessSaveTranslation(boolean isEnglish) {
            mIsEnglish = isEnglish;
        }

        public boolean isEnglish() {
            return mIsEnglish;
        }
    }

    /**
     * Success save Font Type selection to SharedPreference.
     */
    public static class SuccessSaveFontType {
        Font mFont;

        public SuccessSaveFontType(Font font) {
            mFont = font;
        }

        public Font getFont() {
            return mFont;
        }
    }

    /**
     * Success Purchased Premium Item
     */
    public static class SuccessPurchased {
        boolean mSuccess;
        boolean mPremium;

        public SuccessPurchased(boolean success, boolean isPremium) {
            mSuccess = success;
            mPremium = isPremium;
        }

        public boolean isSuccess() {
            return mSuccess;
        }

        public boolean isPremium() {
            return mPremium;
        }
    }

    /**
     * IAB setup is success event
     */
    public static class SuccessIabSetup {
        boolean mPremium;

        public SuccessIabSetup(boolean premium) {
            mPremium = premium;
        }

        public boolean isPremium() {
            return mPremium;
        }
    }

    /**
     * Success saving BGPattern to SharedPreferences.
     */
    public static class SuccessSaveBGPattern {
        BGPattern mBgPattern;

        public SuccessSaveBGPattern(BGPattern bgPattern) {
            mBgPattern = bgPattern;
        }

        public BGPattern getBgPattern() {
            return mBgPattern;
        }
    }
}
