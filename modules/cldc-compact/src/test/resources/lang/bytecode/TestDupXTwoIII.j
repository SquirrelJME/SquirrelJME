; -*- Mode: Jasmin; indent-tabs-mode: t; tab-width: 4 -*-
; ---------------------------------------------------------------------------
; SquirrelJME
;     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
; ---------------------------------------------------------------------------
; SquirrelJME is under the Mozilla Public License Version 2.0.
; See license.mkd for licensing and copyright information.
; ---------------------------------------------------------------------------

.class public lang/bytecode/TestDupXTwoIII
.super net/multiphasicapps/tac/TestInteger

.method public <init>()V
	aload 0
	invokenonvirtual net/multiphasicapps/tac/TestInteger/<init>()V
	return
.end method

.method public test()I
.limit stack 4
	
; c
	sipush 1234
; b
	sipush 3000
; a
	sipush 7777 
	
; c, b, a -> a, c, b, a
	dup_x2

; b + a -> x [a, c, x]
	iadd		
; c + x -> y [a, y]
	iadd		
; a + y -> z [z]
	iadd		
	
	ireturn
.end method
	
