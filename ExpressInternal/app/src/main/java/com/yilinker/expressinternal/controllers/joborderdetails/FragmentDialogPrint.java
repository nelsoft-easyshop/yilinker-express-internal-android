package com.yilinker.expressinternal.controllers.joborderdetails;

import android.app.Activity;
import android.app.DialogFragment;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.yilinker.expressinternal.R;
import com.yilinker.expressinternal.business.ApplicationClass;
import com.yilinker.expressinternal.model.JobOrder;
import com.yilinker.expressinternal.utilities.QRCodeHelper;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

/**
 * Created by J.Bautista
 */
public class FragmentDialogPrint extends DialogFragment implements View.OnClickListener{

    private static final int REQUEST_CONNECT_BT = 0x2300;
    private static final int REQUEST_ENABLE_BT = 0x1000;

    private static byte[] FEED_LINE = {10};
    private static byte[] DEFAULT_LINE_SPACING = {0x1B, 0x33};
    private static byte[] LINE_SPACING = {0x1B, 0x33, 0x30};

    private static final UUID SPP_UUID = UUID
            .fromString("8ce255c0-200a-11e0-ac64-0800200c9a66");

    private static final String PRINTER_NAME = "MSP-100A";

    private static final String ARG_REQUEST_CODE = "requestCode";
    private static final String ARG_JOB_ORDER = "jobOrder";


    private Button btnOk;
    private Button btnCancel;
    private TextView tvStatus;

    private RequestQueue requestQueue;

    private JobOrder jobOrder;
    private int requestCode;

    private BluetoothAdapter mBluetoothAdapter = null;
    private BluetoothSocket mbtSocket = null;
    private OutputStream btoutputstream;

    private byte FONT_TYPE;

    public static FragmentDialogPrint createInstance(int requestCode, JobOrder jobOrder){

        FragmentDialogPrint fragment = new FragmentDialogPrint();

        Bundle args = new Bundle();
        args.putInt(ARG_REQUEST_CODE, requestCode);
        args.putParcelable(ARG_JOB_ORDER, jobOrder);

        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestQueue = ApplicationClass.getInstance().getRequestQueue();

        Bundle args = getArguments();

        jobOrder = args.getParcelable(ARG_JOB_ORDER);
        requestCode = args.getInt(ARG_REQUEST_CODE);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_dialog_print, container, false);

        Window window =  getDialog().getWindow();
        window.requestFeature(Window.FEATURE_NO_TITLE);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);

        //Start connecting to bluetooth
        initDevicesList();
    }

    private void initViews(View root){

        tvStatus = (TextView) root.findViewById(R.id.tvStatus);
        btnOk = (Button) root.findViewById(R.id.btnOk);
        btnCancel = (Button) root.findViewById(R.id.btnCancel);

        btnOk.setOnClickListener(this);
        btnCancel.setOnClickListener(this);

        btnOk.setVisibility(View.GONE);
        btnCancel.setVisibility(View.GONE);

    }

    @Override
    public void onClick(View v) {

        int id = v.getId();

        switch (id){

            case R.id.btnCancel:

                dismiss();
                break;

            case R.id.btnOk:

                String text = ((Button)v).getText().toString();

                if(text.equalsIgnoreCase(getString(R.string.printing_ok))){

                    dismiss();

                }
                else{

                    initDevicesList();

                    btnCancel.setVisibility(View.GONE);
                    btnOk.setVisibility(View.GONE);
                }

                break;

        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            if(mbtSocket!= null){
                btoutputstream.close();
                mbtSocket.close();
                mbtSocket = null;
            }
        } catch (IOException e) {

        }
    }

    private void flushData() {
        try {
            if (mbtSocket != null) {
                mbtSocket.close();
                mbtSocket = null;
            }

            if (mBluetoothAdapter != null) {
                mBluetoothAdapter.cancelDiscovery();
            }

            finalize();

        } catch (Exception ex) {
        } catch (Throwable e) {
        }

    }


    private int initDevicesList() {

        tvStatus.setText(getString(R.string.printing_status_connecting));

        flushData();

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {

            Toast.makeText(getActivity(),
                    "Bluetooth not supported!!", Toast.LENGTH_LONG).show();

            dismiss();
            return -1;
        }

        if (mBluetoothAdapter.isDiscovering()) {
            mBluetoothAdapter.cancelDiscovery();
        }


        Intent enableBtIntent = new Intent(
                BluetoothAdapter.ACTION_REQUEST_ENABLE);
        try {
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        } catch (Exception ex) {
            return -2;
        }


        return 0;

    }

    @Override
    public void onActivityResult(int reqCode, int resultCode, Intent intent) {
        super.onActivityResult(reqCode, resultCode, intent);

        switch (reqCode) {
            case REQUEST_ENABLE_BT:

                if (resultCode == Activity.RESULT_OK) {

                    Set<BluetoothDevice> btDeviceList = mBluetoothAdapter
                            .getBondedDevices();
                    try {

                        if (btDeviceList.size() > 0) {

                            boolean hasFound = false;
                            for (final BluetoothDevice device : btDeviceList) {


                                if(device.getName().equals(PRINTER_NAME)){


                                    Runnable runnable = new Runnable() {
                                        @Override
                                        public void run() {

                                            connectToPrinter(device);
                                        }
                                    };

                                    Handler handler = new Handler();

                                    handler.postDelayed(runnable, 20000);

                                    hasFound = true;

                                }

                            }

                            if(!hasFound){

                                setStatusToFailed();
                            }
                        }
                        else{

                            setStatusToFailed();
                        }

                    } catch (Exception ex) {

                        setStatusToFailed();
                    }
                } else {

                    setStatusToCancelled();
                }

                break;
        }

        mBluetoothAdapter.startDiscovery();

    }

    private Runnable socketErrorRunnable = new Runnable() {

        @Override
        public void run() {

            setStatusToFailed();
        }
    };

    private Runnable startPrintingRunnable = new Runnable() {

        @Override
        public void run() {

            tvStatus.setText(getString(R.string.printing_status_ongoingprint));


            synchronized(this)
            {
                this.notify();
            }
        }
    };


    private void connectToPrinter(final BluetoothDevice device){

        if (mBluetoothAdapter == null) {
            return;
        }

        if (mBluetoothAdapter.isDiscovering()) {
            mBluetoothAdapter.cancelDiscovery();
        }


        final Runnable printRunnable = new Runnable() {


            @Override
            public void run() {


                print_bt();

                synchronized(this)
                {
                    this.notify();
                    setStatusToComplete();
                }

            }

        };


        Thread connectThread = new Thread(new Runnable() {

            boolean isSuccessful = false;
            @Override
            public void run() {
                try {
                    boolean gotuuid = device
                            .fetchUuidsWithSdp();

                    UUID uuid = device.getUuids()[0]
                            .getUuid();
//                    mbtSocket = device
//                            .createRfcommSocketToServiceRecord(uuid);

                    mbtSocket = device
                            .createInsecureRfcommSocketToServiceRecord(uuid);


                    mbtSocket.connect();

                    synchronized (startPrintingRunnable){

                        getActivity().runOnUiThread(startPrintingRunnable);
                        startPrintingRunnable.wait();

                    }

                    synchronized (printRunnable) {


                        getActivity().runOnUiThread(printRunnable);
                        printRunnable.wait();

                    }

                    isSuccessful = true;

                } catch (Exception ex) {

                    getActivity().runOnUiThread(socketErrorRunnable);
                    try {
                        mbtSocket.close();
                    } catch (IOException e) {
                        // e.printStackTrace();
                    }
                    mbtSocket = null;



                }
            }
        });

        connectThread.start();

    }



    private void print_bt() {


        try {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            btoutputstream = mbtSocket.getOutputStream();


            for(int i = 0; i < 2; i++) {

                btoutputstream.write(DEFAULT_LINE_SPACING);

                printQRCode();

                btoutputstream.write(FEED_LINE);

                printBarcode();

                btoutputstream.write(FEED_LINE);

                printContentText(i);


                LINE_SPACING[2] = 100;
                btoutputstream.write(LINE_SPACING);

                btoutputstream.write(FEED_LINE);

                String line = "-------------------------------";
                btoutputstream.write(line.getBytes());

                btoutputstream.write(FEED_LINE);

            }

//            LINE_SPACING[2] = 100;
//            btoutputstream.write(LINE_SPACING);
//
            btoutputstream.write(FEED_LINE);

            btoutputstream.flush();

        } catch (IOException e) {

             setStatusToFailed();
        }

    }

    private void printContentText(int lineNo) throws IOException {

        LINE_SPACING[2] = 40;
        btoutputstream.write(LINE_SPACING);

        btoutputstream.write(FEED_LINE);

        byte[] leftAlign = {0x1B, 0x61, 0x00};
        btoutputstream.write(leftAlign);

        String msg = jobOrder.getWaybillNo();
        btoutputstream.write(msg.getBytes());

        btoutputstream.write(FEED_LINE);

        if(lineNo == 0) {

            msg = String.format("Package Description:\n%s", jobOrder.getPackageDescription());
            btoutputstream.write(msg.getBytes());

            btoutputstream.write(FEED_LINE);

            msg = String.format("Amount to Collect: P%.2f", jobOrder.getAmountToCollect());
            btoutputstream.write(msg.getBytes());

            LINE_SPACING[2] = 100;
            btoutputstream.write(LINE_SPACING);
            btoutputstream.write(FEED_LINE);


            btoutputstream.write(DEFAULT_LINE_SPACING);
            btoutputstream.write(FEED_LINE);

        }


        byte[] centerAlign = {0x1B, 0x61, 0x01};
        btoutputstream.write(centerAlign);

        if(lineNo == 0) {
            msg = getString(R.string.printing_certify);
            btoutputstream.write(msg.getBytes());

            LINE_SPACING[2] = 100;
            btoutputstream.write(LINE_SPACING);
            btoutputstream.write(FEED_LINE);


            msg = ((ApplicationClass)ApplicationClass.getInstance()).getRider().getName();
            btoutputstream.write(msg.getBytes());


            LINE_SPACING[2] = 70;
            btoutputstream.write(LINE_SPACING);
            btoutputstream.write(FEED_LINE);


        }else{

            LINE_SPACING[2] = 100;
            btoutputstream.write(LINE_SPACING);
            btoutputstream.write(FEED_LINE);


            btoutputstream.write(DEFAULT_LINE_SPACING);
            btoutputstream.write(FEED_LINE);

//            msg = jobOrder.getRecipient();
//            btoutputstream.write(msg.getBytes());
        }



        msg = "_________________________";
        btoutputstream.write(msg.getBytes());


        LINE_SPACING[2] = 30;
        btoutputstream.write(LINE_SPACING);
        btoutputstream.write(FEED_LINE);

        btoutputstream.write(DEFAULT_LINE_SPACING);
        btoutputstream.write(FEED_LINE);


        if(lineNo == 0) {
            msg = "Rider's Signature";
            btoutputstream.write(msg.getBytes());
        }
        else{

            {
                msg = "Shipper's Signature";
                btoutputstream.write(msg.getBytes());
            }
        }

        btoutputstream.write(0x0D);
        btoutputstream.write(0x0D);
        btoutputstream.write(0x0D);


//        LINE_SPACING[2] = 100;
//        btoutputstream.write(LINE_SPACING);
//        btoutputstream.write(FEED_LINE);
//
//        btoutputstream.write(DEFAULT_LINE_SPACING);

    }

    private void printQRCode() throws IOException {


        byte[] centerAlign = {0x1B, 0x61, 0x01};
        byte[] data = new byte[]{0x1B, 0x33, 0x00};
//
//
//        btoutputstream.write(data);

        btoutputstream.write(FEED_LINE);

        btoutputstream.write(centerAlign);

        int pixelColor = 0;

        Bitmap bmp = QRCodeHelper.generateQRCode(getActivity().getApplicationContext(), jobOrder.getWaybillNo(), R.dimen.qr_code_print);


        byte[] escBmp = new byte[]{0x1B, 0x2A, 0x00, 0x00, 0x00};

        escBmp[2] = 0x21;

        //nL, nH
        escBmp[3] = (byte) (bmp.getWidth() % 256);
        escBmp[4] = (byte) (bmp.getWidth() / 256);

        for (int i = 0; i < (bmp.getHeight() / 24) + 1; i++) {
            btoutputstream.write(escBmp);

            for (int j = 0; j < bmp.getWidth(); j++) {

                for (int k = 0; k < 24; k++) {
                    if (((i * 24) + k) < bmp.getHeight())   // if within the BMP size
                    {
                        pixelColor = bmp.getPixel(j, (i * 24) + k);
                        if (pixelColor != 0) {
                            data[k / 8] += (byte) (128 >> (k % 8));
                        }
                    }
                }

                btoutputstream.write(data);
                data[0] = 0x00;
                data[1] = 0x00;
                data[2] = 0x00;    // Clear to Zero.
            }

            btoutputstream.write(FEED_LINE);
        }
    }

    private void printBarcode() throws IOException {


        byte[] centerAlign = {0x1B, 0x61, 0x01};
        byte[] data = new byte[]{0x1B, 0x33, 0x00};


//        btoutputstream.write(data);

        btoutputstream.write(FEED_LINE);

        btoutputstream.write(centerAlign);

        int pixelColor = 0;

        Bitmap bmp = QRCodeHelper.generateBarode(getActivity().getApplicationContext(), jobOrder.getWaybillNo(), R.dimen.barcode_print_height, R.dimen.barcode_print_width);


        byte[] escBmp = new byte[]{0x1B, 0x2A, 0x00, 0x00, 0x00};

        escBmp[2] = 0x21;

        //nL, nH
        escBmp[3] = (byte) (bmp.getWidth() % 256);
        escBmp[4] = (byte) (bmp.getWidth() / 256);

        for (int i = 0; i < (bmp.getHeight() / 24) + 1; i++) {
            btoutputstream.write(escBmp);

            for (int j = 0; j < bmp.getWidth(); j++) {

                for (int k = 0; k < 24; k++) {
                    if (((i * 24) + k) < bmp.getHeight())   // if within the BMP size
                    {
                        pixelColor = bmp.getPixel(j, (i * 24) + k);
                        if (pixelColor != 0) {
                            data[k / 8] += (byte) (128 >> (k % 8));
                        }
                    }
                }

                btoutputstream.write(data);
                data[0] = 0x00;
                data[1] = 0x00;
                data[2] = 0x00;    // Clear to Zero.
            }

            btoutputstream.write(FEED_LINE);

        }
    }

    private void setStatusToComplete(){

        tvStatus.setText(getString(R.string.printing_status_done));
        btnOk.setVisibility(View.VISIBLE);
        btnOk.setText(R.string.printing_ok);

    }

    private void setStatusToFailed(){

        tvStatus.setText(getString(R.string.printing_status_problem));

        btnOk.setText(getString(R.string.printing_print_again));

        btnCancel.setVisibility(View.VISIBLE);
        btnOk.setVisibility(View.VISIBLE);

    }

    private void setStatusToCancelled(){

        tvStatus.setText(getString(R.string.printing_bluetooth_enable_cancelled));

        btnOk.setText(getString(R.string.printing_print_again));

        btnCancel.setVisibility(View.VISIBLE);
        btnOk.setVisibility(View.VISIBLE);

    }

}
