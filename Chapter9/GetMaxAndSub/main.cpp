#include <iostream>
#include <vector>
#include <random>
#include "ScoreManager.hpp"
using namespace std;
class Heap;
int main() {
    vector<pair<int,int>> s(512);
    uniform_int_distribution<int> u(0,999);
    default_random_engine e;
    for(int i=0;i<512;i++)
        s[i] = {u(e),i};
    ScoreManager sm(s);

    sm.showScores();
    cout<<endl;

    pair<pair<int,int>,pair<int,int>> res({{0,0},{0,0}});

    clock_t start = clock();
    res = sm.getMaxAndSubMaxScoreByHeap();
    clock_t end = clock();
    cout<<"用堆确定查找："<<endl;
    cout<<"最高分编号为: "<<res.first.second<<" 分数为："<<res.first.first<<"\t"<<"次高分编号为: "<<res.second.second<<" 分数为："
    <<"时间为: "<< (end-start)<<res.second.first<<endl;

    start = clock();
    res = sm.getMaxAndSubMaxScoreBySearch();
    end = clock();
    cout<<"线性查找："<<endl;
    cout<<"最高分编号为: "<<res.first.second<<" 分数为："<<res.first.first<<"\t"<<"次高分编号为: "<<res.second.second<<" 分数为："
        <<"时间为: "<< end-start<<res.second.first<<endl;

    start = clock();
    res = sm.getMaxAndSubMaxScoreByTourNamet();
    end = clock();
    cout<<"竞标赛法查找："<<endl;
    cout<<"最高分编号为: "<<res.first.second<<" 分数为："<<res.first.first<<"\t"<<"次高分编号为: "<<res.second.second<<" 分数为："
    <<"时间为: "<<end-start<<res.second.first<<endl;

    return 0;
}
