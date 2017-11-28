package com.example.tll.map;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public LocationClient mLocationClient;

    private TextView positionText;

    private MapView mapView;

    private BaiduMap baiduMap;

    private boolean isFirstLocate = true;

    protected static final String TAG="ReceiveLoc";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLocationClient = new LocationClient(getApplicationContext());
        mLocationClient.registerLocationListener(new MyLocationListener());
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mapView = (MapView) findViewById(R.id.bmapView);
        baiduMap = mapView.getMap();
        baiduMap.setMyLocationEnabled(true);
        positionText = (TextView) findViewById(R.id.position_text_view);
        List<String> permissionList = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.READ_PHONE_STATE);
        }
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!permissionList.isEmpty()) {
            String [] permissions = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(MainActivity.this, permissions, 1);
        } else {
            requestLocation();
        }
    }

    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.toolbar,menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.settings:
                Toast.makeText(this, "setting", Toast.LENGTH_LONG).show();
                Intent intent1 = new Intent(MainActivity.this,ThirdActivity.class);
                startActivity(intent1);
                break;
            case R.id.more:
                Toast.makeText(this, "......", Toast.LENGTH_LONG).show();
                break;
            default:
        }
        return true;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0) {
                    for (int result : grantResults) {
                        if (result != PackageManager.PERMISSION_GRANTED) {
                            Toast.makeText(this, "必须同意所有权限才能使用本程序", Toast.LENGTH_SHORT).show();
                            finish();
                            return;
                        }
                    }
                    requestLocation();
                } else {
                    Toast.makeText(this, "发生未知错误", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            default:
        }
    }

    private void setMarker(double lat,double lon) {
        //定义Maker坐标点
        LatLng point = new LatLng(lat, lon);

        //LatLng latLng = null;  
        //OverlayOptions overlayOptions = null;  
        Marker marker = null;

        //构建Marker图标
        final BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.drawable.ic_launcher_round);
        //构建MarkerOption，用于在地图上添加Marker
        OverlayOptions option = new MarkerOptions().position(point).icon(bitmap);
        //在地图上添加Marker，并显示
        marker = (Marker) (baiduMap.addOverlay(option));


        baiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(final Marker marker) {
                //获得marker中的数据
                //Info info = (Info) marker.getExtraInfo().get("info");

                InfoWindow mInfoWindow;

                //生成一个TextView用户在地图中显示InfoWindow
                //TextView location = new TextView(getApplicationContext());
                //location.setBackgroundResource(R.drawable.);
                //location.setPadding(30, 20, 30, 50);
                //location.setText(" Raspberry: " + rasploc);

                LayoutInflater inflater =  LayoutInflater.from(getApplicationContext());
                View layout = inflater.inflate(R.layout.appwidget_provider,null);

                showgetdata( 17, 87, 0,"time","loc","",layout);

                //layout.setMinimumWidth(1000);
                //layout.setMinimumHeight(300);
                //转换为Bitmap对象
                BitmapDescriptor bttmap = BitmapDescriptorFactory.fromView(layout);




                //将marker所在的经纬度的信息转化成屏幕上的坐标
                final LatLng ll = marker.getPosition();

                LatLng llInfo = baiduMap.getProjection().fromScreenLocation(baiduMap.getProjection().toScreenLocation(ll));
                //为弹出的InfoWindow添加点击事件


                InfoWindow.OnInfoWindowClickListener infoWindowClicklistener = new InfoWindow.OnInfoWindowClickListener() {
                    @Override
                    public void onInfoWindowClick() {
                        //隐藏InfoWindow
                        baiduMap.hideInfoWindow();

                        Intent intent = new Intent();
                        intent.setClass(MainActivity.this,SecondActivity.class);
                        startActivity(intent);
                    }
                };

                mInfoWindow = new InfoWindow(bttmap, llInfo, -47,infoWindowClicklistener);
                //显示InfoWindow
                baiduMap.showInfoWindow(mInfoWindow);

                return true;
            }
        });
    }

    public void showgetdata( int temperature, int humidity, int pm_value,String date_time ,String rasploct ,String weather_type, final View layout)
    {
        TextView temp = (TextView)layout.findViewById(R.id.textView_widget_temp);
        temp.setText(temperature + "℃");

        TextView humi = (TextView)layout.findViewById(R.id.textView_widget_humi);
        humi.setText(humidity + "%");

        TextView pm = (TextView)layout.findViewById(R.id.textView_widget_pm);
        pm.setText(pm_value + "");

        TextView loc = (TextView)layout.findViewById(R.id.textView_widget_location);
        loc.setText(rasploct);

        TextView time = (TextView)layout.findViewById(R.id.textView_widget_time);
        time.setText(date_time);

        ImageView weather = (ImageView)layout.findViewById(R.id.imageView_widget_weather_ic1);

        Resources res=getResources();

        Bitmap bmp= BitmapFactory.decodeResource(res, R.drawable.icon_duoyun);

        weather.setImageBitmap(bmp);

    }

    private void navigateTo(BDLocation location) {
        if (isFirstLocate) {
            Toast.makeText(this, "当前位置 " + location.getAddrStr(), Toast.LENGTH_SHORT).show();
            LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
            MapStatusUpdate update = MapStatusUpdateFactory.newLatLngZoom(ll, 16f);
            baiduMap.animateMapStatus(update);
            isFirstLocate = false;
        }
        setMarker(30.6373,104.0917);
        MyLocationData.Builder locationBuilder = new MyLocationData.Builder();
        locationBuilder.latitude(location.getLatitude());
        locationBuilder.longitude(location.getLongitude());
        MyLocationData locationData = locationBuilder.build();
        baiduMap.setMyLocationData(locationData);
        baiduMap.setMyLocationEnabled(true);
    }

    private void requestLocation() {
        initLocation();
        mLocationClient.start();
    }

    private void initLocation(){
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);// 高精度
        option.setCoorType("bd09ll");// 返回的定位结果是百度经纬度
        option.setIsNeedAddress(true);// 位置
        option.setOpenGps(true);
        //option.setLocationNotify(true);//设置是否当GPS有效时按照1S/1次频率输出GPS结果
        option.setNeedDeviceDirect(true);// 返回的定位结果包含手机机头的方向
        option.setScanSpan(5000);// 多长时间进行一次请求
        mLocationClient.setLocOption(option);// 使用设置

    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLocationClient.stop();
        mapView.onDestroy();
        baiduMap.setMyLocationEnabled(false);
    }



    public class MyLocationListener implements BDLocationListener {
        double latitude;
        double longitude;
        float radius;

        @Override
        public void onReceiveLocation(BDLocation location) {

            Log.i(TAG, " latitude: " + latitude);

            Log.i(TAG," longitude: " + longitude + location.getAddrStr());

            String rasploc = location.getAddrStr();

            latitude = location.getLatitude();    //获取纬度信息
            longitude = location.getLongitude();    //获取经度信息
            radius = location.getRadius();    //获取定位精度，默认值为0.0f

            System.out.println(latitude);
            System.out.println(longitude);
            System.out.println(radius);
//            StringBuilder currentPosition = new StringBuilder();
//            currentPosition.append("纬度：").append(location.getLatitude()).append("\n");
//            currentPosition.append("经线：").append(location.getLongitude()).append("\n");
//            currentPosition.append("国家：").append(location.getCountry()).append("\n");
//            currentPosition.append("省：").append(location.getProvince()).append("\n");
//            currentPosition.append("市：").append(location.getCity()).append("\n");
//            currentPosition.append("区：").append(location.getDistrict()).append("\n");
//            currentPosition.append("街道：").append(location.getStreet()).append("\n");
//            currentPosition.append("定位方式：");
//            if (location.getLocType() == BDLocation.TypeGpsLocation) {
//                currentPosition.append("GPS");
//            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {
//                currentPosition.append("网络");
//            }
//            positionText.setText(currentPosition);

            if (location.getLocType() == BDLocation.TypeGpsLocation
                    || location.getLocType() == BDLocation.TypeNetWorkLocation) {
                navigateTo(location);
            }
        }

    }


}
