; -*- Mode: Jasmin; indent-tabs-mode: t; tab-width: 4 -*-
; ---------------------------------------------------------------------------
; Multi-Phasic Applications: SquirrelJME
;     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
; ---------------------------------------------------------------------------
; SquirrelJME is under the GNU General Public License v3+, or later.
; See license.mkd for licensing and copyright information.
; ---------------------------------------------------------------------------

.class public lang/bytecode/TestILoad
.super net/multiphasicapps/tac/TestInteger

.method public <init>()V
	aload 0
	invokenonvirtual net/multiphasicapps/tac/TestInteger/<init>()V
	return
.end method

.method private static __internal(IIIIIIII)I
.limit locals 8
.limit stack 9
; Load everything in
	iload_0
	iload_1
	iload_2
	iload_3
	iload 4
	iload 5
	iload 6
	iload 7

; Add all together
	iadd
	iadd
	iadd
	iadd
	iadd
	iadd
	iadd

; Return result
	ireturn
.end method

.method public test()I
.limit locals 2
.limit stack 9

; Call method and then just return its value
	bipush 1
	bipush 2
	bipush 3
	bipush 5
	bipush 8
	bipush 13 
	bipush 21
	bipush 34
	invokestatic lang/bytecode/TestILoad/__internal(IIIIIIII)I

; Return value
	ireturn
.end method
