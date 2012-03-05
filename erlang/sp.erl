-module(sp).
-compile(export_all).
-record(person, {name,age=0,phone=""}).
-define(mar(X),{a,X}).

birthday(P) ->
	P#person{age = P#person.age + 1}.

start() ->
	  ?mar(2).
    spawn(?MODULE, loop, [0]).

loop(Val) ->
    receive
        incre ->
        	  io:format("by ~w~n",[Val]),
            loop(Val + 1)
    end.
