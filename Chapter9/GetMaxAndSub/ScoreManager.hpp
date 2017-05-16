//
// Created by wc on 17-5-3.
//
#include <vector>
#ifndef GETMAXANDSUB_SCOREMANAGER_H
#define GETMAXANDSUB_SCOREMANAGER_H

#include <iostream>
#include <vector>
using namespace std;
class ScoreManager {
    vector<pair<int,int>> save;
public:
    ScoreManager(vector<pair<int,int>> s) : save(s){}
    void showScores();
    pair<pair<int,int>,pair<int,int>> getMaxAndSubMaxScoreBySearch();
    pair<pair<int,int>,pair<int,int>> getMaxAndSubMaxScoreByHeap();
    pair<pair<int,int>,pair<int,int>> getMaxAndSubMaxScoreByTourNamet();
};


#endif //GETMAXANDSUB_SCOREMANAGER_H
