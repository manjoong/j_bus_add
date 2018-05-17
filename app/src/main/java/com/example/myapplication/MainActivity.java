package com.example.myapplication;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
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
    public static EditText et_str;
    public static String destination;
    public static String kinds;
    public static String code;

    //오늘 날짜
    long now = System.currentTimeMillis();
    Date today = new Date(now);
    String TODAY;
    String SELECT_DAY;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

    Calendar calender = Calendar.getInstance();
    //날짜 설정
    final int DIALOG_DATE = 1;
    final int DIALOG_TIME = 2;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.recycler);

        et_des = findViewById(R.id.et_des);
        et_str = findViewById(R.id.et_str);
        et_des.setFocusable(false);
        et_des.setClickable(false);
        et_str.setFocusable(false);
        et_str.setClickable(false);
        final Button exp_button = (Button) findViewById(R.id.exp_btn) ;
        final Button sub_botton = (Button) findViewById(R.id.sub_btn) ;
        final Button trn_button = (Button) findViewById(R.id.trn_btn) ;
        ImageButton ok_button = (ImageButton) findViewById(R.id.enter) ;
        ImageButton date = (ImageButton)findViewById(R.id.date);

        //오늘 날짜를 알려줌
        TODAY  = sdf.format(today);

        Toast.makeText(getApplicationContext(),
                TODAY,
                Toast.LENGTH_SHORT).show();


        //달력 (날짜선택) 띄어주는 버튼
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                    showDialog(DIALOG_DATE); // 날짜 설정 다이얼로그 띄우기
            }
        });


        //검색버튼
        ok_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                destination = et_des.getText().toString();
//                Log.v("Debug : ", destination.toString());
//                Log.v("Debug : ", kinds.toString());
//                Log.v("Debug : ", code.toString());
                setRecyclerView();

                //날짜 선택을 안하면 오늘 날짜로
                if(SELECT_DAY == null){
                    Log.e("INPUT","SELECT_DAY가 NULL이여서 TODAY를 넣습니다.");
                    SELECT_DAY = TODAY;
                }

                //교통수단 종류를 구분
                if(kinds == "exp"){
                    exep_info();
                    Log.e("INPUT","교통종류: "+kinds+"\n"+"선택날짜: "+SELECT_DAY+"\n"+"목적지: " +destination);
                }else if (kinds == "sub"){
                    suburb_info();
                    Log.e("INPUT","교통종류: "+kinds+"\n"+"선택날짜: "+SELECT_DAY+"\n"+"목적지: " +destination);
                }else if (kinds == "trn"){
                    train_info();
                    Log.e("INPUT","교통종류: "+kinds+"\n"+"선택날짜: "+SELECT_DAY+"\n"+"목적지: " +destination);
                }

            }
        });

        exp_button.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(final View view) {
                kinds = "exp";
                setRecyclerView();
                exep_list();
                et_str.setText("전주 고속버스 터미널");
                exp_button.setBackgroundColor(Color.parseColor("#e56b32"));
                sub_botton.setBackgroundColor(Color.parseColor("#b3cc4d"));
                trn_button.setBackgroundColor(Color.parseColor("#b3cc4d"));


            }
        });

        sub_botton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                kinds = "sub";
                setRecyclerView();
                suburb_list();
                et_str.setText("전주 시외버스 터미널");
                exp_button.setBackgroundColor(Color.parseColor("#b3cc4d"));
                sub_botton.setBackgroundColor(Color.parseColor("#e56b32"));
                trn_button.setBackgroundColor(Color.parseColor("#b3cc4d"));
            }
        });

        trn_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                kinds = "trn";
                setRecyclerView();
                train_list();
                et_str.setText("전주역");
                exp_button.setBackgroundColor(Color.parseColor("#b3cc4d"));
                sub_botton.setBackgroundColor(Color.parseColor("#b3cc4d"));
                trn_button.setBackgroundColor(Color.parseColor("#e56b32"));

            }
        });




    }


    //리사이클 뷰 설정
    void setRecyclerView(){
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        //adapter = new CommunityTalkAdapter(getApplicationContext(), item);
        //recyclerView.setAdapter(adapter);
    }
    //고속버스 정보 제공 기능
    public void exep_info() {
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
//                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<ExpItem> call, Throwable t) {
                Log.d("에러다 짜샤왜~~",t.getMessage());
            }
        });
    }
    //시외버스 정보제공기능
    public void suburb_info() {
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
            }

            @Override
            public void onFailure(Call<SubItem> call, Throwable t) {
                Log.d("에러다 짜샤왜~~",t.getMessage());
            }
        });
    }
    //고속버스 정보 제공 기능
    public void train_info() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        APIservice retrofitService = retrofit.create(APIservice.class);

        Call<TrnItem> call = retrofitService.trn_info(code, TODAY);
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
            }

            @Override
            public void onFailure(Call<TrnItem> call, Throwable t) {
                Log.d("에러다 짜샤왜~~",t.getMessage());
            }
        });
    }

    //base url설정, 파싱 성공시 => 어댑터와 연결 실패시 => 처리
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

    //날짜를 선택한다 date picker
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

                                          SELECT_DAY = sdf.format(calender.getTime());


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

