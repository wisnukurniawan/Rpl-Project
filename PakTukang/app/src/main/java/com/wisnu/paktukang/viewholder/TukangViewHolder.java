package com.wisnu.paktukang.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.wisnu.paktukang.R;
import com.wisnu.paktukang.models.DataDiri;

/**
 * Created by private on 13/06/2016.
 */
public class TukangViewHolder extends RecyclerView.ViewHolder {

    public TextView authorView;
    public TextView noHp;
    public TextView daerah;
    public TextView keahlian;

    public TukangViewHolder(View itemView) {
        super(itemView);
        authorView = (TextView) itemView.findViewById(R.id.post_author);
        noHp = (TextView) itemView.findViewById(R.id.tvNoHp__);
        daerah = (TextView) itemView.findViewById(R.id.tvDaerah__);
        keahlian = (TextView) itemView.findViewById(R.id.tvKeahlian__);
    }

    public void bindToPost(DataDiri post) {
        authorView.setText(post.username);
        noHp.setText(post.noHp);
        daerah.setText(post.daerah);
        keahlian.setText(post.keahlian);
    }
}
