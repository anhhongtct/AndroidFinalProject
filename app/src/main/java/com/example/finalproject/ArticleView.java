package com.example.finalproject;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ArticleView extends LinearLayout {
    ImageView article_image;
    TextView article_title;
    TextView article_description;

    public ArticleView(Context context) {
        super(context);
        init(context);
    }

    public ArticleView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(context);
    }

    public void init(Context context) {

        LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layoutInflater.inflate(R.layout.row_article, this, true);

        article_image = (ImageView)findViewById(R.id.article_image);
        article_title = (TextView)findViewById(R.id.article_title);
        article_description = (TextView)findViewById(R.id.article_description);
    }

    public void setImage(String img) {
        article_image.setImageResource(Integer.parseInt(img));
    }

    public void setTitle(String title) {
        article_title.setText(title);
    }

    public void setDescription(String desc){
        article_description.setText(desc);
    }

}
