#!/usr/bin/python
# -*- coding: UTF-8 -*-
import copy
import fileinput
class Task:
    def __init__(self,id,time,put_time=0):
        self.time = time
        self.id = id
        self.put_time = put_time

    def get_time(self):
        return self.time
    def set_time(self,t):
        self.time = t
    def get_id(self):
        return self.id
    def getput_time(self):
        return self.put_time

class Queue:
    def __init__(self):
        self.queue = []
    def enqueue(self,item):
        self.queue.append(item)
    def dequeue(self):
        return self.queue.pop(0)
    def font(self):
        if not self.empty():
            return self.queue[0]
    def empty(self):
        return self.size() == 0
    def size(self):
        return len(self.queue)

class Priorityueue:
    def __init__(self):
        self.queue = []
        self.lenght = 0
        self.queue.insert(0,self.lenght)

    def count(self):
        return self.lenght

    def empty(self):
        if self.lenght == 0:
            return True
        else:
            return False
    def top(self):
        if self.empty():
            return self.queue[0]
        return self.queue[1]

    def push(self,item):
        self.lenght+=1
        tmp = item
        self.queue.insert(self.lenght,item)
        i = self.lenght
        while i>=2 and self.queue[i].get_time() < self.queue[i/2].get_time():
            self.queue[i],self.queue[i/2] = self.queue[i/2],self.queue[i]
            i = i/2
    def pop(self):
        isEmpty = self.empty()
        if self.lenght ==  1:
            self.lenght -= 1
            del self.queue[1]
            return
        if isEmpty == False:
            self.queue[1] = self.queue[self.lenght]
            self.lenght-=1
            self._adjust()
    def _adjust(self):
        root = 1
        j = root << 1
        temp = self.queue[root]
        while j <= self.lenght:
            if j < self.lenght and self.queue[j].get_time() >= self.queue[j+1].get_time() :
                j += 1
                if self.queue[j].get_time() > temp.get_time():
                    break
                self.queue[j],self.queue[root] = self.queue[root],self.queue[j]
                root = j
                j = j<<1
            else:
                break
        self.queue[root] = temp
#静态处理
def process():
    dic = []
    task_num = input("请输入任务个数：")
    if task_num <= 0:
        return
    for i in range(0, task_num, 1):
        id = raw_input("请输入任务编号：")
        time = input("请输入任务时间：")
        task = Task(id, time)
        dic.append(task)
    init_list = dic
    print "按初始顺序"
    show(init_list, task_num)
    sorted_list = sorted(dic, key=lambda d: d.get_time())
    print "处理后"
    show(sorted_list, task_num)
#动态处理
def dly_process():
    dic = []
    task_num = input("请输入任务个数：")
    for i in range(0, task_num, 1):
        id = raw_input("请输入任务编号：")
        time = input("请输入任务时间：")
        start_time = input("请输入任务提交时间:")
        task = Task(id, time, start_time)
        dic.append(task)
    sort_list = sorted(dic, key=lambda d: d.getput_time())
    myQueue = Queue()
    for i in sort_list:
        myQueue.enqueue(i)

    tmpQueue = copy.deepcopy(myQueue)
    now_time = tmpQueue.font().getput_time()
    cur_endtime = now_time
    wa_time = 0
    while not tmpQueue.empty():
        tmp = tmpQueue.dequeue()
        wa_time = -tmp.getput_time()+now_time
        cur_endtime += tmp.get_time()
        print "任务编号：%s 开始时间：%d 终止时间： %d 等待时间：%d" % (tmp.get_id(), now_time, cur_endtime, wa_time)
        now_time = cur_endtime

    print myQueue.font().get_id()
    my = Priorityueue()
    cur_time = myQueue.font().getput_time()
    curend_time = myQueue.font().getput_time()
    my.push(myQueue.dequeue())

    while not myQueue.empty() or not my.empty():

        if myQueue.empty() and (not my.empty()):
            while not my.empty():
                tmp = my.top()
                my.pop()
                cur_time = curend_time
                curend_time = cur_time + tmp.get_time()
                print "任务%s 开始时间：%d 结束时间：%d 等待时间：%d" % (
                    tmp.get_id(), cur_time, curend_time, -tmp.getput_time() + cur_time)
        if not myQueue.empty() and my.empty():

            my.push(myQueue.dequeue())
            tmp = my.top()
            my.pop()
            cur_time = tmp.getput_time()
            print "任务%s 开始时间：%d 结束时间：%d 等待时间：%d" % (
                tmp.get_id(), tmp.getput_time(), cur_time + tmp.get_time(), 0)

        if not myQueue.empty() and not my.empty():

            tmp = my.top()
            my.pop()
            cur_time = curend_time
            curend_time = cur_time + tmp.get_time()
            print "任务%s 开始时间：%d 结束时间：%d 等待时间：%d" % (
                tmp.get_id(), cur_time, curend_time, cur_time - tmp.getput_time())
            while not myQueue.empty():
                if myQueue.font().getput_time() <= curend_time:
                    my.push(myQueue.dequeue())
                else:
                    break


def show(list,task_num):
    sum_time = 0
    start_time = 0
    for i in list:
        print "任务编号：%s 开始时间：%d 终止时间： %d 等待时间：%d" % (i.get_id(), start_time, i.get_time() + start_time, start_time)
        sum_time += start_time
        start_time += i.get_time()
    print "平均等待时间：%f" % (float(sum_time) / task_num)




while True:
    print "请选择动态提交或动态提交："
    print "1.动态 2.静态 3.退出"
    i = input(":")
    if(type(i)!= (int)):
        print "输入错误重新输入"
        continue
    if i == 1:
        dly_process()
        continue
    if i == 2:
        process()
    if i == 3:
        break
    else:
        print "输入错误重新输入"
        continue

