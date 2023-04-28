package com.llw.easyview;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewbinding.ViewBinding;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public abstract class EasyActivity<T extends ViewBinding> extends AppCompatActivity {

    protected T binding;
    protected Context context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        onRegister();
        super.onCreate(savedInstanceState);
        this.context = this;
        Type type = this.getClass().getGenericSuperclass();
        if (type instanceof ParameterizedType) {
            try {
                Class<T> clazz = (Class<T>) ((ParameterizedType) type).getActualTypeArguments()[0];
                //反射
                Method method = clazz.getMethod("inflate", LayoutInflater.class);
                binding = (T) method.invoke(null, getLayoutInflater());
            } catch (Exception e) {
                e.printStackTrace();
            }
            setContentView(binding.getRoot());
        }
        onCreate();
    }

    protected void onRegister() {

    }

    protected abstract void onCreate();

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    protected void showMsg(CharSequence easy) {
        Toast.makeText(context, easy, Toast.LENGTH_SHORT).show();
    }

    protected void jumpActivity(final Class<?> clazz) {
        startActivity(new Intent(context, clazz));
    }
}