package space.wolv.r6sdrone;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

import space.wolv.r6sdrone.joystick.JoystickView;

public class MainActivity extends Activity implements JoystickView.JoystickListener
{
    BluetoothSocket btSocket;
    BluetoothDevice btDevice = null;

    public void sendBtMsg(String msg)
    {
        try
        {
            if (!btSocket.isConnected())
            {
                btSocket.connect();
            }

            OutputStream btOutputStream = btSocket.getOutputStream();
            btOutputStream.write(msg.getBytes());
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();

        if (!btAdapter.isEnabled())
        {
            Intent enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBluetooth, 0);
        }

        Set<BluetoothDevice> pairedDevices = btAdapter.getBondedDevices();

        if (pairedDevices.size() > 0)
        {
            for (BluetoothDevice device : pairedDevices)
            {
                if(device.getName().equalsIgnoreCase("raspberrypi"))
                {
                    Log.e("Drone", device.getName());
                    btDevice = device;
                    break;
                }
            }
        }

        try
        {
            UUID uuid = UUID.fromString("94f39d29-7d6d-437d-973b-fba39e49d4ee"); // standard SerialPortService ID

            btSocket = btDevice.createRfcommSocketToServiceRecord(uuid);
            if (!btSocket.isConnected())
            {
                btSocket.connect();
            }
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onJoystickMoved(float xPercent, float yPercent, int id)
    {
        yPercent *= -1;
        String droneData = (yPercent > 0) ? "up" : "down";

        switch(id)
        {
            case R.id.joystickLeft:
                sendBtMsg("Left: " + droneData );
                break;
            case R.id.joystickRight:
                sendBtMsg("Right: " + droneData);
                break;
        }
    }
}
