; -*- Mode: Jasmin; indent-tabs-mode: t; tab-width: 4 -*-
; ---------------------------------------------------------------------------
; SquirrelJME
;     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
; ---------------------------------------------------------------------------
; SquirrelJME is under the Mozilla Public License Version 2.0.
; See license.mkd for licensing and copyright information.
; ---------------------------------------------------------------------------

.class public lang/bytecode/arraylength/TestArrayLengthByte
.super lang/bytecode/arraylength/__ArrayLength__

.method public <init>()V
	aload 0
	invokenonvirtual lang/bytecode/arraylength/__ArrayLength__/<init>()V
	return
.end method

.method public test()I
.limit stack 4
; Create array
	invokestatic lang/bytecode/ByteCodeUtil/arrayLength()I
	newarray byte
	
; Return the length
	arraylength
	ireturn
.end method
