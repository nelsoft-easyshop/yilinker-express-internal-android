package com.yilinker.expressinternal.controllers.printer;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;

import com.yilinker.expressinternal.R;
import com.yilinker.expressinternal.interfaces.BroadcastReceiverListener;
import com.yilinker.expressinternal.interfaces.RecyclerViewClickListener;
import com.yilinker.expressinternal.model.BluetoothPrinter;
import com.yilinker.expressinternal.model.JobOrder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by J.Bautista
 */
public class FragmentDialogPrinterList extends DialogFragment implements BroadcastReceiverListener, RecyclerViewClickListener<BluetoothPrinter>, View.OnClickListener {

    private static final String ARG_REQUEST_CODE = "requestCode";
    private static final String ARG_JOB_ORDER = "jobOrder";

    private static final String ACTION_BLUETOOTH_FOUND = "android.bluetooth.device.action.FOUND";
    private static final String ACTION_BLUETOOTH_FINISHED = "android.bluetooth.adapter.action.DISCOVERY_FINISHED";

    private RecyclerView rvPrinters;
    private Button btnSearch;

    private BluetoothReceiver receiver;
    private BluetoothAdapter bluetoothAdapter;

    private PrinterListAdapter adapter;
    private List<BluetoothPrinter> list;

    public static FragmentDialogPrinterList createInstance(int requestCode, JobOrder jobOrder){

        FragmentDialogPrinterList fragment = new FragmentDialogPrinterList();

        Bundle args = new Bundle();
        args.putInt(ARG_REQUEST_CODE, requestCode);
        args.putParcelable(ARG_JOB_ORDER, jobOrder);

        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initAdapter();

        initReceiver();

        startBluetoothDiscovery();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_dialog_printerlist, container, false);

        Window window =  getDialog().getWindow();
        window.requestFeature(Window.FEATURE_NO_TITLE);

        return view;

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);
    }

    @Override
    public void onDataReceived(String action, Bundle data) {

        if(action.equalsIgnoreCase(ACTION_BLUETOOTH_FOUND)){

            resetList(data);
        }

    }

    @Override
    public void onClick(View v) {

        int id = v.getId();

        switch (id){

            case R.id.btnSearch:

                list.clear();
                adapter.notifyDataSetChanged();

                startBluetoothDiscovery();
                break;

        }

    }

    @Override
    public void onItemClick(int position, BluetoothPrinter object) {

        showPrintDialog(object);
    }

    private void initViews(View parent){

        rvPrinters = (RecyclerView) parent.findViewById(R.id.rvPrinterList);
        btnSearch = (Button) parent.findViewById(R.id.btnSearch);

        btnSearch.setOnClickListener(this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvPrinters.setLayoutManager(layoutManager);
        rvPrinters.setAdapter(adapter);
    }

    private void showPrintDialog(BluetoothPrinter printer){

        //Get request code and Job Order
        Bundle args = getArguments();
        int requestCode = args.getInt(ARG_REQUEST_CODE);
        JobOrder jobOrder = args.getParcelable(ARG_JOB_ORDER);

        FragmentDialogPrint2 dialog = FragmentDialogPrint2.createInstance(requestCode, jobOrder, printer);
        dialog.setCancelable(false);
        dialog.show(getFragmentManager(), null);

        dismiss();
    }

    private void initAdapter(){

        list = new ArrayList<>();
        adapter = new PrinterListAdapter(list, this);

    }

    private void initReceiver(){

        receiver = new BluetoothReceiver(this);

        Activity parentActivity = getActivity();

        parentActivity.registerReceiver(receiver, new IntentFilter(ACTION_BLUETOOTH_FINISHED));
        parentActivity.registerReceiver(receiver, new IntentFilter(ACTION_BLUETOOTH_FOUND));

    }

    private void startBluetoothDiscovery(){

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (bluetoothAdapter.isDiscovering()) {
            bluetoothAdapter.cancelDiscovery();
        }

        bluetoothAdapter.startDiscovery();

    }

    private void resetList(Bundle data){

        BluetoothPrinter printer = data.getParcelable(BluetoothReceiver.ARG_DEVICES);

        list.add(printer);

        adapter.notifyDataSetChanged();

    }


    private class BluetoothReceiver extends BroadcastReceiver{

        static final String ARG_DEVICES = "devices";

        private static final String EXTRA_BLUETOOTH_DEVICES = "android.bluetooth.device.extra.DEVICE";

        private BroadcastReceiverListener listener;

        public BluetoothReceiver(BroadcastReceiverListener listener) {

            this.listener = listener;
        }

        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();

            if (ACTION_BLUETOOTH_FOUND.equals(action)) {

                BluetoothDevice bluetoothDevice = (BluetoothDevice) intent.getParcelableExtra(EXTRA_BLUETOOTH_DEVICES);

                String name = bluetoothDevice.getName();
                String address = bluetoothDevice.getAddress();

                if (name == null) {
                    name = "";
                }

                BluetoothPrinter printer = new BluetoothPrinter();
                printer.setAddress(address);
                printer.setName(name);

                Bundle arg = new Bundle();
                arg.putParcelable(ARG_DEVICES, printer);

                listener.onDataReceived(action, arg);

            } else if (ACTION_BLUETOOTH_FINISHED.equals(action)) {

                listener.onDataReceived(action, null);
            }

        }

    }


}
