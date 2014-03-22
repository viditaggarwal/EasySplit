package makemachine.android.examples;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Membership extends Activity{
	private String url1 =  "http://107.170.69.155:8080/easy-split-api/alterGroup";
	Context ctx;
	Intent i1;
	@Override
	public void onCreate(Bundle savedInstanceState) {
	// TODO Auto-generated method stub
	super.onCreate(savedInstanceState);
	ctx = Membership.this;
	setContentView(R.layout.testmembership);
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
