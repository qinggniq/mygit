算法导论9.3-7

题目：给定两个长度为n的有序数组，设计一个算法，在O（lgn)的时间复杂度找出2n个数的中位数。

分析：比较两数组下标为n/2的元素，若A[n/2]>B[n/2],则中位数在小于A[n/2]的元素和大于B[n/2]的元素中；
      若A[n/2]=B[n/2]，则中位数为A[n/2];若A[n/2]<B[n/2],则中位数在大于A[n/2]的元素和小于B[n/2]的元素中。
      递归。

#include <iostream>

using namespace std;

int mid(int *A, int *B, int first1,int last1,int first2,int last2);

int main()
{
	int A[3] = { 0,2,3};
	int B[3] = { 3,8,9};
	int zhong = mid(A, B, 0, 2, 0, 2);
	cout << zhong << endl;

	return 0;
}

int mid(int *A, int *B, int first1, int last1, int first2, int last2)
{
	static int i = 0, j = 0;
	if ((last1 - first1 - 1) == 0)          //在元素为
		return i;
	else if (last1 - first1 == 0)
		return (A[last1] + B[last2]) / 2;
	if ((last1 - first1 + 1) % 2 == 0){
		i = ((A[(last1 + first1 + 1) / 2] + A[(last1 + first1 + 1) / 2 - 1]) / 2);
		j = ((B[(last2 + first2 + 1) / 2] + B[(last2 + first2 + 1) / 2 - 1]) / 2);
	}
	else{
		i =( A[(last1 + first1 + 1) / 2 ]);
	    j =( B[(last2 + first2 + 1) / 2 ]); //?
	}
	if (i>j)
	{
		last1 = (last1 + first1 + 1) / 2 ;
		first2 = (last2 + first2 + 1) / 2 ;
		mid(A, B, first1, last1, first2, last2);
	}
	else if (i == j)
	{
		return i;
	}
	else
	{
		last2 = (last2 +first2 + 1) / 2 ;
		first1 = (last1 + first1 + 1) / 2 ;
		mid(A, B, first1, last1, first2, last2);
	}

}
