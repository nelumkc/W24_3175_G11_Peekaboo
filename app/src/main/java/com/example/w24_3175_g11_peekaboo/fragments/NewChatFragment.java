package com.example.w24_3175_g11_peekaboo.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.w24_3175_g11_peekaboo.R;
import com.example.w24_3175_g11_peekaboo.databases.DaycareDatabase;
import com.example.w24_3175_g11_peekaboo.interfaces.ParentDao;
import com.example.w24_3175_g11_peekaboo.model.Child;
import com.example.w24_3175_g11_peekaboo.utils.FirebaseUtil;


public class NewChatFragment extends Fragment {

    String chatroomId;
    long otherUser;

    public static final String PREFS_NAME = "sharedPref";

    DaycareDatabase daycaredb;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_chat, container, false);


        Child child = null;
        if (getArguments() != null) {
            child = (Child) getArguments().getSerializable("child");
            ParentDao parentDao = daycaredb.parentDao();
            otherUser = parentDao.getUserIdByParentId(child.getChildParentId());
        }
        long currentUserId = getActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).getLong("currentUserId", -1);
        chatroomId = FirebaseUtil.getChatroomId(String.valueOf(currentUserId),String.valueOf(otherUser));

        getOrCreateChatRoomModel();

        return view;
    }

    private void getOrCreateChatRoomModel() {
         FirebaseUtil.getChatroomReference(chatroomId).get();
    }
}