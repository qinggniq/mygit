#!/usr/bin/python
# -*- coding: UTF-8 -*-

class Task:
    def __init__(self,id,time):
        self.time = time
        self.id = id
    def getTime(self):
        return self.time
    def getId(self):
        return self.id

def show(list,task_num):
    sum_time = 0
    start_time = 0
    for i in list:
        print "任务编号：%s 开始时间：%d 终止时间： %d 等待时间：%d" % (i[0].getId(), start_time, i[1] + start_time, start_time)
        sum_time += start_time
        start_time += i[1]
    print "平均等待时间：%f" % (float(sum_time) / task_num)

dic = {}
task_num = input("请输入任务个数：")
for i in range(0,task_num,1):
    id = raw_input("请输入任务编号：")
    time = input("请输入任务时间：")
    task = Task(id,time)
    dic[task] = time
init_list = dic.iteritems()

print "按初始顺序"
show(init_list,task_num)
sorted_list = sorted(dic.iteritems(),key =lambda d:d[1])
print "处理后"
show(sorted_list,task_num)

