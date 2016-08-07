/*
 * 四则混合运算中缀变后缀规则：
 *   数字直接写入输出字符串
 *   如果当前运算符优先级比符号stack顶低则stack顶入输出字符串
 *   否则当前的入符号stack
 *   如果遇到‘）’或者'\0'则符号stack一直pop直到遇到‘（’或者stack底
 **/

#include <iostream>
#include <stack>
#include <string>

using namespace std;

string MIdToBeh(string Mid_exp)
{
    string Beh_exp;
    stack<char> mark;
    int exp_length = Mid_exp.length();
    int propority[128];

    propority['-'] = 0;
    propority['+'] = 0;
    propority['*'] = 1;
    propority['/'] = 1;
    propority['%'] = 1;

    for(int index = 0;index != exp_length ; ++index)
    {
        char w = Mid_exp[index];
        switch(w)
        {
            case '0':case '1':case '2':case '3':case '4':
            case '5':case '6':case '7':case '8':case '9':
            case '.':
                cout << Mid_exp[index+1]<<'\t';

                Beh_exp.push_back(w);
                if((Mid_exp[index+1]<='9'
                    &&Mid_exp[index+1]>='0')
                   ||Mid_exp[index+1]=='.');
                else
                    Beh_exp.push_back(' ');
                break;

            case '-':case '+':case '/':case '*':case '%':
            case '(':case ')':
                if(mark.empty())
                    mark.push(w);
                else if(w != '(' && w != ')')
                {
                    if(propority[w]<=propority[mark.top()])
                    {
                        Beh_exp.push_back(mark.top());
                        mark.pop();
                        mark.push(w);
                    }
                    else
                    {
                        mark.push(w);
                    }
                }
                else if(w == ')')
                {
                    char topchar = mark.top();
                    while(!mark.empty() && topchar!='(')
                    {
                        Beh_exp.push_back(topchar);
                        mark.pop();
                        topchar = mark.top();
                    }
                    if(mark.empty())
                        cout  << '缺少相应的左括号' <<endl;
                    else
                        mark.pop();
                }
                else
                    mark.push(w);
                break;
            default:
                break;
        }
    }
    if(!mark.empty())
    {
        while(!mark.empty())
        {
            Beh_exp.push_back(mark.top());
            mark.pop();
        }
    }
    return Beh_exp;
}

int main(int argc,char *argv[])
{
    string Mid_exp;
    cout <<" 输入中缀表达式：" << endl;
    cin >> Mid_exp;
    cout<<"sdfsdf"<<endl;
    cout << "相应的后缀表达式为：" << MIdToBeh(Mid_exp) <<endl;
    return 0;
}