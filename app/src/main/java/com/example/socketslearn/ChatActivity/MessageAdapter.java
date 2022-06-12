package com.example.socketslearn.ChatActivity;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.socketslearn.R;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    private final LayoutInflater inflater;
    private final List<Message> messages;
    private OnMessageListener mOnMessageListener;

    private static int TYPE_SENT = 1;
    private static int TYPE_RECIEVE = 2;

    private String userId;

    MessageAdapter(Context context, List<Message> messages, OnMessageListener onMessageListener, String userId)
    {
        this.messages = messages;
        this.inflater = LayoutInflater.from(context);

        this.mOnMessageListener = onMessageListener;

        this.userId = userId;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view;

        if (viewType == TYPE_SENT)
            view = inflater.inflate(R.layout.message_sent, parent, false);
        else
            view = inflater.inflate(R.layout.message_recieve, parent, false);

        Log.d("SOCKET", String.valueOf(viewType));

        return new ViewHolder(view, mOnMessageListener);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position)
    {
        Message message = messages.get(position);
        holder.nameView.setText(message.getSenderName());
        holder.textView.setText(message.getText());
        holder.timeView.setText(message.getTime());
    }

    @Override
    public int getItemViewType(int position) {
        if (String.valueOf(messages.get(position).getSenderId()).equals(userId)) {
            return TYPE_SENT;
        } else {
            return TYPE_RECIEVE;
        }
    }

    @Override
    public int getItemCount()
    {
        return messages.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final TextView nameView, textView, timeView;
        OnMessageListener onMessageListener;

        ViewHolder(View view, OnMessageListener onMessageListener){
            super(view);
            nameView = view.findViewById(R.id.message_sender);
            textView = view.findViewById(R.id.message_text);
            timeView = view.findViewById(R.id.message_time);
            this.onMessageListener = onMessageListener;

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onMessageListener.onMessageClick(getAdapterPosition());
        }
    }

    public interface OnMessageListener{
        void onMessageClick(int pos);
    }
}
