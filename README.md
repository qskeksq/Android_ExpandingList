# ExpandingListView

- Retrofit2로 서버에서 데이터 받아오기
- 인터페이스를 통한 데이터 동기화
- ServiceGenerator, IRemoteService, RemoteLib
- SCROLL_STATE_IDLE&isLastData를 통해 추가로 데이터 세팅
- AbsListView & onScrollListener 이해

![]()

#### ServiceGenerator
```java
// 제네릭을 통해 서비스 생성 클래스 구현
public static <T> T createService(Class<T> retrofitInterface){
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(IRemoteService.FULL_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    return retrofit.create(retrofitInterface);
}
```

#### RemoteLib

```java
public Data getDatas(int start, int howMany, final TaskInterface taskInterface) {
    // ServiceGenerator를 통해 서비스 객체 생성
    final IRemoteService remote = ServiceGenerator.createService(IRemoteService.class);
    // 서버에서 Call<Data> 객체 받아옴
    Call<Data> call = remote.getMapData(start, howMany);
    // 동기화
    call.enqueue(new Callback<Data>() {
        @Override
        public void onResponse(Call<Data> call, Response<Data> response) {
            // 성공하면 response의 body에서 데이터 꺼내줌
            if (response.isSuccessful()) {
                data = response.body();
                // 인터페이스를 통해 데이터 세팅 동기화
                taskInterface.setData(data);
            } else {
                Log.e("[RemoteLib] getDatas", "unSuccessful");
            }
        }
        // 서버 통신 실패 or 타입 호환 실패
        @Override
        public void onFailure(Call<Data> call, Throwable t) {
            Log.e("[RemoteLib] getDatas", "onFailure");
        }
    });
    return data;
}
```

#### IRemote
```java
// 시작 값부터 몇 개 가져올 것인지 조사
@GET("{start}/{howMany}")
Call<Data> getMapData(@Path("start") int start, @Path("howMany") int howMany);
```

#### TaskInterface
```java
public interface TaskInterface {
    void setData(Data data);
}
```


#### ScrollListener

```java
/**
 * 스크롤 리스너
 */
AbsListView.OnScrollListener scrollListener = new AbsListView.OnScrollListener() {
    @Override
    public void onScrollStateChanged(AbsListView absListView, int state) {
        // 스크롤을 하다가 멈춤 & 마지막 아이템일 경우 추가로 데이터 set 해준다
        if (state == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && isLastItem) {
            start = start + datas.size();
            setData(start, start + offset);
        }
    }

    @Override
    public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        offset = visibleItemCount;
        if (firstVisibleItem + visibleItemCount >= totalItemCount) {
            isLastItem = true;
        } else {
            isLastItem = false;
        }
    }
};
```
#### 동기화 메소드

``` java
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
```

#### MapFragment

```java
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
```
