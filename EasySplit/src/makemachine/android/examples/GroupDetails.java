package makemachine.android.examples;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class GroupDetails extends Activity {
	private String url1 = "http://107.170.69.155:8080/easy-split-api/alterGroup";
	Context ctx;
	Intent i1;
	Button addBill, addMem;
	EditText memName;
	ListView listView;
	String grName, owner, name, grId; 
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		ctx = GroupDetails.this;
		setContentView(R.layout.test7);
		Bundle bndl = getIntent().getExtras();
		grId = bndl.get("groupId").toString();
		grName = bndl.get("groupName").toString();
		owner = bndl.get("owner").toString();
		name = bndl.get("memName").toString();
		listView = (ListView) findViewById(R.id.listdetails);
		JSONObject stats = null;
		try {
			stats = new JSONObject(bndl.get("stats").toString());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String[] statesList = null;
		ArrayList<String> arr = new ArrayList<String>();
		int cnt = 1;

		try {
			Iterator<String> keys = stats.keys();
			while (keys.hasNext()) {
				String key = keys.next();
				arr.add(key + ":  $" + Double.toString(stats.getDouble(key)));
				System.out.println("value of " + key + ":  $"
						+ Double.toString(stats.getDouble(key)));
			}

			statesList = new String[arr.size() + 1];
			statesList[0] = grName;
			for (int i = 0; i < arr.size(); i++) {
				statesList[cnt] = arr.get(i);
				cnt++;
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("size of stateslist is " + statesList.length);

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, statesList) {
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				View view = super.getView(position, convertView, parent);

				TextView textView = (TextView) view
						.findViewById(android.R.id.text1);

				/* YOUR CHOICE OF COLOR */
				textView.setTextColor(Color.BLACK);

				return view;
			}

		};
		listView.setAdapter(adapter);

		addBill = (Button) findViewById(R.id.addBill);
		addMem = (Button) findViewById(R.id.addMember);
		memName = (EditText) findViewById(R.id.memName);
		addBill.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				i1 = new Intent(ctx, PhotoCaptureExample.class);
				i1.putExtra("groupName", grName);
				i1.putExtra("groupId", grId);
				i1.putExtra("owner", owner);
				i1.putExtra("memName", name);
				startActivity(i1);
			}
		});

		addMem.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				i1 = new Intent(ctx, PhotoCaptureExample.class);
				i1.putExtra("groupName", grName);
				i1.putExtra("groupId", grId);
				i1.putExtra("owner", owner);
				i1.putExtra("memName", name);
				startActivity(i1);
			}
		});
	}

	public void fetchJSON(final String name) {
		try {
			String resp = "";
			URL url = new URL(url1);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json");

			String memname = memName.getText().toString();
			
			String input = "{\"owner\":\"" + owner + "\"," + 
			"\"groupName\":\""+ grName + "\"," + 
			"\"groupId\":\"" + grId + "\","+
			"\"memberName\":\"" + memname + "\"" + "}";

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

			int cnt = 0;
			readAndParseJSON(resp);

			conn.disconnect();

		} catch (NullPointerException e) {

			e.printStackTrace();

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
			if(status){
				JSONObject grp = result.getJSONObject("thisGroup");
				
				tempobj = grp.getJSONObject("stats");
			}
			
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
	}
}
