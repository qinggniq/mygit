//
// Created by wc on 17-5-3.
//

#ifndef GETMAXANDSUB_HEAP_H
#define GETMAXANDSUB_HEAP_H
#include <iostream>
#include <vector>
using namespace std;

class Heap {
private:
    vector<pair<int,int>> heapVector;
    int tailPos;
public:
    Heap(vector<pair<int,int>> s);
    pair<int,int> getMax() const;
    pair<int,int> pop();
    void push(pair<int,int>);
    bool empty() const;

};


#endif //GETMAXANDSUB_HEAP_H
