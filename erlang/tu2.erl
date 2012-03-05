%%file io exercise
-module(tu2).% module
-compile(export_all).
 
file(Fname)->
	case file:open(Fname,[read,raw,binary] of 
	{ok,Fd} ->
		scan_file(Fd,0,file:read(Fd,1024));
	{error,Reason}->
		{error,Reason}
		end.
		