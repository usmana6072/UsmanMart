<<<<<<< HEAD
package com.techtitans.usman.usmanmart.recyclerViewAdapters;
=======
package com.techtitans.usman.usmanmartbuyer.recyclerViewAdapters;
>>>>>>> d8f0a10 (backup whole project)

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.techtitans.usman.usmanmart.R;
import com.techtitans.usman.usmanmart.models.MessageModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
<<<<<<< HEAD
import java.util.Locale;
=======
>>>>>>> d8f0a10 (backup whole project)

public class ChatDetailsAdapter extends RecyclerView.Adapter<ChatDetailsAdapter.ViewHolder> {

    protected Context context;
<<<<<<< HEAD
    protected ArrayList<MessageModel> messages;
    protected String senderId;

    int SENDER_VIEW_TYPE = 1;
    int RECEIVER_VIEW_TYPE = 2;

    public ChatDetailsAdapter(Context context, ArrayList<MessageModel> messages, String senderId) {
        this.context = context;
        this.messages = messages;
        this.senderId = senderId;
=======
    protected  ArrayList<MessageModel> messages;
    protected  String senderId;

    int SENDER_VIEW_TYPE=1;
    int RECEIVER_VIEW_TYPE=2;


    public ChatDetailsAdapter(Context context, ArrayList<MessageModel> messages,String senderId) {
        this.context = context;
        this.messages = messages;
        this.senderId=senderId;
>>>>>>> d8f0a10 (backup whole project)
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
<<<<<<< HEAD
        if (viewType == SENDER_VIEW_TYPE)
            view = LayoutInflater.from(context).inflate(R.layout.item_message_buyer, parent, false);
        else
            view = LayoutInflater.from(context).inflate(R.layout.item_message_seller, parent, false);
        return new ViewHolder(view);
=======
        if(viewType==SENDER_VIEW_TYPE)
            view= LayoutInflater.from(context).inflate(R.layout.item_message_buyer,parent,false);
        else
            view= LayoutInflater.from(context).inflate(R.layout.item_message_seller,parent,false);
        return new ViewHolder(view);

>>>>>>> d8f0a10 (backup whole project)
    }

    @Override
    public int getItemViewType(int position) {
<<<<<<< HEAD
        MessageModel model = messages.get(position);
        if (model != null && model.getUserId() != null && model.getUserId().equals(senderId))
=======
        if(messages.get(position).getUserId().equals(senderId))
>>>>>>> d8f0a10 (backup whole project)
            return SENDER_VIEW_TYPE;
        else
            return RECEIVER_VIEW_TYPE;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
<<<<<<< HEAD
        MessageModel model = messages.get(position);
        if (model == null) return;

        holder.messageText.setText(model.getMessageText());

        if (model.getMessageTime() != 0) {
            SimpleDateFormat format = new SimpleDateFormat("h:mm a", Locale.getDefault());
            holder.messageTime.setText(format.format(new Date(model.getMessageTime())));
            holder.messageTime.setVisibility(View.VISIBLE);
        } else {
            holder.messageTime.setVisibility(View.GONE);
        }
=======
        MessageModel model=messages.get(position);
        holder.messageText.setText(model.getMessageText());
        SimpleDateFormat format=new SimpleDateFormat("hh:mm");
        holder.messageTime.setText(format.format(new Date(model.getMessageTime())));
>>>>>>> d8f0a10 (backup whole project)
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

<<<<<<< HEAD
    class ViewHolder extends RecyclerView.ViewHolder {

        TextView messageText;
        TextView messageTime;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            messageText = itemView.findViewById(R.id.tvMessageText);
            messageTime = itemView.findViewById(R.id.tvMessageTime);
        }
    }
}
=======
    class ViewHolder extends RecyclerView.ViewHolder{

        TextView messageText;
        TextView messageTime;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            messageText=itemView.findViewById(R.id.tvMessageText);
            messageTime=itemView.findViewById(R.id.tvMessageTime);
        }

    }
}
>>>>>>> d8f0a10 (backup whole project)
