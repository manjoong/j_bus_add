package adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.myapplication.MainActivity;
import com.example.myapplication.R;

import java.util.ArrayList;

import item.ExpItem;
import item.SubItem;

/**
 * Created by 마루소프트 on 2018-02-01.
 */

//start, result 값비교 갱신


public class SubAdapter extends RecyclerView.Adapter<SubAdapter.ViewHolder> {
    Activity context;
    ArrayList<SubItem.Data> items;

    //커뮤니티 토크 어댑터 함수를 만듬으로써 context와 item을 상속해 준다.
    public SubAdapter(Activity context, ArrayList<SubItem.Data> items) {
        this.context = context;
        this.items = items;
    }

    //in create view holder는 뷰홀더에 넣어줄 layout을 찾는 애다.
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.sub_info_item,parent,false);
        return new ViewHolder(view);
    }
    //온바인드뷰홀더는 아이템을 세팅하거나 스크롤링 할때 호출되는 애다. 때문에 position이 필요하다.
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.str_name.setText("전주");
        holder.des_name.setText(MainActivity.destination.toString());
        holder.str_time.setText(items.get(position).getStarttime());
        holder.charge.setText(items.get(position).getCharge());
        holder.waypoint.setText(items.get(position).getWaypoint());
        holder.along_time.setText(items.get(position).getAlongtime());

//        holder.like.setText(String.valueOf(items.get(position).getT_like())); //int 값일때
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    //뷰홀더라는 애는 아이템안에 들어갈 텍스트등의 내용을 초기화 하는 역할이다.
    class ViewHolder extends RecyclerView.ViewHolder{
        TextView str_name;
        TextView des_name;
        TextView str_time;
        TextView along_time;
        TextView charge;
        TextView waypoint;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    System.out.println(getPosition());
                    //어댑터에서는 this를 쓸 수 없으므로 context를 쓴다. context는 이 레이아웃의 변수들?
//                    Intent intent = new Intent(context , DetailActivity.class);
                    //변수를 해당 activity로 넘긴다.
//                    intent.putExtra("exp_ter_name", items.get(getAdapterPosition()).getTerminalName());
//                    intent.putExtra("exp_ter_id", items.get(getAdapterPosition()).getTerminalId());
//                    context.startActivityForResult(intent, 1002);
                }
            });
            str_name = itemView.findViewById(R.id.str_name);
            des_name = itemView.findViewById(R.id.des_name);
            str_time = itemView.findViewById(R.id.str_time);
            charge = itemView.findViewById(R.id.tv_charge);
            waypoint = itemView.findViewById(R.id.tv_waypoint);
            along_time = itemView.findViewById(R.id.along_time);
        }
    }
}