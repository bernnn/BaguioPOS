package com.sideline.baguiopos.view;

import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class BaseRecyclerViewAdapter<T, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {
    private List<T> list = new ArrayList<>();

    public BaseRecyclerViewAdapter(){}

    public BaseRecyclerViewAdapter(List<T> list)
    {
        this.list = list;
    }

    public void clear()
    {
        list.clear();
    }

    public void addAll(Collection collection)
    {
        list.addAll(collection);
    }

    public List<T> getList(){
        return list;
    }
}