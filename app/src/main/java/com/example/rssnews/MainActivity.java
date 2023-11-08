package com.example.rssnews;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    NewsAdapter adapter;
    ArrayList<News> list;
    ListView lvNews;
  ArrayList<String>  arrayLink;
   // ArrayAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lvNews = (ListView) findViewById(R.id.dsnews);
        list = new ArrayList<>();
        //arrayTitle = new ArrayList<>();
        arrayLink = new ArrayList<>();
         adapter = new NewsAdapter(list,MainActivity.this,R.layout.news_line);

        lvNews.setAdapter(adapter);
        new ReadRSS().execute("https://vnexpress.net/rss/so-hoa.rss");

        lvNews.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this,ActivityDetail.class);
                intent.putExtra("linktintuc",arrayLink.get(position));
                startActivity(intent);
            }
        });
    }

    private class ReadRSS extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... strings) {
            StringBuilder content = new StringBuilder();
            try {
                URL url = new URL(strings[0]);
                InputStreamReader inputStreamReader = new InputStreamReader(url.openConnection().getInputStream());
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                String line="";
                while ((line=bufferedReader.readLine())!=null){
                    content.append(line);
                }
                bufferedReader.close();
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return content.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            XMLDOMParser parser = new XMLDOMParser();
            Document document = parser.getDocument(s);

            NodeList nodeList = document.getElementsByTagName("item");

            String tieuDe="";
            String link="";
            String desc="";
            String imageLink="";
            String htmlDesc="";

            for(int i=0;i<nodeList.getLength();i++){
                Element element = (Element) nodeList.item(i);
                tieuDe = parser.getValue(element,"title");
                link = parser.getValue(element,"link");
                htmlDesc = parser.getValueDesc(element,"description");
                imageLink = parser.getImageLink(htmlDesc);
                desc = parser.getDescContent(htmlDesc);

                News news = new News(tieuDe,desc,link,imageLink);
                list.add(news);
               //arrayTitle.add(tieuDe);

                arrayLink.add(link);
            }

            adapter.notifyDataSetChanged();
        }
    }
}