package makemachine.android.examples;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AddMember extends Activity{
	Context ctx;
	Intent i1;
	@Override
	public void onCreate(Bundle savedInstanceState) {
	// TODO Auto-generated method stub
	super.onCreate(savedInstanceState);
	ctx = AddMember.this;
	setContentView(R.layout.testaddmember);
	Button l = (Button)findViewById(R.id.button1);
	 l.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				
				// TODO Auto-generated method stub
				i1 = new Intent(ctx, Membership.class);
			    startActivity(i1);
			}
		});
	}
	}
