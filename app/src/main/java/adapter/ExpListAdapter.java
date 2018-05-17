package adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.myapplication.MainActivity;
import com.example.myapplication.R;

import java.util.ArrayList;

import item.ExpListItem;

/**
 * Created by 마루소프트 on 2018-02-01.
 */

//start, result 값비교 갱신


public class ExpListAdapter extends RecyclerView.Adapter<ExpListAdapter.ViewHolder> {
    Activity context;
    ArrayList<ExpListItem.Data> items;

    //커뮤니티 토크 어댑터 함수를 만듬으로써 context와 item을 상속해 준다.
    public ExpListAdapter(Activity context, ArrayList<ExpListItem.Data> items) {
        this.context = context;
        this.items = items;
    }

    //in create view holder는 뷰홀더에 넣어줄 layout을 찾는 애다.
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.object_list_item,parent,false);
        return new ViewHolder(view);
    }
    //온바인드뷰홀더는 아이템을 세팅하거나 스크롤링 할때 호출되는 애다. 때문에 position이 필요하다.
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.object_button.setText(items.get(position).getTerminalName());

//        holder.like.setText(String.valueOf(items.get(position).getT_like())); //int 값일때


    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    //뷰홀더라는 애는 아이템안에 들어갈 텍스트등의 내용을 초기화 하는 역할이다.
    class ViewHolder extends RecyclerView.ViewHolder{
        TextView  object_button;
        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    System.out.println(getPosition());
                    //어댑터에서는 this를 쓸 수 없으므로 context를 쓴다. context는 이 레이아웃의 변수들?
                    MainActivity.et_des.setText(items.get(getAdapterPosition()).getTerminalName());
                    MainActivity.code = items.get(getAdapterPosition()).getTerminalId();

//                    Intent intent = new Intent(context , MainActivity.class);
//                    //변수를 해당 activity로 넘긴다.
//                    intent.putExtra("exp_ter_name", items.get(getAdapterPosition()).getTerminalName());
//                    intent.putExtra("exp_ter_id", items.get(getAdapterPosition()).getTerminalId());
////                    context.startActivityForResult(intent, 1002);
                }
            });

            object_button = itemView.findViewById(R.id.object_button);
        }
    }
}
