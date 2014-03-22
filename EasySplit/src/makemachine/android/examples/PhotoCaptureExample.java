package makemachine.android.examples;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.googlecode.tesseract.android.TessBaseAPI;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("NewApi")
public class PhotoCaptureExample extends Activity {
	protected Button _button;
	protected ImageView _image;
	protected TextView _field;
	protected String _path;
	protected String _mypath;
	protected String _outputpath;
	protected boolean _taken;
	protected Bitmap bitmap;
	protected String recognizedText;
	protected Notification noti;
	protected NotificationManager notificationManager;
	protected ArrayList<HashMap<String, String> > lst = null;

	protected static final String PHOTO_TAKEN = "photo_taken";

	ProgressDialog progress = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.main);

		_image = (ImageView) findViewById(R.id.image);
		_field = (TextView) findViewById(R.id.field);
		_button = (Button) findViewById(R.id.button);
		_button.setOnClickListener(new ButtonClickHandler());

		_path = Environment.getExternalStorageDirectory()
				+ "/images/make_machine_example.jpg";
		_mypath = Environment.getExternalStorageDirectory()
				+ "/images/tessdata";
	}

	public class ButtonClickHandler implements View.OnClickListener {
		public void onClick(View view) {
			Log.i("MakeMachine", "ButtonClickHandler.onClick()");
			startCameraActivity();
		}
	}

	protected void startCameraActivity() {
		Log.i("MakeMachine", "startCameraActivity()");
		File file = new File(_mypath);
		boolean success = true;
		// if(!file.exists()){
		// System.out.println("File does not exist");
		// success = file.mkdirs();
		// CopyAssets();
		// }
		if (success) {
			File fl = new File(_path);
			Uri outputFileUri = Uri.fromFile(fl);

			Intent intent = new Intent(
					android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
			intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);

			Log.i("MakeMachine", "BeforeActivity");
			startActivityForResult(intent, 0);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.i("MakeMachine", "resultCode: " + resultCode);
		switch (resultCode) {
		case 0:
			Log.i("MakeMachine", "User cancelled");
			break;

		case -1:
			new photoOperation().execute("");
			break;
		}
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		Log.i("MakeMachine", "onRestoreInstanceState()");
		if (savedInstanceState.getBoolean(PhotoCaptureExample.PHOTO_TAKEN)) {
			new photoOperation().execute("");
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putBoolean(PhotoCaptureExample.PHOTO_TAKEN, _taken);
	}

	private HashMap<String, Integer> makeDictionary() {
		HashMap<String, Integer> hs = new HashMap<String, Integer>();
		hs.put("subtotal", 1);
		hs.put("total", 1);
		hs.put("mastercard", 1);
		hs.put("visa", 1);
		hs.put("balance", 1);
		hs.put("grandtotal", 1);
		hs.put("amount", 1);
		hs.put("totl", 1);
		hs.put("amt", 1);
		hs.put("totol", 1);
		hs.put("grandtotl", 1);
		hs.put("subtotl", 1);
		return hs;
	}

	public Double parser(String text) {
		String total = "";
		try {
			text = text.toLowerCase();
			text = text.replaceAll("[^a-zA-Z0-9\\s\\.]", "");
			HashMap<String, Integer> hs = makeDictionary();
			String s[] = text.split("\\s+");

			for (int i = 0; i < s.length; i++) {
				if (s[i] != null && s[i] != "") {
					Log.i("MakeMachine", "hs key is " + s[i]);
					java.util.Iterator iter = hs.keySet().iterator();
					double max = 0.0;
					while (iter.hasNext()) {
						double dist = jaccardSimilarity(s[i],
								(String) iter.next());
						if (dist > max) {
							max = dist;
							total = s[i + 1];
						}
					}
				}
			}
			total = total.replaceAll("[^0-9\\.]", "");
			if(total == ""){
				total = "0.0";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Double.valueOf(total);
	}

	public static double jaccardSimilarity(String similar1, String similar2) {
		HashSet<String> h1 = new HashSet<String>();
		HashSet<String> h2 = new HashSet<String>();

		for (String s : similar1.split("\\s+")) {
			h1.add(s);
		}
		for (String s : similar2.split("\\s+")) {
			h2.add(s);
		}

		int sizeh1 = h1.size();
		// Retains all elements in h3 that are contained in h2 ie intersection
		h1.retainAll(h2);
		// h1 now contains the intersection of h1 and h2

		h2.removeAll(h1);
		// h2 now contains unique elements

		// Union
		int union = sizeh1 + h2.size();
		int intersection = h1.size();

		return (double) intersection / union;

	}

	private void CopyAssets() {
		System.out.println("copy assets");
		AssetManager assetManager = getAssets();
		String[] files = null;
		try {
			files = assetManager.list("tessdata");
		} catch (IOException e) {
			Log.e("tag", e.getMessage());
		}

		for (String filename : files) {
			System.out.println("File name => " + filename);
			InputStream in = null;
			OutputStream out = null;
			try {
				in = assetManager.open("tessdata/" + filename); // if files
																// resides
																// inside the
																// "Files"
																// directory
																// itself
				out = new FileOutputStream(Environment
						.getExternalStorageDirectory().toString()
						+ "/images/tessdata/" + filename);
				copyFile(in, out);
				in.close();
				in = null;
				out.flush();
				out.close();
				out = null;
			} catch (Exception e) {
				Log.e("tag", e.getMessage());
			}
		}
	}

	private void copyFile(InputStream in, OutputStream out) throws IOException {
		byte[] buffer = new byte[1024];
		int read;
		while ((read = in.read(buffer)) != -1) {
			out.write(buffer, 0, read);
		}
	}

	@SuppressLint("NewApi")
	private class photoOperation extends AsyncTask<String, Void, String> {
		String resp = "";
		String groupName, groupId, owner, memName;
		
		
		@Override
		protected String doInBackground(String... params) {

			try {
				TessBaseAPI baseApi = new TessBaseAPI();
				Log.i("MakeMachine", "AfterTess");
				String DATA_PATH = Environment.getExternalStorageDirectory()
						+ "/images/";
				String lang = "eng";
				baseApi.init(DATA_PATH, lang);
				baseApi.setImage(bitmap);
				recognizedText = baseApi.getUTF8Text();
				System.out.println("recognizedText: " + recognizedText);
				baseApi.end();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return "Executed";
		}

		@Override
		protected void onPostExecute(String result) {
			progress.dismiss();
			_field.setVisibility(View.GONE);

			String url1 = " http://107.170.69.155:8080/easy-split-api/addBill";

			try {
				
				URL url = new URL(url1);
				HttpURLConnection conn = (HttpURLConnection) url
						.openConnection();
				conn.setDoOutput(true);
				conn.setRequestMethod("POST");
				conn.setRequestProperty("Content-Type", "application/json");
				
				Bundle bndl = getIntent().getExtras();				
				groupName = bndl.getString("groupName");
				owner = bndl.getString("owner");
				groupId = bndl.getString("groupId");
				memName = bndl.getString("memName");
				Double amt = parser(recognizedText);
				String input = "{\"groupId\":\"" + groupId + "\"," 
						+"\"userName\":\""+ memName + "\","
						+"\"amount\":\""+ amt + "\""+
						"}";
				System.out.println("amt is "+amt);
				System.out.println("input for photo is "+input);

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

				conn.disconnect();

			} catch (NullPointerException e) {

				e.printStackTrace();

			} catch (MalformedURLException e) {

				e.printStackTrace();

			} catch (IOException e) {

				e.printStackTrace();

			}

			Intent intent = new Intent(PhotoCaptureExample.this,GroupDetails.class);
			intent.putExtra("groupId", groupId);
			intent.putExtra("groupName", groupName);
			intent.putExtra("owner", owner);
			intent.putExtra("memName", memName);
			intent.putExtra("stats", readAndParseJSON(resp).toString());
			startActivity(intent);
		}

		private JSONObject readAndParseJSON(String resp) {
			JSONObject tempobj = null;
			JSONObject object = null;
			try {
				JSONObject result = new JSONObject(resp);
				boolean status = result.getBoolean("success");
				if(status){
					JSONObject grp = result.getJSONObject("updatedGroup");
					
					String failMsg = result.getString("failMsg");
					tempobj = grp.getJSONObject("stats");
				}
				
			} catch (JSONException e1) {
				e1.printStackTrace();
			}
			return tempobj;
		}

		@SuppressLint("NewApi")
		@Override
		protected void onPreExecute() {
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inSampleSize = 4;
			bitmap = BitmapFactory.decodeFile(_path, options);
			_image.setImageBitmap(bitmap);

			progress = new ProgressDialog(PhotoCaptureExample.this);
			progress.setTitle("Processing");
			progress.setMessage("SuperBot Working...");
			progress.show();

		}

		@Override
		protected void onProgressUpdate(Void... values) {
		}
	}

}