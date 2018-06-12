package com.example.myapplication;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import adapter.ExpAdapter;
import adapter.ExpListAdapter;
import adapter.SubAdapter;
import adapter.SubListAdapter;
import adapter.TrainListAdapter;
import adapter.TrnAdapter;
import item.ExpItem;
import item.ExpListItem;
import item.SubItem;
import item.SubListItem;
import item.TrnItem;
import item.TrnListItem;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import uril.CDialog;
import uril.ObjectUtils;

import static com.example.myapplication.APIservice.API_URL;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    ExpListAdapter exp_list_adapter;
    SubListAdapter sub_list_adapter;
    TrainListAdapter train_list_adapter;
    ExpAdapter exp_adapter;
    SubAdapter sub_adapter;
    TrnAdapter trn_adapter;
    public static EditText et_des;
    public static TextView tv_des;
    public static TextView tv_str;
    public static LinearLayout simple_box;
    public static LinearLayout object_list;
    public static LinearLayout sub_box;
    public static TextView select_day;
    public static TextView tv_time;
    public static TextView tv_charge;
    public static String destination;
    public static String kinds;
    public static String code;
    public static ImageButton ok_button;

    //오늘 날짜
    long now = System.currentTimeMillis();
    Date today = new Date(now);
    int check = 0;//날짜 선택후 다음날 체크할때 선택 날짜를 기억하기 위해
    Date dDate;
    String dayday;
    String TODAY_ORIGIN;
    String TODAY_NEW;
    String SELECT_DAY;
    String SELECT_DAY_NEW;
    SimpleDateFormat today_new = new SimpleDateFormat("yyyy년 MM월 dd일");
    SimpleDateFormat today_origin = new SimpleDateFormat("yyyyMMdd");


    Calendar calender = Calendar.getInstance();
    //날짜 설정
    final int DIALOG_DATE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        select_day = findViewById(R.id.select_day);
        ImageButton yesterday = (ImageButton) findViewById(R.id.yesterday);
        ImageButton tommorw = (ImageButton) findViewById(R.id.tommorw);
        final Button exp_button = (Button) findViewById(R.id.exp_btn) ;
        final Button sub_botton = (Button) findViewById(R.id.sub_btn) ;
        final Button trn_button = (Button) findViewById(R.id.trn_btn) ;
        simple_box = findViewById(R.id.simple_box);
        object_list = findViewById(R.id.object_list);
        sub_box = findViewById(R.id.sub_box);
        tv_str = findViewById(R.id.tv_str);
        tv_des = findViewById(R.id.tv_des);
        et_des = findViewById(R.id.et_des);
        tv_time = findViewById(R.id.tv_time);
        tv_charge = findViewById(R.id.tv_charge);
        ok_button = (ImageButton) findViewById(R.id.enter);

//////////////////////////////////////////////////////////////초기 kinds (없앨수도 있음)/////////////////////////////////////////////////////////////////////////////
        et_des.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout.LayoutParams params
                        = (LinearLayout.LayoutParams) MainActivity.object_list.getLayoutParams();
                params.weight = 10;
                MainActivity.object_list.setLayoutParams(params);
                Log.e("알림: ", "에디트 텍스트를 선택하였습니다.");
                if(kinds == null){
                    kinds = "exp";
                    Log.e("알림: ", "kinds(교통수단 종류) 가 선택되지 않아 exp로 선택합니다.");
                }
                if(kinds == "exp"){
                    exp_button.callOnClick();
                }else if (kinds == "sub"){
                    sub_botton.callOnClick();
                }else if (kinds == "trn"){
                    trn_button.callOnClick();
                }
            }
        });
//////////////////////////////////////////////////////////////텍스트 박스 수정할 때마다 리스트뷰 수정/////////////////////////////////////////////////////////////////////////////
        //검색창
        //
        et_des.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable arg0) {
                Log.e("afterTextChanged", et_des.getText().toString());

                String text = et_des.getText().toString()
                        .toLowerCase(Locale.getDefault());

                if(kinds == "exp") {
                    Log.e("kinds: " , kinds);
                    Log.e("text: " , text);
                    exp_list_adapter.filter(text);
                }else if(kinds == "trn") {
                    Log.e("kinds: " , kinds);
                    Log.e("text: " , text);
                    train_list_adapter.filter(text);
                }else if(kinds == "sub") {
                    Log.e("kinds: " , kinds);
                    Log.e("text: " , text);
                    sub_list_adapter.filter(text);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
                Log.e("beforeTextChanged", et_des.getText().toString());
                String leng = et_des.getText().toString();

                if(leng.length() == 0) {
                    et_des.callOnClick();
                }
            }

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {
                if(kinds == null){
                    et_des.callOnClick();
                }
                Log.e("onTextChanged", et_des.getText().toString());
                // TODO Auto-generated method stub
            }
        });
//////////////////////////////////////////////////////////////오늘 날짜 구하기/////////////////////////////////////////////////////////////////////////////
        if(TODAY_NEW == null && TODAY_ORIGIN == null) {
            TODAY_ORIGIN = today_origin.format(today);
            TODAY_NEW = today_new.format(today);
        }

        //요일 구하기
        try {
            dayday =getDateDay(TODAY_ORIGIN, "yyyyMMdd");
            Log.e("SUCCES","성공하였다.");
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("ERROR","실패하였다.");
        }

        select_day.setText(TODAY_NEW + " " + "(" + dayday + ")" );
//////////////////////////////////////////////////////////////시작하며 뜨는 페이지/////////////////////////////////////////////////////////////////////////////
        Toast.makeText(getApplicationContext(),
                TODAY_NEW + dayday,
                Toast.LENGTH_SHORT).show();
//////////////////////////////////////////////////////////////날짜 검색창 클릭시/////////////////////////////////////////////////////////////////////////////
        select_day.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DIALOG_DATE);
            }
        });

/////////////////////////////////////////////////////////////다음 날짜 버튼 클릭시/////////////////////////////////////////////////////////////////////////////
        tommorw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //내일 어제 날짜 구하기\

                if(SELECT_DAY !=null && check == 0) {
                    today = calender.getTime();
                    Log.e("EMPTY", "달력에서 선택한 날이 있이 있어 거기서 더합니다." + check);
                    check++;
                }
                long lCurTime = today.getTime();
                today= new Date(lCurTime+(1000*60*60*24*+1));
                TODAY_NEW = today_new.format(today);
                TODAY_ORIGIN = today_origin.format(today);
                SELECT_DAY = TODAY_ORIGIN;

                //요일 구하기
                try {
                    dayday =getDateDay(TODAY_ORIGIN, "yyyyMMdd");
                    Log.e("SUCCES","성공하였다." +  TODAY_ORIGIN + " " + today + "  " +calender.getTime());
                    Toast.makeText(getApplicationContext(),
                            SELECT_DAY,
                            Toast.LENGTH_SHORT).show();

                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("ERROR","실패하였다.");
                }
                select_day.setText(TODAY_NEW + " " + "(" + dayday + ")" );
            }
        });
/////////////////////////////////////////////////////////////이전 날짜 버튼 클릭시/////////////////////////////////////////////////////////////////////////////
        yesterday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //내일 어제 날짜 구하기\

                if(SELECT_DAY !=null && check == 0) {
                    today = calender.getTime();
                    Log.e("EMPTY", "달력에서 선택한 날이 있이 있어 거기서 더합니다." + check);
                    check++;
                }
                long lCurTime = today.getTime();
                today= new Date(lCurTime+(1000*60*60*24*-1));
                TODAY_NEW = today_new.format(today);
                TODAY_ORIGIN = today_origin.format(today);
                SELECT_DAY = TODAY_ORIGIN;

                //요일 구하기
                try {
                    dayday =getDateDay(TODAY_ORIGIN, "yyyyMMdd");
                    Log.e("SUCCES","성공하였다." +  TODAY_ORIGIN + " " + today + "  " +calender.getTime());
                    Toast.makeText(getApplicationContext(),
                            SELECT_DAY,
                            Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("ERROR","실패하였다.");
                }
                select_day.setText(TODAY_NEW + " " + "(" + dayday + ")" );
            }
        });


////////////////////////////////////////////////////검색버튼 클릭/////////////////////////////////////////////////////////////////////////////////////
        ok_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                if(kinds != "sub") {
                    destination = et_des.getText().toString();
                }
//                Log.v("Debug : ", destination.toString());
//                Log.v("Debug : ", kinds.toString());
//                Log.v("Debug : ", code.toString());

                setRecyclerView();
                simple_box.setVisibility(View.VISIBLE);

                //날짜 선택을 안하면 오늘 날짜로
                if(SELECT_DAY == null){
                    Log.e("INPUT","SELECT_DAY가 NULL이여서 TODAY를 넣습니다.");
                    SELECT_DAY = TODAY_ORIGIN;
                }
//////////////////////////////////////////////////////교통 종류/////////////////////////////////////////////////////////////////////////////////////
                if(kinds == "exp"){
                    exep_info();
                    Log.e("INPUT","교통종류: "+kinds+"\n"+"선택날짜: "+SELECT_DAY+"\n"+"목적지: " +destination + "\n" + "코드 or 메소드: " + code);
                }else if (kinds == "sub"){
                    suburb_info();
                    Log.e("INPUT","교통종류: "+kinds+"\n"+"선택날짜: "+SELECT_DAY+"\n"+"목적지: " +destination+ "\n" + "코드 or 메소드: " + destination);
                }else if (kinds == "trn"){
                    train_info();
                    Log.e("INPUT","교통종류: "+kinds+"\n"+"선택날짜: "+SELECT_DAY+"\n"+"목적지: " +destination+ "\n" + "코드 or 메소드: " + code);
                }
            }
        });
//////////////////////////////////////////////////////고속 클릭시 /////////////////////////////////////////////////////////////////////////////////////
        exp_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                setRecyclerView();
                exep_list();
                LinearLayout.LayoutParams params
                        = (LinearLayout.LayoutParams) MainActivity.object_list.getLayoutParams();
                params.weight = 10;
                MainActivity.object_list.setLayoutParams(params);
                simple_box.setVisibility(View.GONE);
                kinds = "exp";
                exp_button.setBackgroundColor(Color.parseColor("#8FA91E"));
                exp_button.setTextColor(Color.parseColor("#FFFFFF"));
                sub_botton.setBackgroundColor(Color.parseColor("#D5EA7B"));
                sub_botton.setTextColor(Color.parseColor("#8FA91E"));
                trn_button.setBackgroundColor(Color.parseColor("#D5EA7B"));
                trn_button.setTextColor(Color.parseColor("#8FA91E"));
            }
        });
//////////////////////////////////////////////////////시외 클릭시 /////////////////////////////////////////////////////////////////////////////////////
        sub_botton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                kinds = "sub";
                setRecyclerView();
                suburb_list();
                LinearLayout.LayoutParams params
                        = (LinearLayout.LayoutParams) MainActivity.object_list.getLayoutParams();
                params.weight = 10;
                MainActivity.object_list.setLayoutParams(params);
                simple_box.setVisibility(View.GONE);

                exp_button.setBackgroundColor(Color.parseColor("#D5EA7B"));
                exp_button.setTextColor(Color.parseColor("#8FA91E"));
                sub_botton.setBackgroundColor(Color.parseColor("#8FA91E"));
                sub_botton.setTextColor(Color.parseColor("#FFFFFF"));
                trn_button.setBackgroundColor(Color.parseColor("#D5EA7B"));
                trn_button.setTextColor(Color.parseColor("#8FA91E"));

            }
        });
//////////////////////////////////////////////////////기차 클릭시 /////////////////////////////////////////////////////////////////////////////////////
        trn_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                kinds = "trn";
                setRecyclerView();
                train_list();
                LinearLayout.LayoutParams params
                        = (LinearLayout.LayoutParams) MainActivity.object_list.getLayoutParams();
                params.weight = 10;
                MainActivity.object_list.setLayoutParams(params);
                simple_box.setVisibility(View.GONE);

                exp_button.setBackgroundColor(Color.parseColor("#D5EA7B"));
                exp_button.setTextColor(Color.parseColor("#8FA91E"));
                sub_botton.setBackgroundColor(Color.parseColor("#D5EA7B"));
                sub_botton.setTextColor(Color.parseColor("#8FA91E"));
                trn_button.setBackgroundColor(Color.parseColor("#8FA91E"));
                trn_button.setTextColor(Color.parseColor("#FFFFFF"));
            }
        });
}
///////////////////////////////////////////////////////////리사이클 뷰 /////////////////////////////////////////////////////////////////////////////////////
    void setRecyclerView(){
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        //adapter = new CommunityTalkAdapter(getApplicationContext(), item);
        //recyclerView.setAdapter(adapter);
    }

//////////////////////////////////////////////////////고속 정보 /////////////////////////////////////////////////////////////////////////////////////
    public void exep_info() {
        CDialog.showLoading(this);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        APIservice retrofitService = retrofit.create(APIservice.class);
        Call<ExpItem> call = retrofitService.exp_info(code, SELECT_DAY);
        call.enqueue(new Callback<ExpItem>() {

            @Override
            public void onResponse(Call<ExpItem> call, Response<ExpItem> response) {
//                DebugLog.v(response.body().getResults().toString());
                //Log.v("Debug : ", response.body().getData().toString());
                //이렇게 어댑터에 데이터를 보내주고
                exp_adapter = new ExpAdapter(MainActivity.this, response.body().getResults()); //appcompaty에선 context로 해줘야됨
//                textViewIndex.setText(response.body().getData().get(0).getName().toString());
                //어댑터를 연결시키면 끝이다.
                recyclerView.setAdapter(exp_adapter);
                CDialog.hideLoading();
                if(response.body().getResults().size() == 0) {
                    Toast.makeText(getApplicationContext(), "해당 날짜는 아직 제공되지 않습니다.", Toast.LENGTH_SHORT).show();
                }
//                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<ExpItem> call, Throwable t) {
                CDialog.hideLoading();
                Log.d("에러다 짜샤왜~~",t.getMessage());
            }
        });
    }
//////////////////////////////////////////////////////시외 정보 /////////////////////////////////////////////////////////////////////////////////////
    public void suburb_info() {
        CDialog.showLoading(this);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        APIservice retrofitService = retrofit.create(APIservice.class);
        Call<SubItem> call = retrofitService.sub_info(destination.toString());
        call.enqueue(new Callback<SubItem>() {

            @Override
            public void onResponse(Call<SubItem> call, Response<SubItem> response) {
//                DebugLog.v(response.body().getResults().toString());
                //Log.v("Debug : ", response.body().getData().toString());
                //이렇게 어댑터에 데이터를 보내주고
                sub_adapter = new SubAdapter(MainActivity.this, response.body().getBody()); //appcompaty에선 context로 해줘야됨
//                textViewIndex.setText(response.body().getData().get(0).getName().toString());
                //어댑터를 연결시키면 끝이다.
                recyclerView.setAdapter(sub_adapter);
//                adapter.notifyDataSetChanged();
                CDialog.hideLoading();
                if(!ObjectUtils.isEmpty(response.body().getBody())) {
                    if(response.body().getBody().size() == 0) {
                        Toast.makeText(getApplicationContext(), "해당 날짜는 아직 제공되지 않습니다.", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<SubItem> call, Throwable t) {
                CDialog.hideLoading();
                Log.d("에러다 짜샤왜~~",t.getMessage());
            }
        });
    }
//////////////////////////////////////////////////////기차 정보 /////////////////////////////////////////////////////////////////////////////////////
    public void train_info() {
        CDialog.showLoading(this);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        APIservice retrofitService = retrofit.create(APIservice.class);

        Call<TrnItem> call = retrofitService.trn_info(code, SELECT_DAY);
        call.enqueue(new Callback<TrnItem>() {

            @Override
            public void onResponse(Call<TrnItem> call, Response<TrnItem> response) {
//                DebugLog.v(response.body().getResults().toString());
                //Log.v("Debug : ", response.body().getData().toString());
                //이렇게 어댑터에 데이터를 보내주고
                trn_adapter = new TrnAdapter(MainActivity.this, response.body().getResults()); //appcompaty에선 context로 해줘야됨
//                textViewIndex.setText(response.body().getData().get(0).getName().toString());
                //어댑터를 연결시키면 끝이다.
                recyclerView.setAdapter(trn_adapter);
//                adapter.notifyDataSetChanged();
                CDialog.hideLoading();
                if(response.body().getResults().size() == 0) {
                    Toast.makeText(getApplicationContext(), "해당 날짜는 아직 제공되지 않습니다.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<TrnItem> call, Throwable t) {
                CDialog.hideLoading();
                Log.d("에러다 짜샤왜~~",t.getMessage());
            }
        });
    }
//////////////////////////////////////////////////////고속 리스트/////////////////////////////////////////////////////////////////////////////////////
    public void exep_list() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        APIservice retrofitService = retrofit.create(APIservice.class);
        Call<ExpListItem> call = retrofitService.exep();
        call.enqueue(new Callback<ExpListItem>() {

            @Override
            public void onResponse(Call<ExpListItem> call, Response<ExpListItem> response) {
//                DebugLog.v(response.body().getResults().toString());
                //Log.v("Debug : ", response.body().getData().toString());
                //이렇게 어댑터에 데이터를 보내주고
                exp_list_adapter = new ExpListAdapter(MainActivity.this, response.body().getResults()); //appcompaty에선 context로 해줘야됨
//                textViewIndex.setText(response.body().getData().get(0).getName().toString());
                //어댑터를 연결시키면 끝이다.
                recyclerView.setAdapter(exp_list_adapter);
//                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<ExpListItem> call, Throwable t) {
                Log.d("에러다 짜샤~~",t.getMessage());
            }
        });
    }
//////////////////////////////////////////////////////시외 리스트/////////////////////////////////////////////////////////////////////////////////////
    public void suburb_list() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        APIservice retrofitService = retrofit.create(APIservice.class);
        Call<SubListItem> call = retrofitService.suburb();
        call.enqueue(new Callback<SubListItem>() {

            @Override
            public void onResponse(Call<SubListItem> call, Response<SubListItem> response) {
//                DebugLog.v(response.body().getResults().toString());
                //Log.v("Debug : ", response.body().getData().toString());
                //이렇게 어댑터에 데이터를 보내주고
                sub_list_adapter = new SubListAdapter(MainActivity.this, response.body().getBody()); //appcompaty에선 context로 해줘야됨
//                textViewIndex.setText(response.body().getData().get(0).getName().toString());
                //어댑터를 연결시키면 끝이다.
                recyclerView.setAdapter(sub_list_adapter);
//                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<SubListItem> call, Throwable t) {
                Log.d("에러다 짜샤왜~~",t.getMessage());
            }
        });
    }
//////////////////////////////////////////////////////기차 리스트/////////////////////////////////////////////////////////////////////////////////////
    public void train_list() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        APIservice retrofitService = retrofit.create(APIservice.class);
        Call<TrnListItem> call = retrofitService.train();
        call.enqueue(new Callback<TrnListItem>() {

            @Override
            public void onResponse(Call<TrnListItem> call, Response<TrnListItem> response) {
//                DebugLog.v(response.body().getResults().toString());
                //Log.v("Debug : ", response.body().getData().toString());
                //이렇게 어댑터에 데이터를 보내주고
                train_list_adapter = new TrainListAdapter(MainActivity.this, response.body().getBody()); //appcompaty에선 context로 해줘야됨
//                textViewIndex.setText(response.body().getData().get(0).getName().toString());
                //어댑터를 연결시키면 끝이다.
                recyclerView.setAdapter(train_list_adapter);
//                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<TrnListItem> call, Throwable t) {
                Log.d("에러다 짜샤왜~~",t.getMessage());
            }
        });
    }
//////////////////////////////////////////////////////달력/////////////////////////////////////////////////////////////////////////////////////
    @Override
    @Deprecated
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DIALOG_DATE:
                DatePickerDialog dpd = new DatePickerDialog
                        (MainActivity.this, // 현재화면의 제어권자
                                new DatePickerDialog.OnDateSetListener() {
                                    public void onDateSet(DatePicker view,
                                                          int year, int monthOfYear, int dayOfMonth) {
                                        calender.set(year, monthOfYear, dayOfMonth);

                                          SELECT_DAY = today_origin.format(calender.getTime()); //SELECT_DAT 는 yyyyMMdd형식
                                          SELECT_DAY_NEW = today_new.format(calender.getTime()); // SELECT_DAY_NEW 는 yyyy년 MM월 dd일 형식
                                        check =0;

                                        //요일 구하기
                                        try {
                                            dayday =getDateDay(SELECT_DAY, "yyyyMMdd");
                                            Log.e("SUCCES","성공하였다.");
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            Log.e("ERROR","실패하였다.");
                                        }

                                          select_day.setText(SELECT_DAY_NEW + " " + "(" + dayday + ")");

                                        Toast.makeText(getApplicationContext(),
                                                SELECT_DAY,
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                                , // 사용자가 날짜설정 후 다이얼로그 빠져나올때
                                //    호출할 리스너 등록
                                calender.get(calender.YEAR), calender.get(calender.MONTH), calender.get(calender.DATE)); // 기본값 연월일
                return dpd;
        }
        return super.onCreateDialog(id);
    }

//////////////////////////////////////////////////////요일 구하는 함수/////////////////////////////////////////////////////////////////////////////////////
    public static String getDateDay(String date, String dateType) throws Exception {

        String day = "";

        SimpleDateFormat dateFormat = new SimpleDateFormat(dateType);
        Date nDate = dateFormat.parse(date);

        Calendar cal = Calendar.getInstance();
        cal.setTime(nDate);

        int dayNum = cal.get(Calendar.DAY_OF_WEEK);

        switch (dayNum) {
            case 1:
                day = "일";
                break;
            case 2:
                day = "월";
                break;
            case 3:
                day = "화";
                break;
            case 4:
                day = "수";
                break;
            case 5:
                day = "목";
                break;
            case 6:
                day = "금";
                break;
            case 7:
                day = "토";
                break;
        }
        return day;
    }
//////////////////////////////////////////////////////안드로이드 화면 즉시 변환/////////////////////////////////////////////////////////////////////////////////////
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0 || requestCode == 1002) {
            if(resultCode == RESULT_OK){
                suburb_list();
                exep_list();
                train_list();
            }
        }
    }//onActivityResult

}

