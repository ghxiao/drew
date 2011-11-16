#!/usr/bin/python

import time
import os
import glob

# N = 5

def gen(N):
	filename = "df-galen.fact-%d.dl" % N
        print filename
	rf = open(filename, 'w')

	rf.write('isa("project", "Project").\n')
	rf.write('isa("read", "Action").\n')
	rf.write('nom("project").\n')
	rf.write('nom("read").\n')

        rf.write('q("h0").\n')
        rf.write('q("h1").\n')
        rf.write('q("h2").\n')

	i = 0
	while i < N:
		rf.write('nom("h%d").\n'%i)
		if (i % 3 == 0) :
                        rf.write('isa("h%d", "Human").\n'%i)
		elif i % 3 == 1:
			rf.write('isa("h%d", "Infant").\n'%i)
		else:
			rf.write('isa("h%d", "Bird").\n'%i)
		i=i+1
		rf.flush()
	rf.close()


# for n in [1, 3, 5, 7, 9,10,20,30,40,50, 60, 70, 80, 90, 100, 110, 120, 130, 140, 150, 160, 170, 180, 190, 200, 210, 220, 230, 240, 250, 260, 270, 280, 290, 300, 310, 320, 330, 340, 350, 360, 370, 380, 390, 400]:
#	gen(n)
# for n in [1, 3, 5, 7, 9,10,20,30,40,50, 60, 70, 80, 90, 100, 110, 120, 130, 140, 150, 160, 170, 180, 190, 200, 210, 220, 230, 240, 250, 260, 270, 280, 290, 300, 310, 320, 330, 340, 350, 360, 370, 380, 390, 400]:
for n in xrange(300, 1001, 100):
        gen(n)
#gen(10)
#gen(20)
#gen(30)
#gen(40)
#gen(50)
#gen(60)
#gen(70)

