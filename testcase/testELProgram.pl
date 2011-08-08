:- auto_table.
subClass_0("C", "D").
cls_0("C").
cls_0("D").
inst_0(X, X) :- nom_0(X).
self_0(X, V) :- nom_0(X), rol_0(V), triple_0(X, V, X).
inst_0(X, Z) :- subClass_0(Y, Z), inst_0(X, Y).
inst_0(X, Z) :- subConj_0(Y1, Y2, Z), inst_0(X, Y1), inst_0(X, Y2).
inst_0(X, Z) :- rol_0(Y), rol_0(V), subEx_0(V, Y, Z), triple_0(X, V, X1), inst_0(X1, Y).
inst_0(X, Z) :- rol_0(Y), subEx_0(V, Y, Z), self_0(X, V), inst_0(X, Y).
triple_0(X, V, X1) :- supEx_0(Y, V, Z, X1), inst_0(X, Y).
inst_0(X1, Z) :- rol_0(V), supEx_0(Y, V, Z, X1), inst_0(X, Y).
inst_0(X, Z) :- rol_0(V), subSelf_0(V, Z), self_0(X, V).
self_0(X, V) :- rol_0(V), supSelf_0(Y, V), inst_0(X, Y).
triple_0(X, W, X1) :- rol_0(V), rol_0(W), subRole_0(V, W), triple_0(X, V, X1).
self_0(X, W) :- rol_0(V), rol_0(W), subRole_0(V, W), self_0(X, V).
triple_0(X, W, X11) :- rol_0(U), rol_0(V), rol_0(W), subRChain_0(U, V, W), triple_0(X, U, X1), triple_0(X1, V, X11).
triple_0(X, W, X1) :- rol_0(U), rol_0(V), rol_0(W), subRChain_0(U, V, W), self_0(X, U), triple_0(X, V, X1).
triple_0(X, W, X1) :- rol_0(U), rol_0(V), rol_0(W), subRChain_0(U, V, W), triple_0(X, U, X1), self_0(X1, V).
triple_0(X, W, X) :- rol_0(U), rol_0(V), rol_0(W), subRChain_0(U, V, W), self_0(X, U), self_0(X, V).
triple_0(X, W, X1) :- rol_0(V1), rol_0(V2), rol_0(W), subRConj_0(V1, V2, W), triple_0(X, V1, X1), triple_0(X, V2, X1).
self_0(X, W) :- rol_0(V1), rol_0(V2), rol_0(W), subRConj_0(V1, V2, W), self_0(X, V1), self_0(X, V2).
inst_0(Y, Z) :- inst_0(X, Y), nom_0(Y), inst_0(X, Z).
inst_0(X, Z) :- inst_0(X, Y), nom_0(Y), inst_0(Y, Z).
triple_0(Z, U, Y) :- inst_0(X, Y), nom_0(Y), triple_0(Z, U, X).
triple_0(X, V, X) :- nom_0(X), rol_0(V), self_0(X, V).
inst_0(Y, R) :- range_0(V, R), triple_0(X, V, Y).
inst_0(X, R) :- domain_0(V, R), triple_0(X, V, Y).
top_0("Thing").
bot_0("Nothing").
subConj_0(X, Y, Z) :- fail.
subEx_0(X, Y, Z) :- fail.
supEx_0(X, Y, Z, W) :- fail.
subSelf_0(X, Y) :- fail.
supSelf_0(X, Y) :- fail.
self_0(X, Y) :- fail.
subProd_0(X, Y, Z) :- fail.
supProd_0(X, Y, Z) :- fail.
subRConj_0(X, Y, Z) :- fail.
subRChain_0(X, Y, Z) :- fail.
subClass_0(X, Y) :- fail.
subRole_0(X, Y) :- fail.
inst_0(X, Y) :- fail.
triple_0(X, Y, Z) :- fail.
nom_0(X) :- fail.
rol_0(X) :- fail.
cls_0(X) :- fail.
range_0(X, Y) :- fail.
domain_0(X, Y) :- fail.
domainD_0(X, Y) :- fail.
subClass_1("C", "D").
cls_1("C").
cls_1("D").
inst_1(X, X) :- nom_1(X).
self_1(X, V) :- nom_1(X), rol_1(V), triple_1(X, V, X).
inst_1(X, Z) :- subClass_1(Y, Z), inst_1(X, Y).
inst_1(X, Z) :- subConj_1(Y1, Y2, Z), inst_1(X, Y1), inst_1(X, Y2).
inst_1(X, Z) :- rol_1(Y), rol_1(V), subEx_1(V, Y, Z), triple_1(X, V, X1), inst_1(X1, Y).
inst_1(X, Z) :- rol_1(Y), subEx_1(V, Y, Z), self_1(X, V), inst_1(X, Y).
triple_1(X, V, X1) :- supEx_1(Y, V, Z, X1), inst_1(X, Y).
inst_1(X1, Z) :- rol_1(V), supEx_1(Y, V, Z, X1), inst_1(X, Y).
inst_1(X, Z) :- rol_1(V), subSelf_1(V, Z), self_1(X, V).
self_1(X, V) :- rol_1(V), supSelf_1(Y, V), inst_1(X, Y).
triple_1(X, W, X1) :- rol_1(V), rol_1(W), subRole_1(V, W), triple_1(X, V, X1).
self_1(X, W) :- rol_1(V), rol_1(W), subRole_1(V, W), self_1(X, V).
triple_1(X, W, X11) :- rol_1(U), rol_1(V), rol_1(W), subRChain_1(U, V, W), triple_1(X, U, X1), triple_1(X1, V, X11).
triple_1(X, W, X1) :- rol_1(U), rol_1(V), rol_1(W), subRChain_1(U, V, W), self_1(X, U), triple_1(X, V, X1).
triple_1(X, W, X1) :- rol_1(U), rol_1(V), rol_1(W), subRChain_1(U, V, W), triple_1(X, U, X1), self_1(X1, V).
triple_1(X, W, X) :- rol_1(U), rol_1(V), rol_1(W), subRChain_1(U, V, W), self_1(X, U), self_1(X, V).
triple_1(X, W, X1) :- rol_1(V1), rol_1(V2), rol_1(W), subRConj_1(V1, V2, W), triple_1(X, V1, X1), triple_1(X, V2, X1).
self_1(X, W) :- rol_1(V1), rol_1(V2), rol_1(W), subRConj_1(V1, V2, W), self_1(X, V1), self_1(X, V2).
inst_1(Y, Z) :- inst_1(X, Y), nom_1(Y), inst_1(X, Z).
inst_1(X, Z) :- inst_1(X, Y), nom_1(Y), inst_1(Y, Z).
triple_1(Z, U, Y) :- inst_1(X, Y), nom_1(Y), triple_1(Z, U, X).
triple_1(X, V, X) :- nom_1(X), rol_1(V), self_1(X, V).
inst_1(Y, R) :- range_1(V, R), triple_1(X, V, Y).
inst_1(X, R) :- domain_1(V, R), triple_1(X, V, Y).
top_1("Thing").
bot_1("Nothing").
subConj_1(X, Y, Z) :- fail.
subEx_1(X, Y, Z) :- fail.
supEx_1(X, Y, Z, W) :- fail.
subSelf_1(X, Y) :- fail.
supSelf_1(X, Y) :- fail.
self_1(X, Y) :- fail.
subProd_1(X, Y, Z) :- fail.
supProd_1(X, Y, Z) :- fail.
subRConj_1(X, Y, Z) :- fail.
subRChain_1(X, Y, Z) :- fail.
subClass_1(X, Y) :- fail.
subRole_1(X, Y) :- fail.
inst_1(X, Y) :- fail.
triple_1(X, Y, Z) :- fail.
nom_1(X) :- fail.
rol_1(X) :- fail.
cls_1(X) :- fail.
range_1(X, Y) :- fail.
domain_1(X, Y) :- fail.
domainD_1(X, Y) :- fail.
inst_0(X, "C") :- p(X).
inst_1(X, "C") :- s(X).
dl_ATOM_0_0(X) :- inst_0(X, "D").
dl_ATOM_0_1(X) :- inst_1(X, "D").
p("a").
s("a").
s("b").
q :- dl_ATOM_0_1("a"), not dl_ATOM_0_0("b").
