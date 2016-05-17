\ -*- Mode: Forth; indent-tabs-mode: t; tab-width: 4 -*-
\ ---------------------------------------------------------------------------
\ Multi-Phasic Applications: SquirrelJME
\     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
\     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
\ ---------------------------------------------------------------------------
\ SquirrelJME is under the GNU Affero General Public License v3+, or later.
\ For more information see license.mkd.
\ ---------------------------------------------------------------------------

\ Compatibility with ANSI Forth
: alloc-mem  ( bytes -- addr ; on failure nothing is pushed )
	\ Allocate bytes, ANSI Forth does ( bytes -- addr 0 ) for success
	allocate
	
	\ If equal to zero, allocation was a success
	0 = IF
		\ DEBUG TO FORCE FAILURE
		DROP
	
	\ Otherwise, allocation failed, the stack is empty
	ELSE
		DROP
	
	\ End conditional
	THEN
	
	\ End
	;

\ Allocates memory, 0 is returned if the operation failed
: eat-memory ( bytes -- addr)
	\ If allocation fails in IEEE1275 then nothing is placed onto the stack
	\ and it remains the same size, so the stack size must be counted
	DEPTH
	
	\ Add one to it
	1 +
	
	\ Swap so the bytes to allocate is at the top
	swap
	
	\ Allocate memory
	alloc-mem
	
	\ Place the stack depth onto the stack again
	DEPTH
	
	\ Add one to the new depth
	1 +
	
	\ Copy either the allocation pointer or the old depth to the top
	\ If allocation failed:
	\    olddepth+1
	\    newdepth
	\ If allocation succeeded:
	\    olddepth+1
	\    pointer
	\    newdepth
	\
	\ This then becomes:
	\ Failed:
	\    olddepth+1
	\    newdepth
	\    olddepth+1
	\ Succeeded:
	\    olddepth+1
	\    pointer
	\    newdepth
	\    pointer
	OVER
	
	\ If the topmost values are equal then allocation failed
	= IF
		\ Drop the old depth
		DROP
		
		\ Place zero for failure
		0
	
	\ If they are not then allocation suceeded
	ELSE
		\ Swap olddepth+1 and pointer
		SWAP
		
		\ Drop olddepth+1
		DROP
	
	\ End condition
	THEN
	
	\ End
	;

\ Infinite memory testing loop
: memory-test-loop
	BEGIN
		\ Allocate some memory
		1024 eat-memory
	
		\ Dup the value twice (need to store it)
		DUP
		DUP
	
		\ If zero, allocation failed, stop the loop
		0 = IF
			." Allocation failed, stopping!"
			
			\ Drop error value (the pointer)
			DROP
		
			\ End loop
			QUIT
	
		\ Allocation suceeded otherwise
		ELSE
			.S
			DROP
		THEN
	UNTIL
	
	\ End
	;

\ Run testing loop
memory-test-loop

