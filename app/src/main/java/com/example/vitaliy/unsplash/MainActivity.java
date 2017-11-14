package com.example.vitaliy.unsplash;

import android.app.SearchManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;

import com.kc.unsplash.Unsplash;
import com.kc.unsplash.api.Order;
import com.kc.unsplash.models.Photo;
import com.kc.unsplash.models.SearchResults;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    private final String CLIENT_ID = "611b6a517cafa69285e513282257c6516061c205974a8a39a641cdb408f4d4ca";
    private final String code = "98f3eabb12eed312575028ded4bfec7c9e10ec10192c7f9a4e66a67faa6d985f";
    private final String secret = "128fd0de2c81798597c416c83cfd320b5e9029a45e43d74a0e659d2b1bfb5864";
    private final String url = "urn:ietf:wg:oauth:2.0:oob";
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    Unsplash unsplash = new Unsplash(CLIENT_ID);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        Photos(1, 10, Order.POPULAR);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        final SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                SearchQuery(query);
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.oldest:
                Photos(1, 10, Order.OLDEST);
                return true;
            case R.id.latest:
                Photos(1, 10, Order.LATEST);
                return true;
            case R.id.popular:
                Photos(1, 10, Order.POPULAR);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void Photos(int f, int l, Order order) {
        unsplash.getPhotos(f, l, order, new Unsplash.OnPhotosLoadedListener() {
            @Override
            public void onComplete(List<Photo> photos) {
                MyAdapter adapter = new MyAdapter(photos, MainActivity.this);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onError(String error) {
            }
        });
    }

    private void SearchQuery(String query) {
        unsplash.searchPhotos(query, new Unsplash.OnSearchCompleteListener() {
            @Override
            public void onComplete(SearchResults results) {
                List<Photo> photos = results.getResults();
                MyAdapter adapter = new MyAdapter(photos, MainActivity.this);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onError(String error) {

            }
        });
    }
}
