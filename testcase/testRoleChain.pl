subClass("a", "Thing").
subClass("c", "Thing").
subClass("b", "Thing").
supEx("b", "R2", "c", "c").
supEx("a", "R1", "b", "b").
subRChain("R1", "R2", "R").
nom("c").
nom("a").
nom("b").
rol("R").
rol("R2").
rol("R1").
cls("Thing").
inst(X, X) :- nom(X).
self(X, V) :- nom(X), rol(V), triple(X, V, X).
inst(X, Z) :- subClass(Y, Z), inst(X, Y).
inst(X, Z) :- subConj(Y1, Y2, Z), inst(X, Y1), inst(X, Y2).
inst(X, Z) :- rol(Y), rol(V), subEx(V, Y, Z), triple(X, V, X1), inst(X1, Y).
inst(X, Z) :- rol(Y), subEx(V, Y, Z), self(X, V), inst(X, Y).
triple(X, V, X1) :- supEx(Y, V, Z, X1), inst(X, Y).
inst(X1, Z) :- rol(V), supEx(Y, V, Z, X1), inst(X, Y).
inst(X, Z) :- rol(V), subSelf(V, Z), self(X, V).
self(X, V) :- rol(V), supSelf(Y, V), inst(X, Y).
triple(X, W, X1) :- rol(V), rol(W), subRole(V, W), triple(X, V, X1).
self(X, W) :- rol(V), rol(W), subRole(V, W), self(X, V).
triple(X, W, X11) :- rol(U), rol(V), rol(W), subRChain(U, V, W), triple(X, U, X1), triple(X1, V, X11).
triple(X, W, X1) :- rol(U), rol(V), rol(W), subRChain(U, V, W), self(X, U), triple(X, V, X1).
triple(X, W, X1) :- rol(U), rol(V), rol(W), subRChain(U, V, W), triple(X, U, X1), self(X1, V).
triple(X, W, X) :- rol(U), rol(V), rol(W), subRChain(U, V, W), self(X, U), self(X, V).
triple(X, W, X1) :- rol(V1), rol(V2), rol(W), subRConj(V1, V2, W), triple(X, V1, X1), triple(X, V2, X1).
self(X, W) :- rol(V1), rol(V2), rol(W), subRConj(V1, V2, W), self(X, V1), self(X, V2).
inst(Y, Z) :- inst(X, Y), nom(Y), inst(X, Z).
inst(X, Z) :- inst(X, Y), nom(Y), inst(Y, Z).
triple(Z, U, Y) :- inst(X, Y), nom(Y), triple(Z, U, X).
triple(X, V, X) :- nom(X), rol(V), self(X, V).
inst(Y, R) :- range(V, R), triple(X, V, Y).
inst(X, R) :- domain(V, R), triple(X, V, Y).
top("Thing").
bot("Nothing").
