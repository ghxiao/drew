#!/usr/bin/python

import time
import os
import glob
import sys

import bm

if sys.platform.startswith('darwin'):
	# Mac OS X
	timecmd = "/opt/local/bin/gtime"
elif sys.platform.startswith('linux'):
	# Linux-specific code here...
	timecmd = "/usr/bin/time"

#DLV = "/home/xiao/usr/bin/dlv-fixed"
DLV = "/home/xiao/usr/bin/dlv-magic-2011-09-16"
CLINGO = "clingo"

benchmark = "galen" 

T = time.strftime("%Y-%m-%d-%H-%M-%S", time.gmtime())

os.system(("rm -f logs/%s-new-%s.txt")%(benchmark, T))

rf = open('logs/%s-new-%s-time.txt'%(benchmark, T),'w')
log = open('logs/%s-new-%s-time.log'%(benchmark, T),'w')
#rf.write('ontology,\trule,\tdlvhex(s), \told rewriting(s), \tdlv(s),\tclingo, \trewriting(s),\tdlv(s),\tclingo(s)\n')

#%for fact in glob.glob("df-%s.fact-*.dl"%benchmark): 
#for num in [1, 3, 5, 7, 9,10,20,30,40,50, 60, 70, 80, 90, 100, 110, 120, 130, 140, 150, 160, 170, 180, 190, 200, 210, 220, 230, 240, 250, 260, 270, 280, 290, 300, 310, 320, 330, 340, 350, 360, 370, 380, 390, 400]:
for num in xrange(300, 1001, 100):
#        fact = "df-%s.user-%d.dl" % (benchmark, num)
#	print fact
        cmd = "{0} -brave -OMS- df-galen-query.dl el.dl el-i.dl el-i-n.dl el-galen.dl df-galen.fact-{1}.dl df-query.dlv".format(DLV, num)
        (e1,t1)=bm.timelimit(cmd, 300)
	log.write(cmd + "\n")
	print "dlv ", t1,"s"

#        exit(0)

        cmd = "{0} -brave -OMS- df-galen-query.dl el.dl el-i.dl el-i-n.opt.dl el-galen.dl df-galen.fact-{1}.dl df-query.dlv".format(DLV, num)
        (e2,t2)=bm.timelimit(cmd, 300)
	log.write(cmd + "\n")
	print "dlv ", t2,"s"

        cmd = "{0} df-galen-query.dl el.dl el-i.dl el-i-n.opt.dl el-galen.dl df-galen.fact-{1}.dl df-filter.clingo".format(CLINGO, num)
        (e2,t2)=bm.timelimit(cmd, 300)
	log.write(cmd + "\n")
	print "dlv/opt ", t2,"s"

        cmd = "{0} df-galen-query.dl el.dl el-i.dl el-i-n.dl el-galen.dl df-galen.fact-{1}.dl df-filter.clingo".format(CLINGO, num)
        (e3,t3)=bm.timelimit(cmd, 300)
	log.write(cmd + "\n")
	print "clingo ", t3,"s"

        cmd = "{0} df-galen-query.dl el.dl el-i.dl el-i-n.opt.dl el-galen.dl df-galen.fact-{1}.dl df-filter.clingo".format(CLINGO, num)
        (e4,t4)=bm.timelimit(cmd, 300)
	log.write(cmd + "\n")
	print "clingo ", t4,"s"

	rf.write("%d &\t %s&\t %s&\t %s&\t %s & * & * & * \\\\ \n" % (num,t1,t2,t3,t4))
	log.flush()
	rf.flush()

rf.close()

# run a command in the shell with the time limit 
# args:
#   cmd: the command to run
#   limit: the time limit
# return 
#   (errorcode, runningtime)
def timelimit(cmd, limit=300):
        if sys.platform.startswith('darwin'):
                # Mac OS X
                TIME = "/opt/local/bin/gtime"
        elif sys.platform.startswith('linux'):
                # Linux-specific code here...
                TIME = "/usr/bin/time"
        timeoutfile = 'time.out'

        newcmd = "{0} -o {1} timelimit -t {2} {3}".format(TIME, timeoutfile, limit, cmd)
	os.system(newcmd)
	f = open(timeoutfile)
	timeoutput = f.readlines()
	print timeoutput
        return (timeoutput[0][:-2], float(timeoutput[-1][:-2]))


