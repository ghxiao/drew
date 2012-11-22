Introduction
============

`DReW` is a reasoner for DL-Programs over Datalog-rewritable Descritption Logics.


Download and Install
====================

1. Download DLV from <http://www.dlvsystem.com/dlvsystem/index.php/Home> 
2. Download drew-version.zip from <https://github.com/ghxiao/drew/downloads> and extract it  
3. Set environment variable `DREW_HOME` by `export DREW_HOME=/path/to/drew`
  
Command Line Usage
==================

DReW can be used from command line

```
Usage: drew [-rl | -el] [ -asp | -wf ] -ontology <ontology_file> {-sparql <sparql_file> 
    | -cq <cq_file> | -dlp <dlp_file> | -default <df_file> } 
    [-filter <filter>] -dlv <dlv_file> [-verbose <verbose_level>] 
  -rl | -el 
    rewriting for OWL 2 RL or OWL 2 EL
  -asp, -wf
    the semantics of DL-Programs 
    -asp: Answer set semantics (default)
    -wf: Well-founded semantics  
  <ontology_file>
    the ontology file to be read 
  <sparql_file>
    the sparql file to be query, which has to be a conjunctive query 
  <cq_file>
    the cq file to be query, which has to be a conjunctive query 
  <dlp_file>
    the dl-program file
  <df_file>
    the default rules file 
  <dlv_file>
    the path of dlv 
  <verbose_level>
    Specify verbose category (default: 0)

Example: drew -el -ontology university.owl -dlp rule.dlp -dlv /usr/bin/dlv 
```

### A note on `-wf` option

Due to a parser bug with the java library DLVWrapper, when using `-wf` option, please use the script `dlv-wf` provided for `<dlv_file>` .

Examples
=============

In the following, we assume the dlv is located at `$HOME/bin/dlv` .

DL-Program Reasoning
--------------------


### Example with network DL-Programs under ASP semantics	
	./drew -rl -ontology sample_data/network.owl -dlp sample_data/network.dlp -filter connect -dlv $HOME/bin/dlv

	{ connect(x1, <http://www.kr.tuwien.ac.at/staff/xiao/ontology/network.owl#n1>) connect(x2, <http://www.kr.tuwien.ac.at/staff/xiao/ontology/network.owl#n5>) }

	{ connect(x1, <http://www.kr.tuwien.ac.at/staff/xiao/ontology/network.owl#n5>) connect(x2, <http://www.kr.tuwien.ac.at/staff/xiao/ontology/network.owl#n1>) }

	{ connect(x1, <http://www.kr.tuwien.ac.at/staff/xiao/ontology/network.owl#n5>) connect(x2, <http://www.kr.tuwien.ac.at/staff/xiao/ontology/network.owl#n4>) }

	{ connect(x1, <http://www.kr.tuwien.ac.at/staff/xiao/ontology/network.owl#n1>) connect(x2, <http://www.kr.tuwien.ac.at/staff/xiao/ontology/network.owl#n4>) }
	

### Example with network DL-Programs under Well-founded semantics
	
	$ ./drew -rl -ontology sample_data/network.owl -dlp sample_data/network.dlp -filter connect -wf -dlv ./dlv-wf


Answering Conjunctive Query
---------------------------
	
	$ ./drew -rl -ontology sample_data/U0.owl -cq sample_data/lubm_q1.cq -dlv $HOME/bin/dlv
	{ ans(<http://www.Department0.University0.edu/GraduateStudent142>) ans(<http://www.Department0.University0.edu/GraduateStudent44>) ans(<http://www.Department0.University0.edu/GraduateStudent124>) ans(<http://www.Department0.University0.edu/GraduateStudent101>) }


Default Reasoning
-----------------

	$ ./drew -el -ontology sample_data/bird.owl -default sample_data/bird.df -dlv $HOME/bin/dlv
	in("Fred", "Flier")
	out("Tweety", "Flier")
	
	
Build From Source
=================

	$ git clone https://github.com/ghxiao/drew.git
	$ cd drew
	$ ./build.sh
	
Then you will find `drew-verson.zip` in the `target` folder. 

Publications
============

[1] T. Eiter, M. Ortiz, M. Simkus, T. Tran, and G. Xiao. Query rewriting for Horn-SHIQ plus rules. In Proc. of _AAAI 2012_. AAAI.

[2] T. Eiter, T. Krennwallner, P. Schneider, and G. Xiao. Uniform Evaluation of Nonmonotonic DL-Programs. In FoIKS'12, pages 1–22. Springer.

[3] Guohui Xiao and Thomas Eiter. Inline evaluation of hybrid knowledge bases – PhD description. In Proc. 5th International Conference on Web Reasoning and Rule Systems (RR 2011), volume 6902 of Lecture Notes in Computer Science, pages 300–305. Springer, 2011.

[4] S. Heymans, T. Eiter, and G. Xiao. Tractable reasoning with DL-programs over datalog- rewritable description logics. In Proc. of ECAI 2010. IOS Press.

[5] G. Xiao, S. Heymans, and T. Eiter. DReW: a reasoner for datalog-rewritable description logics and dl-programs. In _Informal Proc. 1st Int’l Workshop on Business Models, Business Rules and Ontologies (BuRO 2010)_, 2010.

Contact
=======
Guohui Xiao <mailto:xiao@kr.tuwien.ac.at>
