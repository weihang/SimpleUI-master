package com.example.simpleui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Toast;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseObject;
public class MainActivity extends Activity {

	private EditText editText;
	private Button button;
	private SharedPreferences sp;
	private CheckBox isEncrypt;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Parse.initialize(this, "OY9kzVsuaWYsRUtNXKHn2M7bJRAD4GBmoYoONxTW", "xcYmZOZduFtKGh3FCVjpqHJdp1RhJz7rD6ljsf03");
		setContentView(R.layout.activity_main);

		sp = getSharedPreferences("settings", Context.MODE_PRIVATE);
		
		editText = (EditText) findViewById(R.id.editText1);
		editText.setHint("type something ...");
		editText.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				
				Editor editor = sp.edit();
				editor.putString("text", editText.getText().toString());
				editor.commit();
				
				Log.d("debug",
						"keyOcde =" + keyCode + "keyEvent=" + event.getAction());
				if (event.getAction() == KeyEvent.ACTION_DOWN) {
					if (keyCode == KeyEvent.KEYCODE_ENTER) {
						sendMessage();				
						return true;
					}
				}
				return false;
			}
		});
		
		editText.setText(sp.getString("text",""));
		
		button = (Button) findViewById(R.id.button1);
		button.setText("send");
		button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				sendMessage();
			}
		});// alt + /
		
		isEncrypt=(CheckBox) findViewById(R.id.checkBox1);
		isEncrypt.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				Editor editor = sp.edit();
				editor.putBoolean("isEncrypt", isChecked);
				editor.commit();
				
			}
		});
		
		isEncrypt.setChecked(sp.getBoolean("isEncrypt", false));
		
		
		
	}

	public void submit2(View view) {
		sendMessage();
	}

	private void sendMessage() {
		String text = editText.getText().toString();
		
		ParseObject messageObject = new ParseObject("message");
		messageObject.put("text", text);
		messageObject.put("isEncrypt", isEncrypt.isChecked());
		messageObject.saveInBackground();

		if(isEncrypt.isChecked()){
			text="********";
		}
	
		editText.getText().clear();
		Toast.makeText(this, text, Toast.LENGTH_LONG).show();
		
		Intent intent = new Intent();
		intent.setClass(this, Message.class);
		intent.putExtra("text", text);
		startActivity(intent);

	}
	
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
