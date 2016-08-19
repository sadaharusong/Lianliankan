package lianliankan.LLK;

import lianliankan.LLK.R;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;

public class MediaService extends Service implements MediaPlayer.OnErrorListener,MediaPlayer.OnCompletionListener,MediaPlayer.OnPreparedListener {
      
  private MediaPlayer player;
  
  @Override
  public void onDestroy() {
      // TODO Auto-generated method stub
      super.onDestroy();
      if(player!=null){
          player.stop();
          player.release();
      }
  }

  @Override
	public void onStart(Intent intent, int startId) {
	  player=MediaPlayer.create(this.getApplicationContext(), R.raw.ready);
	  if(player != null) {
		  player.start();
		  try {
			Thread.sleep(1800);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		  player = MediaPlayer.create(this.getApplicationContext(), R.raw.go);
		  player.start();
		  player = MediaPlayer.create(this.getApplicationContext(), R.raw.back);
		  player.setVolume(3, 3);
		  player.start();
		  player.setLooping(true);
	  }
	}

  @Override
  public void onCompletion(MediaPlayer arg0) {
      // TODO Auto-generated method stub
      Log.d("Media","finished.");
  }

  @Override
  public void onPrepared(MediaPlayer arg0) {
      // TODO Auto-generated method stub
      Log.d("Media","prepared.");
      player.start();
  }
  
  @Override
  public boolean onError(MediaPlayer arg0,int what, int extra) {          
      Log.d("Media","onError");
      player.stop();
      return false;
  }

@Override
public IBinder onBind(Intent intent) {
	// TODO Auto-generated method stub
	return null;
}
}
