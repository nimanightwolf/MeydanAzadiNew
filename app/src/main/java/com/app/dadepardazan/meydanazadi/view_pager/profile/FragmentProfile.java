package com.app.dadepardazan.meydanazadi.view_pager.profile;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.dadepardazan.meydanazadi.R;

import org.json.JSONObject;

import java.util.ArrayList;

public class FragmentProfile extends Fragment {

    private String title;
    private int image;
    SharedPreferences settings;
    RecyclerView recyclerview_fragmant_search;
    ProfileFragmentAdapter adapter;


    public static FragmentProfile newInstance(String title, int resImage) {

        FragmentProfile fragment = new FragmentProfile();

        Bundle args = new Bundle();

        args.putInt("image", resImage);

        args.putString("title", title);

        fragment.setArguments(args);

        return fragment;

    }

    @Override

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        image = getArguments().getInt("image", 0);

        title = getArguments().getString("title");

    }

    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //getActivity().setTheme(R.style.NoActionBarDark);

        View view_main = inflater.inflate(R.layout.fragmant_search, container, false);
        settings = PreferenceManager.getDefaultSharedPreferences(getActivity());


        recyclerview_fragmant_search=view_main.findViewById(R.id.recyclerview_fragmant_search);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerview_fragmant_search.setLayoutManager(layoutManager);

        final ArrayList<JSONObject> data_list = new ArrayList<JSONObject>();
        for (int i=0;i<4;i++) {
            JSONObject a = new JSONObject();
            data_list.add(a);
        }
        adapter=new ProfileFragmentAdapter(getContext(),data_list) {
            @Override
            public void load_more() {

            }
        };
        recyclerview_fragmant_search.setAdapter(adapter);








        setHasOptionsMenu(true);
        return view_main;

    }



    @Override
    public void onResume() {
        super.onResume();
        // Toast.makeText(getContext(),"2222",Toast.LENGTH_SHORT).show();
//        last_ad_id = 0;
//        adapter.clear_list();
//        new get_my_ad_list().execute();
//        new get_add().execute();
    }



}