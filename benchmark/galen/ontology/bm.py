import sys
import os

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
        
    newcmd = "{0} -f \"%e\" -o {1} timelimit -t {2} {3}".format(TIME, timeoutfile, limit, cmd)
    print newcmd
    os.system(newcmd)
    e = 0
    f = open(timeoutfile)
    lines = f.readlines()
    print lines
    
    TEMPL = "Command exited with non-zero status "
    if len(lines) > 1:
        e = int(lines[0][len(TEMPL):]) 
    t = float(lines[-1][:-2])
    return (e, t)
