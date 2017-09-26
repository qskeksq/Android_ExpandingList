package com.example.administrator.expandingrecycler;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.administrator.expandingrecycler.domain.Data;
import com.example.administrator.expandingrecycler.domain.Row;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, TaskInterface {


    private TextView textView;
    private ListView listView;
    private ArrayAdapter<String> adapter;
    private List<String> datas;
    private Data data;
    private Row[] rows;
    private boolean isLastItem;
    private GoogleMap gooleMap;

    int start = 0;
    int offset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 초기 데이터 세팅
        setData(start, 10);
        // 초기화
        initView();
        initList();
        initMap();
    }

    /**
     * 비동기로 데이터 출력
     *
     * @param start   보여줄 시작점
     * @param howMany 시작으로부터 보여줄 개수
     */
    private void setData(int start, int howMany) {
        // 인터페이스를 통해 데이터 세팅 동기화
        RemoteLib.getInstance().getDatas(start, howMany, this);
    }

    /**
     * 뷰 세팅
     */
    private void initView() {
        textView = (TextView) findViewById(R.id.textView);
        listView = (ListView) findViewById(R.id.listView);
    }

    /**
     * 리스트뷰 세팅
     */
    private void initList() {
        datas = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, datas);
        listView.setAdapter(adapter);
        listView.setOnScrollListener(scrollListener);
    }

    /**
     * 스크롤 리스너
     */
    AbsListView.OnScrollListener scrollListener = new AbsListView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(AbsListView absListView, int state) {
//             손가락을 휙 날렸을 떄
//             Log.e("SCROLL_STATE_FLING", SCROLL_STATE_FLING+"");
//             멈췄을 때
//             Log.e("SCROLL_STATE_IDLE", SCROLL_STATE_IDLE+"");
//             움직일 때
//             Log.e("SCROLL_STATE_TOUCH", SCROLL_STATE_TOUCH_SCROLL+"");
            // 스크롤을 하다가 멈춤 & 마지막 아이템일 경우 추가로 데이터 set 해준다
            if (state == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && isLastItem) {
                start = start + datas.size();
                setData(start, start + offset);
            }
        }

        @Override
        public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
//            0부터 처음 보이는 아이템 위치
//            Log.e("처음 보이는 아이템", firstVisibleItem+"");
//            아주 살짝 걸치기만 해도 눈에 보이는 개수
//            Log.e("눈에 보이는 개수", visibleItemCount+"");
//            처음부터 끝까지 전체 개수
//            Log.e("전체 아이템 개수", totalItemCount+"");
//            Log.e("마지막으로 보이는 아이템", absListView.getLastVisiblePosition()+"");
            offset = visibleItemCount;
            if (firstVisibleItem + visibleItemCount >= totalItemCount) {
                isLastItem = true;
            } else {
                isLastItem = false;
            }
        }
    };

    /**
     * 맵뷰 세팅
     */
    private void initMap() {
        FragmentManager fm = getSupportFragmentManager();
        SupportMapFragment fragment = (SupportMapFragment) fm.findFragmentById(R.id.mapView);
        if (fragment == null) {
            fragment = new SupportMapFragment().newInstance();
        }
        fragment.getMapAsync(this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.gooleMap = googleMap;
        LatLng seoul = new LatLng(37.566229, 126.977689);
        CameraPosition cp = new CameraPosition.Builder().target(seoul).zoom(10).build();
        googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cp));
        addMarker(seoul);
    }

    /**
     * 동기로 데이터 세팅하기 위한 인터페이스 메소드
     *
     * @param data 레트로핏으로 서버에서 전송 완료된 후 넘겨받은 데이터
     */
    @Override
    public void setData(Data data) {
        this.data = data;
        rows = data.getGeoInfoPublicToiletWGS().getRow();
        for (Row row : rows) {
            datas.add(row.getHNR_NAM());
            addMarker(new LatLng(row.getLAT(), row.getLNG()));
        }
        adapter.notifyDataSetChanged();
    }

    private void addMarker(LatLng position){
        MarkerOptions options = new MarkerOptions();
        options.position(position);
        gooleMap.addMarker(options);
    }

}
