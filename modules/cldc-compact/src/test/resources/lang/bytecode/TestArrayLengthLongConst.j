; -*- Mode: Jasmin; indent-tabs-mode: t; tab-width: 4 -*-
; ---------------------------------------------------------------------------
; SquirrelJME
;     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
; ---------------------------------------------------------------------------
; SquirrelJME is under the Mozilla Public License Version 2.0.
; See license.mkd for licensing and copyright information.
; ---------------------------------------------------------------------------

.class public lang/bytecode/TestArrayLengthLongConst
.super lang/bytecode/__ArrayLength__

.method public <init>()V
	aload 0
	invokenonvirtual lang/bytecode/__ArrayLength__/<init>()V
	return
.end method

.method public test()I
.limit stack 4
; Create array
	bipush 4
	newarray long
	
; Return the length
	arraylength
	ireturn
.end method
