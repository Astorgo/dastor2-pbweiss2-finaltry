package edu.illinois.cs.cs125.cs125finalproject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import yahoofinance.Stock;
import yahoofinance.YahooFinance;

public class MainActivity extends AppCompatActivity {

    EditText ticker_input;
    Button ticker_enter;
    TextView results_view;
    LinearLayout scroll_bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ticker_input = (EditText)findViewById(R.id.ticker_input);
        results_view = (TextView)findViewById(R.id.results_view);
        scroll_bar = (LinearLayout)findViewById(R.id.scroll_bar);

        ticker_enter = (Button)findViewById(R.id.ticker_enter);
        ticker_enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ticker = ticker_input.getText().toString().toUpperCase();
                getSymbolInfo(ticker);
            }
        });
    }

    private void getSymbolInfo(String ticker) {
        if(ticker.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Stock symbol cannot be empty.",
                    Toast.LENGTH_SHORT).show();
        }
        else {
            new RetrieveFeedTask().execute(ticker);
        }
    }

    class RetrieveFeedTask extends AsyncTask<String, Void, String> {

        private Exception exception;

        protected String doInBackground(String... symbols) {
            try {
                StringBuilder sb = new StringBuilder();

                Stock stock = YahooFinance.get(symbols[0]);
                sb.append(stock.getName());
                sb.append("\n");
                sb.append("\n");
                sb.append("Price: ");
                sb.append(stock.getQuote().getPrice());
                sb.append("\n");
                sb.append("\n");
                sb.append("Annual Dividend Yield: ");
                sb.append(stock.getDividend().getAnnualYieldPercent());
                sb.append("\n");
                sb.append("\n");
                sb.append("Market Cap ");
                sb.append(stock.getStats().getMarketCap());
                sb.append("\n");
                sb.append("\n");
                sb.append("Book Value Per Share: ");
                sb.append(stock.getStats().getBookValuePerShare());
                sb.append("\n");
                sb.append("\n");
                sb.append("Price/Earnings Ratio: ");
                sb.append(stock.getStats().getPe());
                sb.append("\n");
                sb.append("\n");
                sb.append("Shares Outstanding: ");
                sb.append(stock.getStats().getSharesOutstanding());
                //see guide here: https://github.com/sstrickx/yahoofinance-api

                return sb.toString();
            }
            catch (Exception e) {
                return e.getMessage();
            }
        }

        protected void onPostExecute(String feed) {
            results_view.setText(feed);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
