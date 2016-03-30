package com.mfadli.doapilihan.event;

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
}
