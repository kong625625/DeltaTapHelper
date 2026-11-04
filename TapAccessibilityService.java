package com.example.deltataphelper;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.GestureDescription;
import android.graphics.Path;
import android.os.Handler;
import android.os.Looper;
import android.content.Intent;
import android.view.accessibility.AccessibilityEvent;
import android.widget.Toast;

public class TapAccessibilityService extends AccessibilityService {
    public static final String ACTION_START="com.example.deltataphelper.START";
    public static final String ACTION_STOP="com.example.deltataphelper.STOP";
    private final Handler handler=new Handler(Looper.getMainLooper());
    private int x,y,delay,left;
    private boolean running=false;
    private static TapAccessibilityService instance;
    public static boolean isReady(){return instance!=null;}
    public static boolean startFromUi(int x,int y,int delay,int count){
        if(instance==null)return false;
        instance.begin(x,y,delay,count);return true;
    }
    public static void stopFromUi(){if(instance!=null)instance.stopMacro();}

    @Override public void onAccessibilityEvent(AccessibilityEvent event) {}
    @Override public void onInterrupt(){stopMacro();}
    @Override protected void onServiceConnected(){super.onServiceConnected();instance=this;}
    private void begin(int px,int py,int ms,int n){
        if(px<0||py<0||ms<1000||n<1||n>30){toast("参数无效");return;}
        stopMacro();x=px;y=py;delay=ms;left=n;running=true;toast("点击流程已开始");scheduleTap(500);
    }

    @Override public int onStartCommand(Intent intent,int flags,int startId){
        // AccessibilityService is system-bound; UI commands are sent through its live instance.
        return START_NOT_STICKY;
    }
    private void scheduleTap(long wait){
        handler.postDelayed(()->{
            if(!running)return;
            Path path=new Path();path.moveTo(x,y);
            GestureDescription gesture=new GestureDescription.Builder()
                .addStroke(new GestureDescription.StrokeDescription(path,0,90)).build();
            boolean accepted=dispatchGesture(gesture,new GestureResultCallback(){
                @Override public void onCompleted(GestureDescription g){
                    if(!running)return;
                    left--;
                    if(left<=0){running=false;toast("本轮点击已完成");}
                    else scheduleTap(delay);
                }
                @Override public void onCancelled(GestureDescription g){stopMacro();toast("点击被系统取消，已停止");}
            },null);
            if(!accepted){stopMacro();toast("系统未接受点击请求，已停止");}
        },wait);
    }
    private void stopMacro(){running=false;handler.removeCallbacksAndMessages(null);}
    @Override public void onUnbind(Intent intent){instance=null;stopMacro();return super.onUnbind(intent);}
    private void toast(String s){new Handler(Looper.getMainLooper()).post(()->Toast.makeText(this,s,Toast.LENGTH_SHORT).show());}
    @Override public void onDestroy(){stopMacro();if(instance==this)instance=null;super.onDestroy();}
}
