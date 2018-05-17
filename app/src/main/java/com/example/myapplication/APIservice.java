package com.example.myapplication;



import item.ExpItem;
import item.ExpListItem;
import item.SubItem;
import item.SubListItem;
import item.TrnItem;
import item.TrnListItem;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by 마루소프트 on 2018-05-04.
 */

public interface APIservice {

    //public String IMG_URL = I_URL + "image/";
    public static final String API_URL = "http://1.255.51.219/api/jbus/exp_sub_businfo/";




    @GET("exep_bus_list.php") // 고속버스 도착지목록
    Call<ExpListItem> exep();

    @GET("suburb_bus_list.php") // 시외버스 도착지 목록
    Call<SubListItem> suburb();

    @GET("train_list.php") // 기차 도착지 목록
    Call<TrnListItem> train();



        @GET("exep_bus.php") //고속버스 정보
        Call<ExpItem> exp_info(
                @Query("terminalId") String id,
                @Query("date") String date
        );

        @GET("suburb_bus.php") //고속버스 정보
        Call<SubItem> sub_info(
                @Query("tername") String name);

        @GET("train_info.php") //고속버스 정보
        Call<TrnItem> trn_info(
                @Query("arrivecode") String id,
                @Query("date") String date
        );
//
//
//    @FormUrlEncoded
//    @POST("insert.php")
//    Call<Talk_CallBackItem> writeTalk(@Field("num") Integer num, @Field("name") String name, @Field("password") String password, @Field("content") String content);





//
//    @FormUrlEncoded //게시글
//    @POST("insert_new.php") //insert_php에 대한 리턴값을 받는 post_cllback item.   밑에 변수들은 insert_new에서만 신경쓰면 됨
//    Call<Post_CallBackItem> writeTalk(@Field("t_user_id") String t_user_id, @Field("t_pwd") String t_pwd,  @Field("t_title") String t_title, @Field("t_content") String t_content, @Field("t_like") int t_like);
//
//    @FormUrlEncoded //좋아요 기능
//    @POST("like_update.php") //insert_php에 대한 리턴값을 받는 post_cllback item.   밑에 변수들은 insert_new에서만 신경쓰면 됨
//    Call<Post_CallBackItem> updateLike(@Field("t_no") int t_no, @Field("t_like") int t_like);
//
//    @FormUrlEncoded //댓글 입력
//    @POST("insert_reply.php")
//    Call<Post_CallBackItem> writeReply(@Field("r_t_no") int r_t_no, @Field("r_user_id") String r_user_id,  @Field("r_content") String r_content);
//
//    @FormUrlEncoded //게시글 삭제
//    @POST("delete_talk.php") //insert_php에 대한 리턴값을 받는 post_cllback item.   밑에 변수들은 insert_new에서만 신경쓰면 됨
//    Call<Post_CallBackItem> deleteTalk(@Field("t_no") int t_no, @Field("input_pwd") String input_pwd);

//    @Multipart
//    @POST("insert.php")
//    Call<Talk_CallBackItem> writeTalk(
//            @Part("num") RequestBody num,
//            @Part("name") RequestBody name,
//            @Part("password") RequestBody password,
//            @Part("content") RequestBody content);
}