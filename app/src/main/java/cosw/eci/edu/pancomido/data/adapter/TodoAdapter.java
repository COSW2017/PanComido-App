package cosw.eci.edu.pancomido.data.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import cosw.eci.edu.pancomido.R;
import cosw.eci.edu.pancomido.data.model.Todo;

/**
 * Created by estudiante on 3/9/17.
 */

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.viewHolder> {

    private List<Todo> todos;

    public TodoAdapter(List<Todo> t){
        todos = t;
    }

    @Override
    public viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.todos, parent, false);

        return new viewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(viewHolder holder, int position) {
        Todo t = todos.get(position);
        holder.setDescription(t.getDescription());
        holder.setPriority(t.getPriority()+"");
        holder.setCompleted(t.isCompleted()+"");
    }

    @Override
    public int getItemCount() {
        return todos.size();
    }

    class viewHolder extends RecyclerView.ViewHolder {

        TextView description;
        TextView priority;
        TextView completed;

        public viewHolder(View itemView) {
            super(itemView);
            description = (TextView) itemView.findViewById(R.id.description);
            priority = (TextView) itemView.findViewById(R.id.priority);
            completed = (TextView)  itemView.findViewById(R.id.completed);
        }

        public TextView getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description.setText(description);
        }

        public TextView getPriority() {
            return priority;
        }

        public void setPriority(String priority) {
            this.priority.setText(priority);
        }

        public TextView getCompleted() {
            return completed;
        }

        public void setCompleted(String completed) {
            this.completed.setText(completed);
        }
    }
}
