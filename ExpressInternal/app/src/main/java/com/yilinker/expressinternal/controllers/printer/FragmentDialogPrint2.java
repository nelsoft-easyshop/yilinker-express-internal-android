package com.yilinker.expressinternal.controllers.printer;

import android.app.DialogFragment;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.yilinker.expressinternal.R;
import com.yilinker.expressinternal.business.ApplicationClass;
import com.yilinker.expressinternal.model.BluetoothPrinter;
import com.yilinker.expressinternal.model.JobOrder;
import com.yilinker.expressinternal.utilities.QRCodeHelper;

import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

/**
 * Created by J.Bautista
 */
public class FragmentDialogPrint2 extends DialogFragment implements View.OnClickListener{

    private static final int REQUEST_CONNECT_BT = 0x2300;
    private static final int REQUEST_ENABLE_BT = 0x1000;

    private static byte[] FEED_LINE = {10};
    private static byte[] DEFAULT_LINE_SPACING1 = {0x1B, 0x33};
    private static byte[] DEFAULT_LINE_SPACING2 = {0x1B, 0x33, 0x01};

    private static byte[] LINE_SPACING = {0x1B, 0x33, 0x30};

    private static final UUID SPP_UUID = UUID
            .fromString("8ce255c0-200a-11e0-ac64-0800200c9a66");

//    private static final String PRINTER_ADDRESS = "98:D3:31:80:5C:03";
//    private static final String PRINTER_ADDRESS = "00:02:0A:03:FF:99";   //new printer

    private static final String ARG_REQUEST_CODE = "requestCode";
    private static final String ARG_JOB_ORDER = "jobOrder";
    private static final String ARG_PRINTER = "printer";

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

    //For new printing process
    private Thread splashTread;
    private Handler errorHandler;
    private Handler successHandler;

    private byte[] defaultLineSpacing;
    private String printerAddress;


    public static FragmentDialogPrint2 createInstance(int requestCode, JobOrder jobOrder, BluetoothPrinter printer){

        FragmentDialogPrint2 fragment = new FragmentDialogPrint2();

        Bundle args = new Bundle();
        args.putInt(ARG_REQUEST_CODE, requestCode);
        args.putParcelable(ARG_JOB_ORDER, jobOrder);
        args.putParcelable(ARG_PRINTER, printer);

        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestQueue = ApplicationClass.getInstance().getRequestQueue();

        initData();

        initPrinter();

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

        startPrintThread();

    }

    private void initViews(View root){

        tvStatus = (TextView) root.findViewById(R.id.tvStatus);
        btnOk = (Button) root.findViewById(R.id.btnOk);
        btnCancel = (Button) root.findViewById(R.id.btnCancel);

        btnOk.setOnClickListener(this);
        btnCancel.setOnClickListener(this);

        btnOk.setVisibility(View.GONE);
        btnCancel.setVisibility(View.GONE);

        tvStatus.setText(getString(R.string.printing_status_ongoingprint));

    }

    private void initData(){

        Bundle args = getArguments();

        jobOrder = args.getParcelable(ARG_JOB_ORDER);
        requestCode = args.getInt(ARG_REQUEST_CODE);

    }

    private void initPrinter(){

        //Initialize handlers
        errorHandler = new ErrorHandler();
        successHandler = new FinishPrintingHandler();

        //Set printer
        Bundle args = getArguments();
        BluetoothPrinter printer = args.getParcelable(ARG_PRINTER);
        printerAddress = printer.getAddress();

        //Set default line spacing
        if(printer.getName().equalsIgnoreCase("MTP-3-4")){

            defaultLineSpacing = DEFAULT_LINE_SPACING2;
        }
        else{
            defaultLineSpacing = DEFAULT_LINE_SPACING1;
        }

        enableBluetooth();
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

                    restartPrinter();

                }

                break;

        }

    }

    private void restartPrinter(){

        btnCancel.setVisibility(View.GONE);
        btnOk.setVisibility(View.GONE);

        tvStatus.setText(getString(R.string.printing_status_ongoingprint));

        startPrintThread();


    }

    private void print() throws IOException {


//            btoutputstream = mbtSocket.getOutputStream();

        btoutputstream.write(defaultLineSpacing);
        printLogo();

            for(int i = 0; i < 2; i++) {

                btoutputstream.write(defaultLineSpacing);


                printQRCode();

//                btoutputstream.write(FEED_LINE);

                printBarcode();

//                btoutputstream.write(FEED_LINE);

                printContentText(i);

//
//                LINE_SPACING[2] = 100;
//                btoutputstream.write(LINE_SPACING);

                btoutputstream.write(FEED_LINE);

                String line = "-------------------------------";
                btoutputstream.write(line.getBytes());

                btoutputstream.write(FEED_LINE);

            }

            LINE_SPACING[2] = 30;
            btoutputstream.write(LINE_SPACING);
            btoutputstream.write(FEED_LINE);

            btoutputstream.flush();

    }

    private void printContentText(int lineNo) throws IOException {

//        LINE_SPACING[2] = 40;
//        btoutputstream.write(LINE_SPACING);

        btoutputstream.write(FEED_LINE);

        byte[] leftAlign = {0x1B, 0x61, 0x00};
        btoutputstream.write(leftAlign);

        String msg = jobOrder.getWaybillNo();
        btoutputstream.write(msg.getBytes());

        btoutputstream.write(FEED_LINE);

        if(lineNo == 0) {

            msg = String.format("%s\n%s", getString(R.string.printing_package_description), jobOrder.getPackageDescription());
            btoutputstream.write(msg.getBytes());

            btoutputstream.write(FEED_LINE);

            msg = String.format("%s %s", getString(R.string.printing_package_amount_to_collect), jobOrder.getAmountToCollect());
            btoutputstream.write(msg.getBytes());

//            LINE_SPACING[2] = 100;
            LINE_SPACING[2] = 30;
            btoutputstream.write(LINE_SPACING);
            btoutputstream.write(FEED_LINE);


            btoutputstream.write(defaultLineSpacing);
//            btoutputstream.write(FEED_LINE);

        }


        byte[] centerAlign = {0x1B, 0x61, 0x01};
        btoutputstream.write(centerAlign);

        if(lineNo == 0) {
            msg = getString(R.string.printing_certify);
            btoutputstream.write(msg.getBytes());

//            LINE_SPACING[2] = 100;
            LINE_SPACING[2] = 50;
            btoutputstream.write(LINE_SPACING);
            btoutputstream.write(FEED_LINE);


            msg = "_________________________";
            btoutputstream.write(msg.getBytes());

            btoutputstream.write(defaultLineSpacing);
            btoutputstream.write(FEED_LINE);

            msg = ((ApplicationClass)ApplicationClass.getInstance()).getRider().getName();
            btoutputstream.write(msg.getBytes());


            btoutputstream.write(defaultLineSpacing);
            btoutputstream.write(FEED_LINE);


        }else{


            btoutputstream.write(defaultLineSpacing);
            btoutputstream.write(FEED_LINE);

//            msg = jobOrder.getRecipient();
//            btoutputstream.write(msg.getBytes());
        }


//
//        msg = "_________________________";
//        btoutputstream.write(msg.getBytes());
//
//
//        LINE_SPACING[2] = 30;
//        btoutputstream.write(LINE_SPACING);
//        btoutputstream.write(FEED_LINE);

        btoutputstream.write(defaultLineSpacing);
        btoutputstream.write(FEED_LINE);


        if(lineNo == 0) {
//            msg = "Rider's Signature";
//            btoutputstream.write(msg.getBytes());
        }
        else{

            {
//                LINE_SPACING[2] = 30;
//                btoutputstream.write(LINE_SPACING);
//                btoutputstream.write(FEED_LINE);

                msg = "_________________________";
                btoutputstream.write(msg.getBytes());

                btoutputstream.write(defaultLineSpacing);
                btoutputstream.write(FEED_LINE);

                msg = "Shipper's Signature";
                btoutputstream.write(msg.getBytes());
            }
        }

        btoutputstream.write(0x0D);
        btoutputstream.write(0x0D);
        btoutputstream.write(0x0D);


    }

    private void printLogo() throws IOException {


//        byte[] centerAlign = {0x1B, 0x61, 0x01};
        byte[] data = new byte[]{0x1B, 0x33, 0x00};


        byte[] leftAlign = {0x1B, 0x61, 0x00};
        btoutputstream.write(leftAlign);

        int pixelColor = 0;

        AssetManager assetManager = getActivity().getAssets();
        Bitmap bmp = BitmapFactory.decodeStream(assetManager.open("yilinker_logo.png"));


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

    private void printQRCode() throws IOException {


        byte[] centerAlign = {0x1B, 0x61, 0x01};
        byte[] data = new byte[]{0x1B, 0x33, 0x00};


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


    /////////////////////For Printing//////////////////////
    private class FinishPrintingHandler extends Handler {

        public FinishPrintingHandler() {

        }

        public void handleMessage(Message msg) {

//            Toast.makeText(getActivity(), "Printing Successful!", Toast.LENGTH_LONG).show();

            setStatusToComplete();
        }

    }


    private class ErrorHandler extends Handler {

        public ErrorHandler() {

        }

        public void handleMessage(Message msg) {

            setStatusToCancelled();
        }
    }

    private class PrintRunnable implements Runnable {

        private final String printerName;

        private final Handler handlerError;
        private final Handler handlerSuccess;


        public PrintRunnable(String str, Handler handlerError, Handler handlerSuccess) {

            this.printerName = str;
            this.handlerError = handlerError;
            this.handlerSuccess = handlerSuccess;
        }

        public void run() {

            try {

                Looper.prepare();
                String UUID_BPP = "00001101-0000-1000-8000-00805F9B34FB";
                BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                if (bluetoothAdapter != null && bluetoothAdapter.isEnabled()) {
                    BluetoothDevice printer = bluetoothAdapter.getRemoteDevice(this.printerName);
                    if (printer != null) {
                        BluetoothSocket socket = printer.createRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));
                        if (socket != null) {

                            socket.connect();
                            Thread.sleep(1500);

                            runPrintThread(socket, "");

                        } else {

                            handlerError.sendEmptyMessage(0);

                        }

                        Thread.sleep(500);
                        Looper.myLooper().quit();
                    }
                }
                else{

                    handlerError.sendEmptyMessage(0);

                }

            } catch (Exception e) {

                handlerError.sendEmptyMessage(0);

            }
        }

    }

    private class PrintThread extends Thread {

        private final BluetoothSocket bluetoothSocket;
        private final String printText;
        private final Handler errorHandler;
        private final Handler successHandler;

        public PrintThread(BluetoothSocket bluetoothSocket, String str, Handler errorHandler, Handler successHandler) {

            this.bluetoothSocket = bluetoothSocket;
            this.printText = str;
            this.errorHandler = errorHandler;
            this.successHandler = successHandler;
        }

        public void run() {

            try {

                synchronized (this) {
                    wait((long) 1500);
                }

                btoutputstream = this.bluetoothSocket.getOutputStream();

                print();

                PrintThread.sleep(10000);
                btoutputstream.flush();
                btoutputstream.close();
                if (this.bluetoothSocket != null) {

                    this.bluetoothSocket.close();
                }

                successHandler.sendEmptyMessage(0);


            } catch (InterruptedException e2) {

                errorHandler.sendEmptyMessage(0);

            } catch (Throwable th) {

                errorHandler.sendEmptyMessage(0);
            }
        }
    }


    private void startPrintThread() {
        new Thread(new PrintRunnable(printerAddress, errorHandler, successHandler)).start();
    }



    private void enableBluetooth() {

        if (this.mBluetoothAdapter != null && !this.mBluetoothAdapter.isEnabled()) {
            this.mBluetoothAdapter.enable();

        }
    }


    public void runPrintThread(BluetoothSocket socket, String textoImprimir) {
        this.splashTread = new PrintThread(socket, textoImprimir, errorHandler, successHandler);
        this.splashTread.start();
    }

}
