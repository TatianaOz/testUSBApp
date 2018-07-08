package com.micronet.test3usb.test3usb;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbConstants;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private TextView mStatusView, mResultView;
    private static final String TAG = "USB";
    private UsbManager mUsbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mStatusView = (TextView) findViewById(R.id.text_status);
        mResultView = (TextView) findViewById(R.id.text_result);
        mUsbManager = (UsbManager) getSystemService(Context.USB_SERVICE);

        IntentFilter filter = new IntentFilter();
        filter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED);
        filter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
        registerReceiver(mUsbReceiver, filter);
    }


    @Override
    public void onResume() {
        super.onResume();

        Intent intent = getIntent();
        String action = intent.getAction();
        mStatusView.setText("Device attached");
        printDeviceList();

    }

    private final BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (UsbManager.ACTION_USB_DEVICE_ATTACHED.equals(action)) {
                mStatusView.setText("Device attached");
                printDeviceList();
            } else {
                mResultView.setText("No devices currently connected");
                mStatusView.setText("Device attached");
            }
        }
    };

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mUsbReceiver);
    }

    private void printDeviceList() {
        HashMap<String, UsbDevice> connectedDevices = mUsbManager.getDeviceList();
        UsbDevice dev = getIntent().getParcelableExtra(UsbManager.EXTRA_DEVICE);
        if (connectedDevices.isEmpty()) {
            mResultView.setText("No Devices Currently Connected");
        } else {
            if (dev != null) {
                printDeviceDetails(dev);
            } else {
                StringBuilder builder = new StringBuilder();
                builder.append("Connected Device Count: ");
                builder.append(connectedDevices.size());
                builder.append("\n\n");
                for (UsbDevice device : connectedDevices.values()) {
                    //Use the last device detected (if multiple) to open
                    builder.append(printDeviceInfo(device));
                    builder.append("\n\n");
                }
                mResultView.setText(builder.toString());
            }
        }
    }

    private void printDeviceDetails(UsbDevice device) {
        mResultView.setText(printDeviceInfo(device));
    }

    public static String printDeviceInfo(UsbDevice device) {
        String s = "Device Info: " + "\n" +
                "ProductId: " + device.getProductId() + "\n" +
                "VendorId: " + device.getVendorId() + "\n" +
                "ManufacturerName: " + device.getManufacturerName() + "\n" +
                "ProductName: " + device.getProductName() + "\n" +
                "DeviceClass: " + device.getDeviceClass() + "\n" +
                "DeviceId: " + device.getDeviceId() + "\n" +
                "DeviceSubclass: " + device.getDeviceSubclass() + "\n" +
                "DeviceProtocol: " + device.getDeviceProtocol() + "\n" +
                "DeviceName: " + device.getDeviceName() + "\n";
        return s;
    }

}

