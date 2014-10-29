package com.garena.tapme.activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.wearable.view.CardFragment;
import com.garena.tapme.R;

/**
 * @author zhaocong
 * @since 29/10/14.
 */
public class ReminderWearActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reminder_card);

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        CardFragment cardFragment = CardFragment.create(getString(R.string.app_reminder_title),
                getString(R.string.hello_round),
                R.drawable.ic_launcher);
        fragmentTransaction.add(R.id.card_frame_layout, cardFragment);
        fragmentTransaction.commit();
    }
}
