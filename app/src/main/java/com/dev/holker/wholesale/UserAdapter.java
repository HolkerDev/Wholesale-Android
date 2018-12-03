package com.dev.holker.wholesale;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import de.hdodenhof.circleimageview.CircleImageView;

import java.util.ArrayList;

public class UserAdapter extends ArrayAdapter<User> {
    //TODO: Implement adapter
    private Context mContext;
    private int mResources;
    private ArrayList<User> mUsers;

    public UserAdapter(@NonNull Context context, int resource, @NonNull ArrayList<User> objects) {
        super(context, resource, objects);

        mContext = context;
        mResources = resource;
        mUsers = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(mContext);

        View view = inflater.inflate(R.layout.item_user, null);
        CircleImageView circleImageView = view.findViewById(R.id.user_item_image);

        final User user = mUsers.get(position);
        circleImageView.setImageBitmap(user.getAvatar());

        return view;

    }
}
