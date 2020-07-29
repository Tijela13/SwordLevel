package br.tijela.swordlevelv3.util;

import java.util.*;

public class TimeMark<K>{

    protected Map<K, List<DValue<Long, String>>> marks = new HashMap<>();



    public List<DValue<Long, String>> getMarks(K key){
        return marks.containsKey(key) ? marks.get(key) : new ArrayList<>();
    }

    public List<DValue<Long, String>> getMarks(K key, long start){
        List<DValue<Long, String>> marks = new ArrayList<>();

        for (DValue<Long, String> entry : getMarks(key)){
            if(entry.getValue1() >= start)
                marks.add(entry);
        }

        return marks;
    }

    public List<DValue<Long, String>> getMarks(K key, String tag){
        List<DValue<Long, String>> marks = new ArrayList<>();

        for (DValue<Long, String> entry : getMarks(key)){
            if(entry.getValue2().equalsIgnoreCase(tag))
                marks.add(entry);
        }

        return marks;
    }

    public List<DValue<Long, String>> getMarks(K key, long start, String tag){
        List<DValue<Long, String>> marks = new ArrayList<>();

        for (DValue<Long, String> entry : getMarks(key)){
            if(entry.getValue1() >= start && entry.getValue2().equalsIgnoreCase(tag))
                marks.add(entry);
        }

        return marks;
    }




    public int countMarks(){
        return marks.size();
    }

    public int countMarks(K key){
        return getMarks(key).size();
    }

    public int countMarks(K key, long start){
        int i = 0;

        for (DValue<Long, String> entry : getMarks(key)){
            if(entry.getValue1() >= start)
                i++;
        }


        return i;
    }

    public int countMarks(K key, String tag){
        int i = 0;

        for (DValue<Long, String> entry : getMarks(key)){
            if(entry.getValue2().equalsIgnoreCase(tag))
                i++;
        }


        return i;
    }

    public int countMarks(K key, long start, String tag){
        int i = 0;

        for (DValue<Long, String> entry : getMarks(key)){
            if(entry.getValue1() >= start && entry.getValue2().equalsIgnoreCase(tag))
                i++;
        }


        return i;
    }



    public void removeMarks(K key){
        marks.remove(key);
    }

    public void removeMarks(K key, long end){

        List<DValue<Long, String>> marksKey = marks.get(key);
        if(marksKey == null) return;

        List<DValue<Long, String>> toRemove = new ArrayList<>();

        for (DValue<Long, String> entry : marksKey){
            if(entry.getValue1() <= end)
                toRemove.add(entry);
        }

        for (DValue<Long, String> entry : toRemove){
            marksKey.remove(entry);
        }

        if(marksKey.size() < 1)
            marks.remove(key);

    }

    public void removeMarks(long end){
        Set<K> keys = new HashSet<>(marks.keySet());
        for (K key : keys){
            removeMarks(key, end);
        }
    }



    public void add(K key){
        add(key, System.currentTimeMillis(), null);
    }

    public void add(K key, String tag){
        add(key, System.currentTimeMillis(), tag);
    }

    public void add(K key, long time, String tag){
        List<DValue<Long, String>> mark = getMarks(key);

        mark.add(new DValue<>(time, tag));

        marks.put(key, mark);
    }


    public Set<K> keySet(){
        return marks.keySet();
    }

}
