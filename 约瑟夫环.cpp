#include <iostream>
#include <string.h>
#include <malloc.h>
#include <vector>
using namespace std;

typedef struct Node{
    int order;
    struct Node* next;
} node;

typedef struct Loop{
    int length;
    node* head;
    node* tail;
} loop;

bool loopInit(loop *L,int n){
    if(n<=0){
        cout << "请输入大于0的人数"<<endl;
        return false;
    }
    for(int i=1;i<=n;i++){
        node* newp=(node*)malloc(sizeof(node));
        if(!newp){
             cout<<"内存分配失败"<<endl;
            return false;
        }
        if(i==1){
            L->head=L->tail=newp;
            L->tail->order=1;
        }else{
            L->tail->next=newp;
            L->tail=L->tail->next;
            L->tail->order=i;
        }
    }
    L->tail->next=L->head;
    L->length=n;
    return true;
}
//void deleteNode()
bool process(loop *L,int &m){
    if(m<=0){
        cout<<"请输入大于0的密码"<<endl;
        return false;
    }
    if(!L->head){
        cout<<"请先初始化L"<<endl;
        return false;
    }
    node *pre=L->head,*cur=L->head;
    for(int i=1;L->length;i++){
        if(!(i%m)){
            cout<<cur->order;
            pre->next=cur->next;
            free(cur);
            cur=pre->next;
            L->length--;
            if(L->length)
                cout<<"---";
            else
                cout<<endl;
        }else{
            cur=cur->next;
            if(i!=1)
                pre=pre->next;
        }
    }
    return true;
}
bool process(loop *L,vector<int> &key){
    if(!L->head){
        cout<<"未初始化L"<<endl;
        return false;
    }
    node *pre=L->tail,*cur=L->head;
    int k = key[0];
    //node* p=L->head;
    /* for(;p!=L->tail;p=p->next)
        cout<<p->order<<"**";

    cout<<L->tail->next->order<<endl;
    cout<<L->length<<endl;*/
    for(int i=1;L->length;i++){
        if(!(i%k)){
            k = key[(cur->order)-1];
            cout<<cur->order;
            pre->next=cur->next;

            //cout<<"*k"<<k<<"**"<<"i:"<<i<<endl;
            i = 0;
            free(cur);
            L->length--;
            if(L->length){
                cout<<"---";
                cur=pre->next;
            }
            else
                cout<<endl;
        }
            cur=cur->next;
                pre=pre->next;

 }
    return true;
}

void input(){
    cout<<"问题描述"<<endl;
    bool goOn=true;
    for( ;goOn ; ){
        loop *L = new loop();
        char mode;
        bool inptuGoOn=true;
        while(inptuGoOn){
            cout<< "模式选择："<<endl;
            cout<< "1.单密码模式\t2.多密码模式.\tq.退出."<<endl;
            cout<< "select mode:";
            cin>>mode;
            int n;

            //           cout<<mode<<"***"<<endl;
            switch(mode){
            case '1':
                cout<<"请输入人数:"<<endl;
                cin>>n;
                if(loopInit(L,n)){
                    int key;
                    cout<<"请输入密码：";
                    cin>>key;
                    process(L,key);
                    delete L;
                }else{
                    delete L;
                    break;
                }
                inptuGoOn=false;
                break;
            case '2':
                cout<<"请输入人数:"<<endl;
                cin>>n;
//                cout<<"select 2"<<endl;
                if(loopInit(L,n)){
                    vector<int> key(n,1);
                    cout<<"请输入每个人的密码："<<endl;
                    for(int i=0;i<n;i++){
                        cin>>key[i];

                    }
                    /*       for(int i=0;i<n;i++)
                             cout<<key[i];*/
                    process(L,key);
                    delete L;
                }else{
                    delete L;
                    break;
                }
                inptuGoOn=false;
                break;
            case 'q':
                goOn = false;
                inptuGoOn = false;
                break;
            default :
                cout<<"输入错误,请重新输入."<<endl;
}
        }
    }}
int main() {
    //int m,n;
    // loop *L;
    input();
    return 0;
}
