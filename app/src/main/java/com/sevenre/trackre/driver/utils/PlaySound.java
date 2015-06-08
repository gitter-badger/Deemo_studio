package com.sevenre.trackre.driver.utils;

import java.io.IOException;

import com.sevenre.trackre.driver.R;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;

public class PlaySound {

	public static void play(Context context, int id) {
		
		Uri soundUri = null;
		switch (id) {
		case Utils.no_internet:
			soundUri = Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.internet);
			break;
		case Utils.no_gps:
			soundUri = Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.gps);
			break;
		case Utils.unknown_error:
			soundUri = Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.trackre);
			break;
		case Utils.try_again:
			soundUri = Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.tryagain);
			break;
		case Utils.student_found:
			soundUri = Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.student_found);
			break;
		case Utils.student_not_found:
			soundUri = Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.student_not_found);
			break;
		default: 
			return;
		}
		
		MediaPlayer mediaPlayer = new MediaPlayer();

		try {
		      mediaPlayer.setDataSource(context, soundUri);
		      mediaPlayer.setAudioStreamType(AudioManager.STREAM_NOTIFICATION);
		      mediaPlayer.prepare();
		      mediaPlayer.setOnCompletionListener(new OnCompletionListener() {

		         @Override
		         public void onCompletion(MediaPlayer mp)
		         {
		            mp.release();
		         }
		      });
		  mediaPlayer.start();
		} catch (IllegalArgumentException e) {
		 e.printStackTrace();
		} catch (SecurityException e) {
		 e.printStackTrace();
		} catch (IllegalStateException e) {
		 e.printStackTrace();
		} catch (IOException e) {
		 e.printStackTrace();
		}

	}

}
