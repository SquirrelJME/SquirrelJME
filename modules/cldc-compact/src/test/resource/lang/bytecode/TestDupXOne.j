; -*- Mode: Jasmin; indent-tabs-mode: t; tab-width: 4 -*-
; ---------------------------------------------------------------------------
; Multi-Phasic Applications: SquirrelJME
;     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
; ---------------------------------------------------------------------------
; SquirrelJME is under the GNU General Public License v3+, or later.
; See license.mkd for licensing and copyright information.
; ---------------------------------------------------------------------------

.class lang/bytecode/TestDupXOne
.super net/multiphasicapps/tac/TestInteger

.method public <init>()V
	aload 0
	invokenonvirtual net/multiphasicapps/tac/TestInteger/<init>()V
	return
.end method

.method public test()I
.limit stack 3
	
	sipush 1234	; b
	sipush 3000	; a
	
	; b, a -> a, b, a
	dup_x1
	
	iadd		; b + a -> x [a, x]
	iadd		; a + x -> y [y]
	
	ireturn
.end method
	
