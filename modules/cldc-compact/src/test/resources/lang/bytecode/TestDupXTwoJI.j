; -*- Mode: Jasmin; indent-tabs-mode: t; tab-width: 4 -*-
; ---------------------------------------------------------------------------
; SquirrelJME
;     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
; ---------------------------------------------------------------------------
; SquirrelJME is under the Mozilla Public License Version 2.0.
; See license.mkd for licensing and copyright information.
; ---------------------------------------------------------------------------

.class public lang/bytecode/TestDupXTwoJI
.super net/multiphasicapps/tac/TestInteger

.method public <init>()V
	aload 0
	invokenonvirtual net/multiphasicapps/tac/TestInteger/<init>()V
	return
.end method

.method public test()I
.limit stack 5
	
	sipush 1234	; b
	i2l
	
	sipush 3000 ; a
	
	; J b, I a -> I a, J b, I a
	dup_x2
	
	i2l			; I a -> J a [I a, J b, J a]
	ladd		; J b + J a -> x [I a, J x]
	
	l2i			; J x -> I x [I a, I x]
	
	iadd		; I a + I x -> I y [I y]
	
	ireturn
.end method
	
