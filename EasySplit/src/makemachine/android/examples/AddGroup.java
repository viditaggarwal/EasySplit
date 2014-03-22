package makemachine.android.examples;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class AddGroup extends Activity {
	Context ctx;
	private String url1 = " http://107.170.69.155:8080/easy-split-api/createGroup";
	private HandleJSON obj;
	EditText ed;
	String owner = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		ctx = AddGroup.this;
		setContentView(R.layout.testt5);
		ed = (EditText) findViewById(R.id.grpName);
		Button l = (Button) findViewById(R.id.addGrp);
		Bundle bndl = getIntent().getExtras();
		owner = bndl.getString("owner");
		l.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				// TODO Auto-generated method stub
				Intent i1 = new Intent(ctx, GroupDetails.class);
				startActivity(i1);
			}
		});

	}

	public void open(View view) throws MalformedURLException, IOException {
		EditText username = (EditText) findViewById(R.id.editText1);
		String url3 = username.getText().toString();
		fetchJSON(url3);
	}

	public void fetchJSON(final String name) {
		try {
			String resp = "";
			URL url = new URL(url1);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json");

			String input = "{\"groupName\":\"" + ed.getText().toString() + "\""
					+ "\"owner\":\"" + owner + "}";

			System.out.println("url:" + url);

			System.out.println("input:" + input);

			OutputStream os = conn.getOutputStream();
			os.write(input.getBytes());
			os.flush();

			BufferedReader br = new BufferedReader(new InputStreamReader(
					(conn.getInputStream())));

			String output;
			while ((output = br.readLine()) != null) {
				resp += output;
			}

			readAndParseJSON(resp);

			conn.disconnect();

		} catch (MalformedURLException e) {

			e.printStackTrace();

		} catch (IOException e) {

			e.printStackTrace();

		}

	}

	private void readAndParseJSON(String resp) {
		JSONObject tempobj = null;
		JSONObject object = null;
		try {
			JSONObject result = new JSONObject(resp);
			boolean status = result.getBoolean("success");
			if (status) {
				JSONObject grp = result.getJSONObject("thisGroup");

				tempobj = grp.getJSONObject("stats");
			}

		} catch (JSONException e1) {
			e1.printStackTrace();
		}
	}

}
