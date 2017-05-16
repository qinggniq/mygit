#include <iostream>
#include <memory>
#include <vector>

using namespace std;

class Heap{
private:
    shared_ptr<vector<int>> heapVector;

public:
    Heap(int length){
        this->heapVector = make_shared<vector<int>>(length+1);
    }
    int getMax(){

    } 
};

int main(){
    Heap s(0);
    s.heapVector->push_back(1);
    cout<<s.heapVector->at(0)<<endl;
}
