package com.connectconnect.cc.view; 

import java.io.IOException;  
import java.util.Timer;  
import java.util.TimerTask;  

import android.content.Context;
import android.media.AudioManager;  
import android.media.MediaPlayer;  
import android.media.MediaPlayer.OnBufferingUpdateListener;  
import android.media.MediaPlayer.OnCompletionListener;  
import android.os.Handler;  
import android.os.Message;  
import android.util.Log;  
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MediaPlayerutil implements OnBufferingUpdateListener,  
      OnCompletionListener, MediaPlayer.OnPreparedListener{  
  public MediaPlayer mediaPlayer;  
  private TextView skbProgress;  
  private Timer mTimer=new Timer();  
  private int times;
  private int totalTimes;
  private Context context;
  public MediaPlayerutil(TextView skbProgress,int times,Context context)  
  {  
      this.skbProgress=skbProgress;  
      this.times=times;
      this.context=context;
      this.totalTimes=times;
      try {  
          mediaPlayer = new MediaPlayer();  
          mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);  
          mediaPlayer.setOnBufferingUpdateListener(this);  
          mediaPlayer.setOnPreparedListener(this);  
      } catch (Exception e) {  
          Log.e("mediaPlayer", "error", e);  
      }  
        
      mTimer.schedule(mTimerTask, 0, 1000);  
  }  
    
  /******************************************************* 
   * 通过定时器和Handler来更新进度条 
   ******************************************************/  
  TimerTask mTimerTask = new TimerTask() {  
      @Override  
      public void run() {  
          if(mediaPlayer==null)  
              return;  
          if (mediaPlayer.isPlaying() && skbProgress.isPressed() == false) {  
              handleProgress.sendEmptyMessage(0);  
          }  
      }  
  };  
    
  Handler handleProgress = new Handler() {  
      public void handleMessage(Message msg) {  

          int position = mediaPlayer.getCurrentPosition();  
          int duration = mediaPlayer.getDuration();  
            
          if (duration > 0) {  
//              long pos = skbProgress.getMax() * position / duration;  
//              skbProgress.setProgress((int) pos); 
        	  times=times-1;
        	  skbProgress.setText(times+"S");
          }  
      };  
  };  
  //*****************************************************  
    
  public void play()  
  {  
      mediaPlayer.start();  
  }  
    
  public void playUrl(String videoUrl)  
  {  
      try {  
          mediaPlayer.reset();  
          mediaPlayer.setDataSource(videoUrl);  
          mediaPlayer.prepare();//prepare之后自动播放  
          mediaPlayer.setOnCompletionListener(new OnCompletionListener() {
			
			@Override
			public void onCompletion(MediaPlayer arg0) {
				// TODO Auto-generated method stub
				skbProgress.setEnabled(true);
				skbProgress.setText(totalTimes+"S");
				Toast.makeText(context, "播放完成", Toast.LENGTH_LONG).show();
			}
		});
          //mediaPlayer.start();  
      } catch (IllegalArgumentException e) {  
          // TODO Auto-generated catch block  
          e.printStackTrace();  
      } catch (IllegalStateException e) {  
          // TODO Auto-generated catch block  
          e.printStackTrace();  
      } catch (IOException e) {  
          // TODO Auto-generated catch block  
          e.printStackTrace();  
      }  
  }  

    
  public void pause()  
  {  
      mediaPlayer.pause();  
  }  
    
  public void stop()  
  {  
      if (mediaPlayer != null) {   
          mediaPlayer.stop();  
          mediaPlayer.release();   
          mediaPlayer = null;   
      }   
  }  

  @Override  
  /**  
   * 通过onPrepared播放  
   */  
  public void onPrepared(MediaPlayer arg0) {  
      arg0.start();  
      Log.e("mediaPlayer", "onPrepared");  
  }  

  @Override  
  public void onCompletion(MediaPlayer arg0) {  
      Log.e("mediaPlayer", "onCompletion");  
  }  

  @Override  
  public void onBufferingUpdate(MediaPlayer arg0, int bufferingProgress) {  
//      skbProgress.setSecondaryProgress(bufferingProgress);  
//      int currentProgress=skbProgress.getMax()*mediaPlayer.getCurrentPosition()/mediaPlayer.getDuration();  
//      Log.e(currentProgress+"% play", bufferingProgress + "% buffer");  
  }  

}  