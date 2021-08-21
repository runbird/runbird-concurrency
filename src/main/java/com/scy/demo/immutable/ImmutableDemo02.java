package com.scy.demo.immutable;

import com.scy.annoations.NotThreadSafe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ImmutableDemo02 {

    private final List<Integer> data = new ArrayList<>();

    public ImmutableDemo02(){
        data.add(1);
        data.add(2);
        data.add(3);
    }

    @NotThreadSafe
    public List<Integer> getData(){
        return data;
    }

    public List<Integer> getData2() {
        return Collections.unmodifiableList(new ArrayList<>(data));
    }

    public static void main(String[] args) {
        ImmutableDemo02 immutable = new ImmutableDemo02();
        List<Integer> data = immutable.getData();
        List<Integer> unmodifiable = immutable.getData2();
        //导致了数据可修改
        data.add(4);
//        unmodifiable.add(4);
    }
}
