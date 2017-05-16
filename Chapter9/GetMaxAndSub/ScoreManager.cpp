//
// Created by wc on 17-5-3.
//

#include "ScoreManager.hpp"
#include "Heap.h"
#include <vector>
#include <iostream>
using namespace std;


pair<pair<int,int>,pair<int,int>> ScoreManager::getMaxAndSubMaxScoreBySearch() {
        vector<pair<int,int>> tmpVector(this->save);
        pair<pair<int,int>,pair<int,int>> sa({{0,0},{0,0}});

        for(int i=0;i < tmpVector.size(); ++i) {
            if(tmpVector[i].first > sa.first.first) {
                sa.first  = tmpVector[i];
                //sa.second = i;
            }else if(tmpVector[i].first > sa.second.first){
                sa.second = tmpVector[i];
            }
        }
        return sa;
}

pair<pair<int,int>,pair<int,int>> ScoreManager::getMaxAndSubMaxScoreByHeap() {
    Heap heap(this->save);
    pair<pair<int,int>,pair<int,int>> result({{0,0},{0,0}});
    result.first = heap.pop();
    result.second = heap.pop();
    return result;
}
pair<pair<int,int>,pair<int,int>> ScoreManager::getMaxAndSubMaxScoreByTourNamet() {//第一次O(n)第二次log(n)
    pair<pair<int,int>,pair<int,int>> result({{0,0},{0,0}});
    int len = this->save.size();
    int tmplen = 2*this->save.size();
    vector<pair<int,int>> tmp(tmplen);
    for(int i=len;i<tmplen;i++) {
        tmp[i] = this->save[i-len];
    }
    for(int i=tmplen-2;i>=2;i--) {
        tmp[i/2] = tmp[i].first > tmp[i+1].first ? tmp[i] : tmp[i+1];
    }
    result.first = tmp[1];
    for(int i=1;i < len;) {
        if(tmp[2*i] == tmp[i]){
            if(result.second.first < tmp[2*i+1].first) {
                result.second = tmp[2*i+1];
            }
            i*=2;
        }else{
            if(result.second.first<tmp[2*i].first){
                result.second = tmp[2*i];
            }
            i = 2*i+1;
        }
    }
    return result;
}
void ScoreManager::showScores() {
    for (int i = 0, j = 0; i < this->save.size(); ++j, ++i) {
        if (j % 8 == 0)
            std::cout << std::endl;
        std::cout << "[ " << this->save[i].second << " : " << this->save[i].first << " ]   ";
    }
}

