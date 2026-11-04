package com.example.deltataphelper;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.provider.Settings;
import android.view.Gravity;
import android.view.View;
import android.widget.*;
import android.graphics.Color;

public class MainActivity extends Activity {
    EditText x,y,delay,count;
    TextView status;
    LinearLayout root;
    int dp(float n){return (int)(n*getResources().getDisplayMetrics().density+0.5f);}
    @Override public void onCreate(Bundle b){
        super.onCreate(b);
        getWindow().getDecorView().setBackgroundColor(Color.rgb(15,22,30));
        root=new LinearLayout(this);root.setOrientation(1);root.setPadding(dp(18),dp(24),dp(18),dp(24));
        ScrollView sc=new ScrollView(this);sc.addView(root);setContentView(sc);
        addText("自动点击助手",26,0xff55d6a5,true);
        addText("用户自定义点击流程｜安卓无障碍服务",13,0xffc4d0d8,false);
        addText("使用方法",18,Color.WHITE,true);
        addText("1. 打开无障碍权限。\\n2. 在目标界面确认按钮位置并填写屏幕坐标。\\n3. 设置点击间隔和次数。\\n4. 开始后可随时停止。",14,0xffd1dbe2,false);
        addText("点击位置（屏幕像素坐标）",16,Color.WHITE,true);
        x=field("X 坐标，例如 540");y=field("Y 坐标，例如 1500");
        addText("点击间隔（毫秒，建议至少 1000）",14,0xffc4d0d8,false);
        delay=field("间隔，例如 1800");
        addText("点击次数（最多 30 次）",14,0xffc4d0d8,false);
        count=field("次数，例如 5");
        Button perm=button("打开无障碍设置");root.addView(perm);perm.setOnClickListener(v->startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)));
        Button start=button("开始点击");root.addView(start);start.setOnClickListener(v->startMacro());
        Button stop=button("停止");root.addView(stop);stop.setOnClickListener(v->send(TapAccessibilityService.ACTION_STOP));
        status=addText("状态：等待设置",14,0xff55d6a5,true);
        addText("重要说明：这是通用、可见、由你配置的点击器。请只用于你有权自动化的界面；不自动登录账号、不读取游戏数据、不隐藏运行，也不绕过反作弊。每次开始前请确认点击位置，避免误操作。",12,0xffffd071,false);
    }
    TextView addText(String s,int size,int color,boolean bold){TextView t=new TextView(this);t.setText(s);t.setTextSize(size);t.setTextColor(color);if(bold)t.setTypeface(null,1);t.setPadding(0,dp(9),0,dp(9));root.addView(t);return t;}
    EditText field(String hint){EditText e=new EditText(this);e.setSingleLine(true);e.setHint(hint);e.setTextColor(Color.WHITE);e.setHintTextColor(0xff8798a5);e.setInputType(2);root.addView(e);return e;}
    Button button(String s){Button b=new Button(this);b.setText(s);b.setAllCaps(false);return b;}
    void startMacro(){
        try{
            int px=Integer.parseInt(x.getText().toString().trim());
            int py=Integer.parseInt(y.getText().toString().trim());
            int ms=Integer.parseInt(delay.getText().toString().trim());
            int n=Integer.parseInt(count.getText().toString().trim());
            if(px<0||py<0||ms<1000||n<1||n>30){status.setText("状态：坐标或参数超出范围");return;}
            boolean ok=TapAccessibilityService.startFromUi(px,py,ms,n);
            status.setText(ok?"状态：已开始，立即切换到目标界面":"状态：无障碍服务尚未启用，请先开启后返回本软件");
        }catch(Exception e){status.setText("状态：请正确填写全部数字");}
    }
    void send(String action){TapAccessibilityService.stopFromUi();status.setText("状态：已停止");}
}
