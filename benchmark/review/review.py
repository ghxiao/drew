#!/usr/bin/python

import time
import os
import glob

benchmark = "review" 

os.system(("rm -f %s-time.txt")%(benchmark))

rf = open('%s-time.txt'%(benchmark),'w')

rf.write('ontology,\trule,\tdlvhex(s), \told rewriting(s), \tdlv(s),\tclingo, \trewriting(s),\tdlv(s),\tclingo(s)\n')

for ontology in glob.glob("ontology/*.owl"):
    for rule in glob.glob("rules/*.elp"):
    #for rule in glob.glob("rules/lubm_7.elp"):
        print ("%s,\t%s") % (ontology, rule)
        rf.write (("%s,\t%s") % (ontology, rule))
        
        hexfile = rule.replace(".elp", ".dlvhex")
        dlvhexcmd= "/usr/bin/time -o time.out -f \"%%e\" timelimit -t 300  dlvhex  %s --ontology=%s > /dev/null 2>&1" % (hexfile, ontology)
        print dlvhexcmd; 
        os.system(dlvhexcmd)
        f = open('time.out')
        t = f.readlines()[-1][:-2]
        print "dlvhex ", t,"s"
        rf.write(",\t%s"%t)
        rf.flush()
        
        dreweloldcmd= "/usr/bin/time -o time.out -f \"%%e\" timelimit -t 300  ../../dist/drew.el.sh -ontology %s -dlp %s -dlv /usr/bin/dlv --rewriting-only > /dev/null 2>&1" % (ontology, rule) 
        datalog = "%s-%s-default.dl" % (ontology, rule.split('/')[-1])
        os.system(dreweloldcmd)
        f = open('time.out')
        t = f.readlines()[-1][:-2]
        print "old rewriting ", t,"s"
        rf.write(",\t%s"%t)
        rf.flush()

        dlvcmd = "/usr/bin/time -o time.out -f \"%%e\" timelimit -t 300 /usr/bin/dlv %s > /dev/null 2>&1" % datalog
        os.system(dlvcmd)
        f = open('time.out')
        t = f.readlines()[-1][:-2]
        print "dlv", t, "s"
        rf.write(",\t%s"%t)
        rf.flush()
        
        clingocmd = "/usr/bin/time -o time.out -f \"%%e\" timelimit -t 300 clingo %s > /dev/null 2>&1" % datalog
        os.system(clingocmd)
        f = open('time.out')
        t = f.readlines()[-1][:-2]
        print "clingo ", t, "s"
        rf.write(",\t%s"%t)
        rf.flush()
        
        drewelcmd= "/usr/bin/time -o time.out -f \"%%e\" timelimit -t 300  ../../dist/drew.el.sh -ontology %s -dlp %s -dlv /usr/bin/dlv -rewriting inc --rewriting-only > /dev/null 2>&1" % (ontology, rule) 
        datalog = "%s-%s-inc.dl" % (ontology, rule.split('/')[-1])
        os.system(drewelcmd)
        f = open('time.out')
        t = f.readlines()[-1][:-2]
        print "drew-el new incremental rewriting ", t, "s"
        rf.write(",\t%s"%t)
        rf.flush()

        dlvcmd = "/usr/bin/time -o time.out -f \"%%e\" timelimit -t 300 /usr/bin/dlv %s > /dev/null 2>&1" % datalog
        os.system(dlvcmd)
        f = open('time.out')
        t = f.readlines()[-1][:-2]
        print "dlv", t, "s"
        rf.write(",\t%s"%t)
        rf.flush()
        
        clingocmd = "/usr/bin/time -o time.out -f \"%%e\" timelimit -t 300 clingo %s > /dev/null 2>&1" % datalog
        os.system(clingocmd)
        f = open('time.out')
        t = f.readlines()[-1][:-2]
        print "clingo ", t, "s"
        rf.write(",\t%s"%t)
        
        rf.write('\n')
        
        
rf.close()        
        #print drewelcmd
        #print datalog
        #os.system(drewelcmd)
        
#    do		#echo $ontology $rule
#	echo "$ontology $rule" >> time.txt
#	echo "/usr/bin/time -o time.out -f \"%e\" timelimit -t 300  ../../dist/drew.el.sh -ontology $ontology -dlp $rule -dlv /usr/bin/dlv -rewriting inc > /dev/null" 
#	echo cat time.out >> time.txt
		#for file in *.txt
	   	#do cat $file $dirName/*.dlABox > $dirName/"$file-lubm1.requiem.dl"
		#done
#    done

