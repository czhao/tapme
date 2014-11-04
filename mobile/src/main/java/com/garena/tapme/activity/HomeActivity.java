package com.garena.tapme.activity;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import com.garena.tapme.R;
import com.garena.tapme.helper.AppLogger;
import com.garena.tapme.helper.DeviceBridge;
import com.garena.tapme.helper.UIHelper;
import com.garena.tapme.service.ActivityRecognitionIntentService;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.ActivityRecognitionClient;
import org.androidannotations.annotations.*;

@EActivity(R.layout.activity_home)
public class HomeActivity extends Activity{

    @ViewById(R.id.wear_app_connect_status)
    TextView statusView;

    @ViewById(R.id.spinner_adjust_timeout)
    Spinner timeoutAdjustSpinner;

    @AfterViews
    void initUI(){
        timeoutAdjustSpinner.setAdapter(ArrayAdapter.createFromResource(this,R.array.time_interval_array,android.R.layout.simple_spinner_item));
    }

    @Override
    protected void onResume() {
        super.onResume();
        //check the connection status
        statusView.setText(DeviceBridge.getInstance().isConnected() ? R.string.connection_status_connected: R.string.connection_status_disconnected);
        if (!DeviceBridge.getInstance().isConnected()){
            //attempt to connect the wear device
            DeviceBridge.getInstance().connectToWearDevice();
        }

    }

    @ItemSelect(R.id.spinner_adjust_timeout)
    public void timeoutItemSelected(boolean selected, int position) {
        UIHelper.showShortToast("new item "+position);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        return id == R.id.action_settings || super.onOptionsItemSelected(item);
    }
}
