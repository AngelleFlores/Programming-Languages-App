package edu.oswego.aflores.proglangv1;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by aflores on 2/9/2017.
 */

public class MainActivity extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView title = (TextView) findViewById(R.id.title);
        title.setText("Topics");
        ListView listView = (ListView) findViewById(android.R.id.list);
        if (listView == null) {
            Log.d("Main", "listView == null");
        }

        final DbAccess db = new DbAccess(this);
        String[] topics = db.getTopics();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_list_item_1, android.R.id.text1,
                topics);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                TextView clickedView = (TextView) view;
                String topic = "" + clickedView.getText();
                boolean waiting = true;

                ExecutorService es = Executors.newSingleThreadExecutor();
                FormatCodeTask fct = new FormatCodeTask(db, topic);
                es.execute(fct);

                while (waiting) {
                    if (fct.isDone()) {
                        String[] codesFormatted = fct.getCodesFormatted();
                        Intent intent = new Intent(MainActivity.this, List.class);
                        intent.putExtra("topic", topic);
                        intent.putExtra("codes", codesFormatted);
                        startActivity(intent);
                        waiting = false;
                    }
                }
            }
        });
    }
}
