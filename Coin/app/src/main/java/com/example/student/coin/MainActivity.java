package com.example.student.coin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    CryptoListAdapter adapter;
    APIInterface apiInterface;
    private RecyclerView recyclerView;
    private List<Data> cryptoList = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        apiInterface = APIClient.getClient().create(APIInterface.class);
        initRecyclerView();
        getCoinList();
    }
    private void initRecyclerView() {

        recyclerView = findViewById(R.id.recycler);

        cryptoList = new ArrayList<>();

        adapter = new CryptoListAdapter(cryptoList);

        recyclerView.setAdapter(adapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter.setClickListener(new CryptoListAdapter.ItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(MainActivity.this, CoinPage.class);
                intent.putExtra("coin", adapter.getItem(position));
                startActivity(intent);
            }
        });
    }
    private void getCoinList() {
        Call<CryptoList> call2 = apiInterface.doGetUserList("100");
        call2.enqueue(new Callback<CryptoList>() {
            @Override
            public void onResponse(Call<CryptoList> call, Response<CryptoList> response) {
                CryptoList list = response.body();

                cryptoList.clear();
                cryptoList.addAll(list.getData());
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onFailure(Call<CryptoList> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Exista o problema", Toast.LENGTH_SHORT).show();
                Log.d("XXXX", t.getLocalizedMessage());
                call.cancel();
            }
        });
    }
}
