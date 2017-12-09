package com.example.tll.map;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

//Activity For Setting

public class ThirdActivity extends AppCompatActivity {

    public Properties prop;
    private Context context;
    protected static final String TAG="settings_config";

    private EditText mEditText;
    private EditText mEditText2;
    private EditText mEditText3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thrid);

        TestProp();

        mEditText = (EditText) findViewById(R.id.threshold_app_pm2_5);
        mEditText2 = (EditText) findViewById(R.id.threshold_app_temp);
        mEditText3 = (EditText) findViewById(R.id.threshold_app_humi);

        setSaveconfig();

        Button btn1 = (Button) findViewById(R.id.button7);

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
              public void onClick(View v) {
                // TODO Auto-generated method stub

                String thr_app_pm = mEditText.getText().toString();
                String thr_app_temp = mEditText2.getText().toString();
                String thr_app_humi = mEditText3.getText().toString();

                if ((TextUtils.isEmpty(thr_app_pm)) || (TextUtils.isEmpty(thr_app_temp)) || (TextUtils.isEmpty(thr_app_humi))){
                    Toast.makeText(ThirdActivity.this, "设置不能为空", Toast.LENGTH_SHORT).show();}
                else {
                    Toast.makeText(ThirdActivity.this, "配置成功", Toast.LENGTH_SHORT).show();

                    prop.put("app_threshold_pm2.5", thr_app_pm);
                    prop.put("app_threshold_temp", thr_app_temp);
                    prop.put("app_threshold_humi", thr_app_humi);
                    saveConfig(context, "/mnt/sdcard/sconfig.properties", prop);
                }

                Log.i(TAG, " config: " + prop.get("app_threshold_pm2.5"));
              }
        });
    }


    /**
     * 读取配置文件
     * <p>
     * Title: loadConfig
     * <p>
     * <p>
     * Description:
     * </p>
     *
     * @param context
     * @param file
     * @return
     */
    public static Properties loadConfig(Context context, String file) {
        Properties properties = new Properties();
        try {
            FileInputStream s = new FileInputStream(file);
            properties.load(s);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return properties;
    }

    /**
     * 保存配置文件
     * <p>
     * Title: saveConfig
     * <p>
     * <p>
     * Description:
     * </p>
     *
     * @param context
     * @param file
     * @param properties
     * @return
     */
    public static boolean saveConfig(Context context, String file, Properties properties) {
        try {
            File fil = new File(file);
            if (!fil.exists())
                fil.createNewFile();
            FileOutputStream s = new FileOutputStream(fil);
            properties.store(s, "");
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


    public void TestProp() {

        prop = loadConfig(context,"/mnt/sdcard/sconfig.properties" );

        if (prop == null) {
            // 配置文件不存在的时候创建配置文件 初始化配置信息
            prop = new Properties();
            prop.put("bool", "ture");
            prop.put("string", "settings_config_test");

            //prop.put("bool", "no");// put方法可以直接修改配置信息，不会重复添加


            prop.put("app_threshold_pm2.5", "100");
            prop.put("app_threshold_temp", "40");
            prop.put("app_threshold_humi", "80");

            prop.put("rasp_threshold_smog", "1");
            prop.put("rasp_threshold_temp", "40");
            prop.put("rasp_threshold_humi", "80");

            prop.put("db_username", "null");
            prop.put("db_password", "null");
            prop.put("db_name", "localhost");
            prop.put("db_ip", "192.168.137.100");
            prop.put("db_port", "1866844L25.51mypc.cn:18029");

            // 也可以添加基本类型数据 get时就需要强制转换成封装类型
            saveConfig(context, "/mnt/sdcard/sconfig.properties", prop);

            System.out.println("properties的内容：");
            System.out.println(prop);

        }

        boolean b = !(((String) prop.get("bool")).equals("ture")) ;// get出来的都是Object对象
        // 如果是基本类型需要用到封装类

        Log.i(TAG, " config: " + b);

        String s = (String) prop.get("string");

        Log.i(TAG, " config: " + s);

        saveConfig(context, "/mnt/sdcard/sconfig.properties", prop);

    }


    public void setSaveconfig(){
        mEditText.setText(prop.get("app_threshold_pm2.5").toString());
        mEditText2.setText(prop.get("app_threshold_temp").toString());
        mEditText3.setText(prop.get("app_threshold_humi").toString());

    }

}