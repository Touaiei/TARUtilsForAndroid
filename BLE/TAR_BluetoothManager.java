package com.huanwei.TAR_UtilsForAndroid.BLE;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Administrator on 2019/1/16.
 */
/**
 * BLE低功耗蓝牙操作类
 * */
public class TAR_BluetoothManager {
    private String TAG = "TAR_BluetoothManager";

    public Context context;
    static private TAR_BluetoothManager instance = null;
    public BluetoothAdapter bluetoothAdapter;

    public ArrayList<BluetoothDevice> bluetoothDeviceArrayList = new ArrayList<>();//搜搜到的设备列表
    public boolean mScanning;
    public long SCAN_PERIOD = 10000;//最大扫描时间，超过就停止扫描
    public BleDeviceListAdapter bleDeviceListAdapter;


    /**
     * 蓝牙设备
     */
    public BluetoothDevice mBluetoothDevice;
    /**
     * 连接蓝牙设备 重连等操作
     */
    public BluetoothGatt mBluetoothGatt;
    public List<BluetoothGatt> bluetoothGattList;
    public static final int PERMISSIONS_REQUEST_CODE = 101;

    /**
     * 数据的读写操作
     */
    public BluetoothGattCharacteristic characteristic;

    public void setScanCallback(TARScanCallback scanCallback) {
        this.tarScanCallback = scanCallback;
    }

    public interface TARScanCallback {
        public void scanningResults(List<BluetoothDevice> bluetoothDeviceArrayList);

        public void scanningStart();

        public void scanningStop();
    }

    private TARScanCallback tarScanCallback;//扫描到结果的监听器


    static public TAR_BluetoothManager getInstance(Context context) {
        if (instance == null) {
            instance = new TAR_BluetoothManager(context);
        }
        return instance;
    }

    private TAR_BluetoothManager(Context context) {
        this.context = context;
    }

    public BluetoothGattCharacteristic getCharacteristic() {
        return characteristic;
    }

    public void setCharacteristic(BluetoothGattCharacteristic characteristic) {
        this.characteristic = characteristic;
    }

    public ArrayList<BluetoothDevice> getBluetoothDeviceArrayList() {
        return bluetoothDeviceArrayList;
    }

    public BluetoothDevice getmBluetoothDevice() {
        return mBluetoothDevice;
    }

    public void setmBluetoothDevice(BluetoothDevice mBluetoothDevice) {
        this.mBluetoothDevice = mBluetoothDevice;
    }

    public boolean isSupportBluetooth_ble() {
        // 检查当前手机是否支持ble 蓝牙,如果不支持退出程序
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            return true;
        } else {
            Toast.makeText(context, "设备不支持蓝牙BLE", Toast.LENGTH_SHORT).show();
            return false;
        }
    }


    public void initBluetooth() {
        //初始化蓝牙适配器
        BluetoothManager bluetoothManager = (BluetoothManager) context.getSystemService(context.BLUETOOTH_SERVICE);
        if (bluetoothManager != null) {
            bluetoothAdapter = bluetoothManager.getAdapter();
            if (bluetoothAdapter != null) {
                if (!bluetoothAdapter.isEnabled()) {
                    openBluetooth(bluetoothAdapter);
                } else {
                    scanBluetoothDevice(true);
                }
            } else {
                openBluetooth(bluetoothAdapter);
            }
        }
        bleDeviceListAdapter = new BleDeviceListAdapter(bluetoothDeviceArrayList, context);
        bluetoothGattList = new ArrayList<>();
    }

    private void openBluetooth(BluetoothAdapter bluetoothAdapter) {
        //打开蓝牙权限
        //以下两种方式 第二种方式在onActivityResult处理回调
        //方式一
        boolean enable = bluetoothAdapter.enable();//打开蓝牙'直接打开，用户不知权，用于定制系统'
        if (enable) {
            Toast.makeText(context, "打开蓝牙成功", Toast.LENGTH_LONG).show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    scanBluetoothDevice(true);
                }
            }, 2000);
        } else {
            Toast.makeText(context, "打开蓝牙失败", Toast.LENGTH_LONG).show();
        }

        /*
        //方式二
        Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(intent,1);
        */
    }

    public void scanBluetoothDevice(boolean isEnable) {
        /*打开扫描或者停止扫描*/
        //扫描周围的蓝牙设备
        if (isEnable) {
            tarScanCallback.scanningStart();
            mScanning = true;
            // 定义一个回调接口供扫描结束处理
            bluetoothAdapter.startLeScan(mLeScanCallback);
            Toast.makeText(context, "开始扫描", Toast.LENGTH_SHORT).show();
            // 预先定义停止蓝牙扫描的时间（因为蓝牙扫描需要消耗较多的电量）
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mScanning = false;
                    bluetoothAdapter.stopLeScan(mLeScanCallback);
                    tarScanCallback.scanningStop();
                    Log.e(TAG, "扫描到的蓝牙设备: " + bluetoothDeviceArrayList);
                    Toast.makeText(context, "停止扫描", Toast.LENGTH_SHORT).show();
                }
            }, SCAN_PERIOD);


        } else {
            mScanning = false;
            bluetoothAdapter.stopLeScan(mLeScanCallback);
        }

    }


    /**
     * 扫描回调
     */

    private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
            String name = device.getName();
            if (name != null) {
                if (!bluetoothDeviceArrayList.contains(device)) {
                    bluetoothDeviceArrayList.add(device);
                    tarScanCallback.scanningResults(bluetoothDeviceArrayList);
                    Log.e(TAG, "onLeScan:device.getName()" + device.getName());
                    Log.e(TAG, "onLeScan:device.getAddress()" + device.getAddress());
                }
            }
        }

    };


    ScanCallback scanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            super.onScanResult(callbackType, result);
            Log.e(TAG, "onScanResult: ");

        }

        @Override
        public void onBatchScanResults(List<ScanResult> results) {
            super.onBatchScanResults(results);
            Log.e(TAG, "onBatchScanResults: ");

        }

        @Override
        public void onScanFailed(int errorCode) {
            super.onScanFailed(errorCode);
            Log.e(TAG, "onScanFailed: ");

        }
    };


    /**
     * 连接蓝牙 参数为目标设备
     */
    public void connectBle(BluetoothDevice bluetoothDevice) {
        mBluetoothDevice = bluetoothDevice;
        if (bluetoothDevice != null) {
//            //第二个参数 是否重连
            mBluetoothGatt = bluetoothDevice.connectGatt(context, false, bluetoothGattCallback);
            bluetoothGattList.add(mBluetoothGatt);
            Toast.makeText(context, "开始建立连接！", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 蓝牙连接成功回调
     */
    private BluetoothGattCallback bluetoothGattCallback = new BluetoothGattCallback() {
        @Override
        public void onPhyUpdate(BluetoothGatt gatt, int txPhy, int rxPhy, int status) {
            super.onPhyUpdate(gatt, txPhy, rxPhy, status);
        }

        @Override
        public void onPhyRead(BluetoothGatt gatt, int txPhy, int rxPhy, int status) {
            super.onPhyRead(gatt, txPhy, rxPhy, status);
        }

        //不要执行耗时操作
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            super.onConnectionStateChange(gatt, status, newState);
            if (status == BluetoothGatt.GATT_SUCCESS) {
//                Toast.makeText(context,"连接成功！",Toast.LENGTH_SHORT).show();
                if (newState == BluetoothProfile.STATE_CONNECTED) {//连接成功
                    Log.e(TAG, "onConnectionStateChange 蓝牙连接");
                    gatt.discoverServices();//发现服务
                } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                    Log.e(TAG, "onConnectionStateChange 蓝牙断连");
                    if (mBluetoothDevice != null) {//重新连接
                        //关闭当前新的连接
                        gatt.close();
                        characteristic = null;
                        adapterFreshHandler.sendEmptyMessage(0);
//                    connectBle(mBluetoothDevice);
                    }
                } else {
                    Log.e(TAG, "onConnectionStateChange: ");
                }
            } else if (status == 133) {
                Toast.makeText(context, "蓝牙连接数量过多！", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "连接不成功！", Toast.LENGTH_SHORT).show();
            }


        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {//以上的方法gatt.discoverServices();才会回调
            super.onServicesDiscovered(gatt, status);
            //回调之后，设备之间才真正通信连接起来
            if (status == BluetoothGatt.GATT_SUCCESS) {

                Log.e(TAG, "onServicesDiscovered 蓝牙连接正常");
                List<BluetoothGattService> serviceList = gatt.getServices();
                Log.e(TAG, "所有服务: " + serviceList);
//                BluetoothGattService service = gatt.getService(UUID.fromString("00001800-0000-1000-8000-00805f9b34fb"));
                BluetoothGattService service = gatt.getService(UUID.fromString(BleConstantValue.serverUuid));
                List<BluetoothGattCharacteristic> characteristicList = service.getCharacteristics();
                characteristic = service.getCharacteristic(UUID.fromString(BleConstantValue.charaUuid));
//                characteristic = service.getCharacteristic(UUID.fromString("00002a00-0000-1000-8000-00805f9b34fb"));
//                setCharacteristicNotification(characteristic, true);
//                setCharacteristicNotificationss(gatt, characteristic, true);
                enableNotification(gatt,UUID.fromString(BleConstantValue.serverUuid),UUID.fromString(BleConstantValue.charaUuid));
                gatt.readCharacteristic(characteristic);//执行之后，会执行下面的onCharacteristicRead的回调方法
                adapterFreshHandler.sendEmptyMessage(0);
            } else {
                Log.e(TAG, "onServicesDiscovered 蓝牙连接失败");
            }

        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicRead(gatt, characteristic, status);
            //读操作的回调
            if (status == BluetoothGatt.GATT_SUCCESS) {
//                Log.e(TAG, "read value: " + characteristic.getValue());
                Log.e(TAG, "read value: " + FormatUtil.bytesToHexString(characteristic.getValue()));

            }


        }

        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicWrite(gatt, characteristic, status);
            Log.e(TAG, "write value: " + FormatUtil.bytesToHexString(characteristic.getValue()));
            //write操作会调用此方法
            if (status == BluetoothGatt.GATT_SUCCESS) {
                System.out.println("onCharacteristicWrite成功");
//                Intent intent = new Intent(ACTION_GATT_CHARACTERISTIC_WRITE_SUCCESS);
//                // 这里通过属性能够读取你发送的数据，可以对此数据进行判断
//                characteristic.getValue();
//                mContext.sendBroadcast(intent);
            } else {
                System.out.println("onCharacteristicWrite失败");

//                Intent intent = new Intent(ACTION_GATT_CHARACTERISTIC_ERROR);
//                intent.putExtra(Constants.EXTRA_CHARACTERISTIC_ERROR_MESSAGE, "" + status);
//                mContext.sendBroadcast(intent);
            }
        }

        // 数据返回的回调（此处接收机器返回数据并作处理）
        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic
                characteristic) {
            Log.e(TAG, "接收机器返回数据：" + FormatUtil.bytesToHexString(characteristic.getValue()));//byte[]转为16进制字符串

            super.onCharacteristicChanged(gatt, characteristic);
            Log.e(TAG, "接收机器返回数据：" + FormatUtil.bytesToHexString(characteristic.getValue()));//byte[]转为16进制字符串
//                bleWriteReceiveCallback("0101010101010101010101010101010101010101");
        }

        //通过 Descriptor 写监听
        @Override
        public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor,
                                      int status) {
            super.onDescriptorWrite(gatt, descriptor, status);
            Log.e(TAG, "onDescriptorWrite: ");
        }

        // 通过 Descriptor 读监听
        @Override
        public void onDescriptorRead(BluetoothGatt gatt, BluetoothGattDescriptor descriptor,
                                     int status) {
            super.onDescriptorRead(gatt, descriptor, status);
            Log.e(TAG, "onDescriptorRead: ");
        }

        @Override
        public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
            super.onReadRemoteRssi(gatt, rssi, status);
            Log.e(TAG, "onReadRemoteRssi: ");
        }

        @Override
        public void onMtuChanged(BluetoothGatt gatt, int mtu, int status) {
            super.onMtuChanged(gatt, mtu, status);
            Log.e(TAG, "onMtuChanged: ");
        }
    };


    private Handler adapterFreshHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            bleDeviceListAdapter.notifyDataSetChanged();
        }
    };

    /**
     * 设置接收通知 即 另一端发送数据
     *
     * @param characteristic
     * @param enabled
     */
    public void setCharacteristicNotification(BluetoothGattCharacteristic characteristic, boolean enabled) {
        if (bluetoothAdapter == null || mBluetoothGatt == null) {
            return;
        }
        //通知远程端开启 notify
        if (characteristic.getDescriptor(UUID.fromString(BleConstantValue.descriptorUuid)) != null) {
            if (enabled == true) {
                BluetoothGattDescriptor descriptor = characteristic.getDescriptor(UUID.fromString(BleConstantValue.descriptorUuid));
                descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                mBluetoothGatt.writeDescriptor(descriptor);
            } else {
                BluetoothGattDescriptor descriptor = characteristic.getDescriptor(UUID.fromString(BleConstantValue.descriptorUuid));
                descriptor.setValue(BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE);
                mBluetoothGatt.writeDescriptor(descriptor);
            }
        }
        //设置了该特征具有Notification功能
        mBluetoothGatt.setCharacteristicNotification(characteristic, enabled);
/*
        if (bluetoothAdapter == null || mBluetoothGatt == null) {
            return;
        }

        BluetoothGattDescriptor descriptor = characteristic.getDescriptor(UUID
                .fromString(BleConstantValue.descriptorUuid));
        if (descriptor != null) {
            descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
            mBluetoothGatt.writeDescriptor(descriptor);
        }
        mBluetoothGatt.setCharacteristicNotification(characteristic, enabled);
    */
    }

    public void setCharacteristicNotificationss(BluetoothGatt gatt,BluetoothGattCharacteristic characteristic, boolean enabled) {
        if (bluetoothAdapter == null || mBluetoothGatt == null) {
            Log.e("", "BluetoothAdapter not initialized");
            return;
        }
        boolean notification = mBluetoothGatt.setCharacteristicNotification(characteristic, enabled);
        if (notification) {
            for (BluetoothGattDescriptor dp : characteristic.getDescriptors()) {
                if (dp != null) {
                    if ((characteristic.getProperties() & BluetoothGattCharacteristic.PROPERTY_NOTIFY) != 0) {
                        dp.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                    } else if ((characteristic.getProperties() & BluetoothGattCharacteristic.PROPERTY_INDICATE) != 0) {
                        dp.setValue(BluetoothGattDescriptor.ENABLE_INDICATION_VALUE);
                    }
                    gatt.writeDescriptor(dp);
                }
            }

        }
        if (true){
            return;
        }
        Log.e("", "ble notification  =" + notification);
        //这里可以加入判断对指定的UUID值进行订阅
        BluetoothGattDescriptor descriptor = characteristic.getDescriptor(UUID.fromString(BleConstantValue.descriptorUuid));
        descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
        mBluetoothGatt.writeDescriptor(descriptor);
        Log.e("", "ble 订阅");

    }


    /**
     * 写入：接受成功指令
     */
    public void bleWriteReceiveCallback(int writeStr) {
//        String writeStr = "REVOK\r\n";
//        writeCmd(writeStr.getBytes());
//        int code = Integer.valueOf(writeStr).intValue();
//        String hex = Integer.toHexString(code);
        byte[] cmd = {0x55, 0x18, (byte) 0x82, 0x01, 0x11, 0x22, 0x33, 0x44, 0x55, 0x66, 0x77, (byte) 0x88, (byte) 0x99, 0x00, 0x11, 0x22, 0x33, 0x44, 0x55, 0x55};
        if (writeStr==00){
            cmd[3] = 0x00;
        }else if (writeStr==01){
            cmd[3] = 0x01;
        }else if (writeStr==02){
            cmd[3] = 0x02;
        }else if (writeStr==03){
            cmd[3] = 0x03;
        }else if (writeStr==04){
            cmd[3] = 0x04;
        }else if (writeStr==05){
            cmd[3] = 0x05;
        }
        //          byte[] cmd = writeStr.getBytes();
        writeCmd(cmd);
    }

    /**
     * 写入命令
     *
     * @param cmd
     */
    private void writeCmd(byte[] cmd) {
        if (characteristic != null) {
            boolean notification = mBluetoothGatt.setCharacteristicNotification(characteristic, true);
            //设置回复形式
            characteristic.setWriteType(BluetoothGattCharacteristic.WRITE_TYPE_NO_RESPONSE);
            //将指令放置进特征中
            characteristic.setValue(cmd);
            //开始写数据
            boolean ifWriteSuccess = mBluetoothGatt.writeCharacteristic(characteristic);
            if (ifWriteSuccess) {
                Log.e(TAG, "写入成功");
            } else {
                Log.e(TAG, "写入失败");
            }
        } else {
            Toast.makeText(context, "蓝牙未连接", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 断开蓝牙设备,释放资源
     */
    public void bleDisConnectDevice(BluetoothDevice bluetoothDevice) {
        for (BluetoothGatt gatt : bluetoothGattList) {
            if (gatt != null) {
                Log.e(TAG, "断开蓝牙连接，释放资源");
                gatt.disconnect();
                gatt.close();
            }
        }
    }


    /**
     * 注册蓝牙监听广播
     */
    public void registerBleListenerReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        intentFilter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
        intentFilter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
        context.registerReceiver(bleListenerReceiver, intentFilter);
    }

    /**
     * 蓝牙监听广播接受者
     */
    private BroadcastReceiver bleListenerReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            showBleStateChange(intent);
        }
    };

    /**
     * 显示监听状态变化
     *
     * @param intent
     */
    private void showBleStateChange(Intent intent) {
        String action = intent.getAction();
        //连接的设备信息
        BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
        Log.e(TAG, "蓝牙监听广播…………………………" + action);

        if (mBluetoothDevice != null && mBluetoothDevice.equals(device)) {
            Log.e(TAG, "收到广播-->是当前连接的蓝牙设备");

            if (BluetoothDevice.ACTION_ACL_CONNECTED.equals(action)) {
                Log.e(TAG, "广播 蓝牙已经连接");

            } else if (BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(action)) {
                Log.e(TAG, "广播 蓝牙断开连接");
            }
        } else {
            Log.e(TAG, "收到广播-->不是当前连接的蓝牙设备");
        }

        if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {
            int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);
            switch (state) {
                case BluetoothAdapter.STATE_OFF:
                    Log.e(TAG, "STATE_OFF 蓝牙关闭");
                    Toast.makeText(context, "蓝牙关闭", Toast.LENGTH_SHORT).show();
                    bleDisConnectDevice(mBluetoothDevice);
                    bleDeviceListAdapter.clear();
                    break;
                case BluetoothAdapter.STATE_TURNING_OFF:
                    Log.e(TAG, "STATE_TURNING_OFF 蓝牙正在关闭");
                    //停止蓝牙扫描
                    scanBluetoothDevice(false);
                    break;
                case BluetoothAdapter.STATE_ON:
                    Log.d(TAG, "STATE_ON 蓝牙开启");
                    Toast.makeText(context, "蓝牙开启", Toast.LENGTH_SHORT).show();

                    break;
                case BluetoothAdapter.STATE_TURNING_ON:
                    Log.e(TAG, "STATE_TURNING_ON 蓝牙正在开启");
                    break;
            }
        }
    }

    /*
    private static final int REQUEST_COARSE_LOCATION = 0;
    private void mayRequestLocation() {
        if (Build.VERSION.SDK_INT >= 23) {
            int checkCallPhonePermission = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION);
            if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED) {
//判断是否需要 向用户解释，为什么要申请该权限
                if (ActivityCompat.shouldShowRequestPermissionRationale(context, Manifest.permission.ACCESS_COARSE_LOCATION))
                    Toast.makeText(context, "动态请求权限", Toast.LENGTH_LONG).show();
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_COARSE_LOCATION);
                return;
            } else {
            }
        } else {
        }
    }
    */
//系统方法,从requestPermissions()方法回调结果

    public boolean enableNotification(BluetoothGatt gatt, UUID serviceUUID, UUID characteristicUUID) {
        //        BluetoothGattService service = gatt.getService(serviceUUID);
//        BluetoothGattCharacteristic characteristic = findNotifyCharacteristic(service, characteristicUUID);
//        if (characteristic != null) {
//        if(bluetoothGatt.setCharacteristicNotification(characteristic2, true)){
//            //获取到Notify当中的Descriptor通道 然后再进行注册
//            BluetoothGattDescriptor clientConfig = characteristic2 .getDescriptor(UUID.fromString(DESCRIPTOR_UUID));
//            clientConfig.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
//            bluetoothGatt.writeDescriptor(clientConfig);
//        }
        boolean success = false;
        BluetoothGattService service = gatt.getService(serviceUUID);
        if (service != null) {
            BluetoothGattCharacteristic characteristic = findNotifyCharacteristic(service, characteristicUUID);
            if (characteristic != null) {
                success = gatt.setCharacteristicNotification(characteristic, true);

                gatt.readCharacteristic(characteristic);
                if (success) {
                    for(BluetoothGattDescriptor dp: characteristic.getDescriptors()){
                        if (dp != null) {
                            if ((characteristic.getProperties() & BluetoothGattCharacteristic.PROPERTY_NOTIFY) != 0) {
                                dp.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                            } else if ((characteristic.getProperties() & BluetoothGattCharacteristic.PROPERTY_INDICATE) != 0) {
                                dp.setValue(BluetoothGattDescriptor.ENABLE_INDICATION_VALUE);
                            }
                            int writeType = characteristic.getWriteType();
                            Log.e(TAG, "enableNotification: "+writeType );
                            characteristic.setWriteType(BluetoothGattCharacteristic.WRITE_TYPE_DEFAULT);
                            gatt.writeDescriptor(dp);
                            characteristic.setWriteType(writeType);
                        }
                    }
                }
            }
        }
        return success;
    }
    private BluetoothGattCharacteristic findNotifyCharacteristic(BluetoothGattService service, UUID characteristicUUID) {
        BluetoothGattCharacteristic characteristic = null;
        List<BluetoothGattCharacteristic> characteristics = service.getCharacteristics();
        for (BluetoothGattCharacteristic c : characteristics) {
            if ((c.getProperties() & BluetoothGattCharacteristic.PROPERTY_NOTIFY) != 0 && characteristicUUID.equals(c.getUuid())) {
                characteristic = c;
                break;
            }
        }
        if (characteristic != null) {
            return characteristic;
        }
        for (BluetoothGattCharacteristic c : characteristics) {
            if ((c.getProperties() & BluetoothGattCharacteristic.PROPERTY_INDICATE) != 0 && characteristicUUID.equals(c.getUuid())) {
                characteristic = c;
                break;
            }
        }
        return characteristic;
    }

}
