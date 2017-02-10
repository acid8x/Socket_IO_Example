package com.example.acid8xtreme.socket_io_example;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends FragmentActivity {

    public EditText editText;
    public TextView textView;
    public Button button;
    private MainFragment mainFragment = null;

    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Constants.MESSAGE_SOCKET_IO:
                    String socketMessage = msg.getData().getString(Constants.SOCKET_IO);
                    if (socketMessage != null) {
                        textView.append("\nReceive: " + socketMessage);
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            mainFragment = MainFragment.newInstance(mHandler);
            getSupportFragmentManager().beginTransaction().add(mainFragment, "worker").commit();
            mainFragment.setRetainInstance(true);
        }

        editText = (EditText) findViewById(R.id.editText);
        editText.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == android.view.KeyEvent.ACTION_DOWN) && (keyCode == android.view.KeyEvent.KEYCODE_ENTER)) {
                    sendSocketMessage();
                    return true;
                }
                return false;
            }
        });
        textView = (TextView) findViewById(R.id.textView);
        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendSocketMessage();
            }
        });
    }

    private void sendSocketMessage() {
        String message = editText.getText().toString();
        if (!message.equals("")) {
            mainFragment.attemptSend(message);
            textView.append("\nSent: " + message);
            editText.setText("");
        }
    }

    @Override
    public void onBackPressed() {
        finishAndRemoveTask();
        System.exit(0);
    }
}
