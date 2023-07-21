; -*- Mode: Jasmin; indent-tabs-mode: t; tab-width: 4 -*-
; ---------------------------------------------------------------------------
; Multi-Phasic Applications: SquirrelJME
;     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
; ---------------------------------------------------------------------------
; SquirrelJME is under the GNU General Public License v3+, or later.
; See license.mkd for licensing and copyright information.
; ---------------------------------------------------------------------------

.class public lang/bytecode/TestIAdd
.super lang/bytecode/BaseMathInteger

.method public <init>()V
	aload 0
	invokenonvirtual lang/bytecode/BaseMathInteger/<init>()V
	return
.end method

.method public calc(II)I
.limit locals 3
.limit stack 2
	iload_1
	iload_2

; Calculate then return
	iadd
	ireturn
.end method
