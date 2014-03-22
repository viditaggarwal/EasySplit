package makemachine.android.examples;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Login extends Activity{

	    /** Called when the activity is first created. */
		Context ctx;
		Intent i1,i2;
		int k1;
		int k2;
		@Override
		public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
			super.onCreate(savedInstanceState);
			setContentView(R.layout.test);
			ctx =Login.this;
			final EditText et = (EditText) findViewById(R.id.editText1);
	        final Button l = (Button)findViewById(R.id.button1);
	        k1 = 4;
	        l.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					k1 = 5;
					// TODO Auto-generated method stub
					i1 = new Intent(ctx, Profile.class).putExtra("name", et.getText().toString());
				    startActivity(i1);
				}
	        });
	        
		}
}
