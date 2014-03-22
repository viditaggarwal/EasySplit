package makemachine.android.examples;
import java.util.Date;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnCreateContextMenuListener;
import android.widget.Button;
import android.widget.EditText;

public class Activity3 extends Activity{

	    /** Called when the activity is first created. */
		Context ctx;
		Intent i1;
		@Override
		public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		ctx = Activity3.this;
		setContentView(R.layout.test3);
		Button l = (Button)findViewById(R.id.button1);
        l.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				i1 = new Intent(ctx, Activity2.class);
			    startActivity(i1);
			}
		});
        Button t = (Button)findViewById(R.id.button2);
        t.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				
				// TODO Auto-generated method stub
				i1 = new Intent(ctx, Activity2.class);
			    startActivity(i1);
			}
		});
        
	}
}
