package com.garena.tapme.activity;

import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import com.garena.tapme.R;
import com.garena.tapme.application.Const;
import com.garena.tapme.helper.AppLogger;
import com.garena.tapme.helper.DeviceBridge;
import com.garena.tapme.storage.AppSettings;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_home)
public class HomeActivity extends Activity implements DeviceBridge.ConnectionCallback{

    @ViewById(R.id.wear_app_connect_status)
    TextView statusView;

    @ViewById(R.id.spinner_adjust_timeout)
    Spinner timeoutAdjustSpinner;

    @AfterViews
    void initUI(){
        timeoutAdjustSpinner.setAdapter(ArrayAdapter.createFromResource(this,
                R.array.time_interval_array,android.R.layout.simple_spinner_item));

        //set the default value
        long currentInterval = AppSettings.getReminderInterval();
        int position = (int)(currentInterval / (10 * 60 * Const.MILL_TO_SECONDS));
        timeoutAdjustSpinner.setSelection(position-1);
        timeoutAdjustSpinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                AppLogger.i("item selected %d",position);
                PutDataMapRequest dataMap = PutDataMapRequest.create("/time_interval");
                //convert to million seconds
                long timeInterval = (position+1) * 10 * 60 * Const.MILL_TO_SECONDS;
                //long timeInterval = 20 * Const.MILL_TO_SECONDS;
                dataMap.getDataMap().putLong(Const.SHARE_KEY.SHARED_KEY_TIME_INTERVAL, timeInterval);
                PutDataRequest request = dataMap.asPutDataRequest();
                DeviceBridge.getInstance().sendData(request);
                //save the data locally
                AppSettings.updateReminderInterval(timeInterval);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        //check the connection status
        statusView.setText(DeviceBridge.getInstance().isConnected() ? R.string.connection_status_connected: R.string.connection_status_disconnected);
        if (!DeviceBridge.getInstance().isConnected()){
            //attempt to connect the wear device
            DeviceBridge.getInstance().connectToWearDevice(this);
        }
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

    @Override
    public void onConnectionStatusChange() {
        if (statusView != null) {
            statusView.setText(DeviceBridge.getInstance().isConnected() ? R.string.connection_status_connected : R.string.connection_status_disconnected);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //dismiss the callback
        DeviceBridge.getInstance().dismissCallback();
    }
}
