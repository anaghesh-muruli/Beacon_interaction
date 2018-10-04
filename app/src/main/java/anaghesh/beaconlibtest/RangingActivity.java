package anaghesh.beaconlibtest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.Identifier;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.util.ArrayList;
import java.util.Collection;

public class RangingActivity extends Activity implements BeaconConsumer, RangeNotifier {
    protected static final String TAG = "RangingActivity";
    Region region;
    Button button;
    private BeaconManager mBeaconManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.e("started","RangingActivity");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranging);
        button = findViewById(R.id.button);
        mBeaconManager = BeaconManager.getInstanceForApplication(this);
        // In this example, we will use Eddystone protocol, so we have to define it here
        mBeaconManager.getBeaconParsers().add(new BeaconParser().
                setBeaconLayout(BeaconParser.EDDYSTONE_UID_LAYOUT));
        // Binds this activity to the BeaconService
        mBeaconManager.bind(this);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RangingActivity.this, MainActivity.class));
            }
        });

    }
    @Override
    public void onBeaconServiceConnect() {
        Log.e("Inside","DidRange");
        // Encapsulates a beacon identifier of arbitrary byte length
        ArrayList<Identifier> identifiers = new ArrayList<>();
        region = new Region("AllBeaconsRegion", identifiers);
        // Set null to indicate that we want to match beacons with any value
        identifiers.add(null);
        // Represents a criteria of fields used to match beacon

        try {
            // Tells the BeaconService to start looking for beacons that match the passed Region object
            mBeaconManager.startRangingBeaconsInRegion(region);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        // Specifies a class that should be called each time the BeaconService gets ranging data, once per second by default
        mBeaconManager.addRangeNotifier(this);
    }
    @Override
    public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
        if (beacons.size() > 0) {
            Log.e("Inside","DidRange");
            Log.e("Macid", "Bluetooth MacId "+beacons.iterator().next().getBluetoothAddress());
            Log.e("NAme", "Bluetooth Name "+beacons.iterator().next().getBluetoothName()+" ");
            Log.e("Dist", "Dist "+beacons.iterator().next().getDistance()+" ");
            Log.e("Type", "Type "+beacons.iterator().next().getBeaconTypeCode()+" ");
            Log.e("Rssi", "Rssi "+beacons.iterator().next().getRssi()+" ");
            Log.e("Tx", "Tx "+beacons.iterator().next().getTxPower()+" ");
            Log.e("uuid", "Tx "+beacons.iterator().next().getServiceUuid()+" ");
        }
    }


}