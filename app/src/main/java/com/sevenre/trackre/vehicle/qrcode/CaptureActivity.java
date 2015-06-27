/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sevenre.trackre.vehicle.qrcode;

import java.text.DateFormat;
import java.util.Date;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.Result;
import com.sevenre.trackre.vehicle.R;
import com.sevenre.trackre.vehicle.qrcode.result.ResultHandler;
import com.sevenre.trackre.vehicle.qrcode.result.ResultHandlerFactory;
import com.sevenre.trackre.vehicle.utils.PlaySound;
import com.sevenre.trackre.vehicle.utils.Utils;

/**
 * Example Capture Activity.
 * 
 * @author Justin Wetherell (phishman3579@gmail.com)
 */
public class CaptureActivity extends DecoderActivity {

    private static final String TAG = CaptureActivity.class.getSimpleName();
    private TextView statusView = null;
    private View resultView = null;
    private boolean inScanMode = false;

    DatabaseHandler db;
    
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.qr_code_capture);
        Log.v(TAG, "onCreate()");

        resultView = findViewById(R.id.result_view);
        resultView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showCameraAndScanner();
			}
		});
        statusView = (TextView) findViewById(R.id.status_view);

        db = new DatabaseHandler(this);
        
        inScanMode = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.v(TAG, "onDestroy()");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.v(TAG, "onResume()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.v(TAG, "onPause()");
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (inScanMode)
                finish();
            else
                onResume();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void handleDecode(Result rawResult, Bitmap barcode) {
        drawResultPoints(barcode, rawResult);

        ResultHandler resultHandler = ResultHandlerFactory.makeResultHandler(this, rawResult);
        handleDecodeInternally(rawResult, resultHandler, barcode);
    }

    protected void showScanner() {
        inScanMode = true;
        resultView.setVisibility(View.GONE);
        statusView.setText(R.string.msg_default_status);
        statusView.setVisibility(View.VISIBLE);
        viewfinderView.setVisibility(View.VISIBLE);
    }

    protected void showResults() {
        inScanMode = false;
        statusView.setVisibility(View.GONE);
        viewfinderView.setVisibility(View.GONE);
        resultView.setVisibility(View.VISIBLE);
    }

    // Put up our own UI for how to handle the decodBarcodeFormated contents.
    private void handleDecodeInternally(Result rawResult, ResultHandler resultHandler, Bitmap barcode) {
        onPause();
        showResults();

        ImageView barcodeImageView = (ImageView) findViewById(R.id.barcode_image_view);
        if (barcode == null) {
            barcodeImageView.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.icon));
        } else {
            barcodeImageView.setImageBitmap(barcode);
        }
        
        DateFormat formatter = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT);
        String formattedTime = formatter.format(new Date(rawResult.getTimestamp()));
        TextView timeTextView = (TextView) findViewById(R.id.time_text_view);
        timeTextView.setText(formattedTime);

        TextView contentsTextView = (TextView) findViewById(R.id.contents_text_view);
        CharSequence displayContents = resultHandler.getDisplayContents();
        
        // Crudely scale betweeen 22 and 32 -- bigger font for shorter text
        int scaledSize = Math.max(22, 32 - displayContents.length() / 4);
        contentsTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, scaledSize);
        String[] name = {"Mahesh", "Faizan", "Manuel", "Venketash"};
        boolean find = false;
        
        for (String s : name) {
        	if (s.equalsIgnoreCase(displayContents.toString())) {
        		find = true;
        		break;
        	}
        }
        
        contentsTextView.setText(displayContents + (find==true?"\nFound":"\nNot found" ));
        if (find) {
        	resultView.setBackgroundColor(Color.parseColor("#8000FF00"));
        	PlaySound.play(getApplicationContext(), Utils.student_found);
        }
        else {
        	resultView.setBackgroundColor(Color.parseColor("#80FF0000"));
        	PlaySound.play(getApplicationContext(), Utils.student_not_found);
        }
    }
}
