package com.example.tll.map;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
    private Button btn1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thrid);

        TestProp();

        mEditText = (EditText) findViewById(R.id.editText_config);

        btn1 = (Button) findViewById(R.id.button_config);

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
              public void onClick(View v) {
                  // TODO Auto-generated method stub
                    Toast tst = Toast.makeText(ThirdActivity.this, "配置成功", Toast.LENGTH_SHORT);
                    tst.show();

                    String conf = mEditText.getText().toString();
                    prop.put("string",conf);

                    Log.i(TAG, " config: " + conf);
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
            prop.put("int", "110");// 也可以添加基本类型数据 get时就需要强制转换成封装类型
            saveConfig(context, "/mnt/sdcard/sconfig.properties", prop);
        }
        prop.put("bool", "no");// put方法可以直接修改配置信息，不会重复添加


        boolean b = !(((String) prop.get("bool")).equals("ture")) ;// get出来的都是Object对象
        // 如果是基本类型需要用到封装类

        Log.i(TAG, " config: " + b);

        String s = (String) prop.get("string");

        Log.i(TAG, " config: " + s);

        int i = Integer.parseInt((String) prop.get("int"));

        Log.i(TAG, " config: " + i);

        saveConfig(context, "/mnt/sdcard/sconfig.properties", prop);
    }


}
