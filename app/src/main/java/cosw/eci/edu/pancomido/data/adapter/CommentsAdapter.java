package cosw.eci.edu.pancomido.data.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import cosw.eci.edu.pancomido.R;
import cosw.eci.edu.pancomido.data.listener.RestaurantsListener;
import cosw.eci.edu.pancomido.data.model.Comment;
import cosw.eci.edu.pancomido.data.model.Dish;

/**
 * Created by Alejandra on 3/12/2017.
 */

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.viewHolder>{
    private final List<Comment> comments;

    public CommentsAdapter( List<Comment> comments)
    {
        this.comments = comments;
    }

    @Override
    public CommentsAdapter.viewHolder onCreateViewHolder(ViewGroup parent, int viewType )
    {
        View itemView = LayoutInflater.from( parent.getContext() ).inflate( R.layout.comment_row, parent, false );
        return new CommentsAdapter.viewHolder( itemView );
    }

    @Override
    public void onBindViewHolder( final CommentsAdapter.viewHolder holder, final int position )
    {
       final Comment comment = comments.get(position);
       DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
       String reportDate = df.format(comment.getComment_date());
       holder.setDate(reportDate);
       holder.setDescription(comment.getComment_des());
    }


    @Override
    public int getItemCount()
    {
        return comments.size();
    }

    class viewHolder
            extends RecyclerView.ViewHolder {
        TextView date;
        TextView description;
        public viewHolder( View itemView )
        {
            super( itemView );
            date = (TextView) itemView.findViewById( R.id.comment_date );
            description = (TextView) itemView.findViewById( R.id.comment );
        }
        public void setDate( String date )
        {
            this.date.setText( date );
        }
        public void setDescription( String description )
        {
            this.description.setText( description );
        }

    }
}
