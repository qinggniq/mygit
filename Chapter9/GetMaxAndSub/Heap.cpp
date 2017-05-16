//
// Created by wc on 17-5-3.
//
#include <iostream>
#include <vector>
#include "Heap.h"

using namespace std;
class Heap;

Heap::Heap(vector<pair<int,int>> s) {
    this->heapVector.assign(s.begin(),s.end());
    this->heapVector.insert(heapVector.cbegin(),make_pair(0,0));
    this->tailPos = this->heapVector.size()-1;

    for(int i = this->tailPos/2;i>=1;i--) {
        pair<int,int> curNum = this->heapVector[i];
        int parePos = 2*i;

        while(parePos <= this->tailPos){
            if(parePos<this->tailPos&&this->heapVector[parePos].first<this->heapVector[parePos+1].first) {
                parePos++;
            }
            if(curNum.first > this->heapVector[parePos].first){
                break;
            }
            heapVector[parePos/2] = heapVector[parePos];
            parePos*=2;
        }
        heapVector[parePos/2] = curNum;
    }
}

pair<int,int> Heap::getMax() const {
    return this->heapVector[1];
}

bool Heap::empty() const {
    return this->heapVector.size() <= 1;
}
pair<int,int> Heap::pop() {
    if(this->empty())
        return make_pair(-1,-1);
    pair<int,int> max = this->getMax();
    this->heapVector[1] = make_pair(-1,-1);
    pair<int,int> tailEle = this->heapVector[this->tailPos];
    this->tailPos--;
    int curPos = 1;
    int childPos = 2;
    while (childPos <= this->tailPos) {
        if(childPos < this->tailPos&& this->heapVector[childPos].first < this->heapVector[childPos+1].first){
            childPos++;
        }
        if(tailEle.first>=this->heapVector[childPos].first)
            break;
        this->heapVector[curPos] = this->heapVector[childPos];
        curPos = childPos;
        childPos *= 2;
    }
    this->heapVector[curPos] = tailEle;
    return max;
}

void Heap::push(pair<int,int> n) {
    if(this->tailPos+1 > this->heapVector.size()){
        this->heapVector.push_back({0,0});
    }else{
        this->heapVector[this->tailPos+1] = make_pair(0,0);
    }
    this->heapVector;
    this->tailPos++;
    int curPos = this->tailPos;
    while(curPos!=1&&n.first > this->heapVector[curPos/2].first) {
        this->heapVector[curPos] = this->heapVector[curPos/2];
        curPos/=2;
    }
    this->heapVector[curPos] = n;
}


