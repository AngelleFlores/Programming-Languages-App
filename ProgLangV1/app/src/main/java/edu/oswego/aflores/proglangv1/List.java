package edu.oswego.aflores.proglangv1;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by aflores on 2/9/2017.
 */

public class List extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        TextView title = (TextView) findViewById(R.id.title);
        ListView list = (ListView) findViewById(android.R.id.list);
        Intent intent = getIntent();
        String topic_name = intent.getStringExtra("topic");
        String[] codes = intent.getStringArrayExtra("codes");
        title.setText(topic_name);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_list_item_1, android.R.id.text1,
                codes);
        list.setAdapter(adapter);
    }
}
