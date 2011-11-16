#!/usr/bin/python

import time
import os
import glob
import sys

if sys.platform.startswith('darwin'):
	# Mac OS X
	timecmd = "/opt/local/bin/gtime"
elif sys.platform.startswith('linux'):
	# Linux-specific code here...
	timecmd = "/usr/bin/time"

DLV = "/home/xiao/usr/bin/dlv-fixed"

benchmark = "policy" 

T = time.strftime("%a-%d-%b-%Y-%H-%M-%S", time.gmtime())

os.system(("rm -f logs/%s-new-%s.txt")%(benchmark, T))

rf = open('logs/%s-new-%s-time.txt'%(benchmark, T),'w')
log = open('logs/%s-new-%s-time.log'%(benchmark, T),'w')
#rf.write('ontology,\trule,\tdlvhex(s), \told rewriting(s), \tdlv(s),\tclingo, \trewriting(s),\tdlv(s),\tclingo(s)\n')

#%for fact in glob.glob("df-%s.fact-*.dl"%benchmark): 
#for num in [1, 3, 5, 7, 9,10,20,30,40,50, 60, 70, 80, 90, 100, 110, 120, 130, 140, 150, 160, 170, 180, 190, 200, 210, 220, 230, 240, 250, 260, 270, 280, 290, 300, 310, 320, 330, 340, 350, 360, 370, 380, 390, 400]:
for num in xrange(10000, 100001, 10000):
        fact = "df-%s.fact-%d.dl" % (benchmark, num)
	print fact
	cmd = "%s -o time.out -f \"%%e\" timelimit -t 300 %s -filter=in df-%s.dl %s policy.dl ../df-common/el.dl ../df-common/el-i.dl ../df-common/el-i-n.dl  ../df-common/default.dl  > /dev/null 2>&1" % (timecmd, DLV, benchmark, fact)
	print cmd
	os.system(cmd)
	log.write(cmd + "\n")
	f = open('time.out')
	timeoutput = f.readlines()
	print timeoutput
	for line in timeoutput:
		log.write(line)
	t1 = timeoutput[-1][:-2]
	print "dlv ", t1,"s"

	cmd = "%s -o time.out -f \"%%e\" timelimit -t 300 %s -filter=in df-%s.dl %s policy.dl ../df-common/el.dl ../df-common/el-i.dl ../df-common/el-i-n.opt.dl  ../df-common/default.dl > /dev/null 2>&1" % (timecmd, DLV,benchmark,  fact)
	print cmd
	os.system(cmd)
	log.write(cmd + "\n")
	f = open('time.out')
	timeoutput = f.readlines()
	print timeoutput
	for line in timeoutput:
		log.write(line)
	t2 = timeoutput[-1][:-2]
	print "dlv/opt ", t2,"s"

	cmd = "%s -o time.out -f \"%%e\" timelimit -t 300 clingo df-%s.dl policy.dl %s ../df-common/el.dl ../df-common/el-i.dl ../df-common/el-i-n.dl  ../df-common/default.dl ../df-common/df-filter.clingo > /dev/null 2>&1" % (timecmd,benchmark, fact)
	print cmd
	os.system(cmd)
	log.write(cmd + "\n")
	f = open('time.out')
	timeoutput = f.readlines()
	print timeoutput
	for line in timeoutput:
		log.write(line)
	t3 = timeoutput[-1][:-2]
	print "clingo ", t3,"s"

	cmd = "%s -o time.out -f \"%%e\" timelimit -t 300 clingo df-%s.dl policy.dl %s ../df-common/el.dl ../df-common/el-i.dl ../df-common/el-i-n.opt.dl ../df-common/default.dl ../df-common/df-filter.clingo  > /dev/null 2>&1" % (timecmd,benchmark, fact)
	print cmd
	os.system(cmd)
	log.write(cmd + "\n")
	f = open('time.out')
	timeoutput = f.readlines()
	print timeoutput
	for line in timeoutput:
		log.write(line)
	t4 = timeoutput[-1][:-2]
	print "clingo/opt ", t4,"s"

	rf.write("%d &\t %s&\t %s&\t %s&\t %s & * & * & * \\\\ \n" % (num,t1,t2,t3,t4))
	log.flush()
	rf.flush()

rf.close()
