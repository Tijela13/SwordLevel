package br.tijela.swordlevelv3.util;

public class DValue<K, V>{

    protected K value1;
    protected V value2;

    public DValue(K value1, V value2){
        this.value1 = value1;
        this.value2 = value2;
    }

    public K getValue1(){
        return value1;
    }

    public V getValue2(){
        return value2;
    }

}
