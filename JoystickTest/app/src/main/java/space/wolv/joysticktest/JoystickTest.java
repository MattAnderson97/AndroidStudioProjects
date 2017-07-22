package space.wolv.joysticktest;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class JoystickTest extends Activity implements JoystickView.JoystickListener{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joystick_test);
    }

    @Override
    public void onJoystickMoved(float xPercent, float yPercent, int id) {
        switch(id)
        {
            case R.id.joystickLeft:
                Log.d("Right joystick", "direction: " + ((xPercent > 0) ? "right" : "left") + "|" + ((yPercent > 0) ? "up" : "down"));
                break;
            case R.id.joystickRight:
                Log.d("Left joystick", "direction: " + ((xPercent > 0) ? "right" : "left") + "|" + ((yPercent > 0) ? "up" : "down"));
                break;
        }
    }
}
