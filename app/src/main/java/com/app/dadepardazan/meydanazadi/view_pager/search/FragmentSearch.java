package com.app.dadepardazan.meydanazadi.view_pager.search;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.dadepardazan.meydanazadi.MainActivity;
import com.app.dadepardazan.meydanazadi.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FragmentSearch extends Fragment {

    private String title = "";
    private int image;
    SharedPreferences settings;
    RecyclerView recyclerview_fragmant_search;
    SearchFragmentAdapter adapter;
    MainActivity.MCrypt mcrypt;
    public JSONArray jarr_person = new JSONArray();
    JSONArray jarr_tag;


    public static FragmentSearch newInstance(String title, int resImage) {

        FragmentSearch fragment = new FragmentSearch();

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


        recyclerview_fragmant_search = view_main.findViewById(R.id.recyclerview_fragmant_search);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerview_fragmant_search.setLayoutManager(layoutManager);
        try {

            jarr_person = new JSONArray(title);
            Toast.makeText(getContext(), jarr_person.toString(), Toast.LENGTH_SHORT).show();

        } catch (JSONException e) {
            e.printStackTrace();
        }
        final ArrayList<JSONObject> data_list = new ArrayList<JSONObject>();
        for (int i = 0; i < 4; i++) {
            JSONObject a = new JSONObject();
            data_list.add(a);
        }
        adapter = new SearchFragmentAdapter(getContext(), data_list,false) {
            @Override
            public void load_more() {

            }
        };
        adapter.clear_list();
        adapter.insert(adapter.getItemCount(), jarr_person);
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