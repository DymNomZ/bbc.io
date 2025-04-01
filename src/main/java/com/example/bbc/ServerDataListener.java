package com.example.bbc;

import datas.SerialData;

public interface ServerDataListener<T extends SerialData> {
    void run(T data);
}
