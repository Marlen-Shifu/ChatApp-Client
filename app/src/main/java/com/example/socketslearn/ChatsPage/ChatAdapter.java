package com.example.socketslearn.ChatsPage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.socketslearn.R;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {

    private final LayoutInflater inflater;
    private final List<Chat> chats;
    private OnChatListener mOnChatListener;

    ChatAdapter(Context context, List<Chat> chats, OnChatListener onChatListener)
    {
        this.chats = chats;
        this.inflater = LayoutInflater.from(context);

        this.mOnChatListener = onChatListener;
    }

    @Override
    public ChatAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = inflater.inflate(R.layout.chat_item, parent, false);
        return new ViewHolder(view, mOnChatListener);
    }

    @Override
    public void onBindViewHolder(ChatAdapter.ViewHolder holder, int position)
    {
        Chat chat = chats.get(position);
        holder.nameView.setText(chat.getName());
    }

    @Override
    public int getItemCount()
    {
        return chats.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final TextView nameView;
        OnChatListener onChatListener;

        ViewHolder(View view, OnChatListener onChatListener){
            super(view);
            nameView = view.findViewById(R.id.name);
            this.onChatListener = onChatListener;

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onChatListener.onChatClick(getAdapterPosition());
        }
    }

    public interface OnChatListener{
        void onChatClick(int pos);
    }
}
