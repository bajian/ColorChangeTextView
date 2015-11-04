package com.example.bajian.colorchangetextview;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

/**
 * 字体颜色变换demo，可用于歌词等场景，从上到下还没写，有需要的自己完成
 */
public class MainActivity extends AppCompatActivity {

    private ColorChangeTextView mChangeTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        mChangeTv=(ColorChangeTextView)findViewById(R.id.changeTv);

        fromLeft();
    }

    private void fromLeft() {
//        mChangeTv.setDirection(ColorChangeTextView.FROM_RIGHT);
        mChangeTv.setDirection(ColorChangeTextView.FROM_LEFT);
//你要设置动画的对象的属性必须有一个set该值的方法。因为ObjectAnimator在动画的过程中自动更新属性值，这是通过调用该属性的set方法来实现的。例如，如果属性的名字是foo，你需要有一个setFoo()的方法
        ObjectAnimator.ofFloat(mChangeTv,"progress",0,1).setDuration(5000).start();
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
