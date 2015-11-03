package baasj005.ivmd.devicetracker.Activities;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.SQLException;
import java.util.ArrayList;

import baasj005.ivmd.devicetracker.Activities.DeviceListActivity;
import baasj005.ivmd.devicetracker.Adapter.DeviceListAdapter;
import baasj005.ivmd.devicetracker.Models.Device;
import baasj005.ivmd.devicetracker.R;
import baasj005.ivmd.devicetracker.SQLite.DataSource;
import baasj005.ivmd.devicetracker.Utility.DeleteDialog;

public class DeviceDetailsActivity extends AppCompatActivity implements DeleteDialog.DeleteDialogListener {

    private Device device;
    private TextView name;
    private TextView type;
    private DataSource dataSource;

    public DeviceDetailsActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_details);
        initializeViews();
        dataSource = new DataSource(this);
        device = (Device) getIntent().getSerializableExtra("selectedDevice");
        setDevice(device);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_device_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_modify_device) {
            Intent intent = new Intent(DeviceDetailsActivity.this, ModifyDeviceActivity.class);
            intent.putExtra("currentDevice", device);
            startActivity(intent);
        }else if(id == R.id.action_delete_device){
            DialogFragment dialog = new DeleteDialog();
            dialog.show(this.getFragmentManager(), "DeleteDialog");
        }

        return super.onOptionsItemSelected(item);
    }

    private void initializeViews(){
        name = (TextView) findViewById(R.id.details_value_name);
        type = (TextView) findViewById(R.id.details_value_type);
    }

    private void setDevice(Device device){
        name.setText(device.getName());
        type.setText(device.getType());
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        try {
            dataSource.deleteDevice(device);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        showDeviceDeletedToast();
        Intent intent = new Intent(DeviceDetailsActivity.this, DeviceListActivity.class);
        startActivity(intent);
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {

    }

    private void showDeviceDeletedToast(){
        Context context = getApplicationContext();
        String text = getString(R.string.device_deleted);
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }
}
