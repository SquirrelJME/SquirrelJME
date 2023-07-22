; -*- Mode: Jasmin; indent-tabs-mode: t; tab-width: 4 -*-
; ---------------------------------------------------------------------------
; Multi-Phasic Applications: SquirrelJME
;     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
; ---------------------------------------------------------------------------
; SquirrelJME is under the GNU General Public License v3+, or later.
; See license.mkd for licensing and copyright information.
; ---------------------------------------------------------------------------

.class public lang/bytecode/TestINeg
.super lang/bytecode/BaseMathIntegerSingle

.method public <init>()V
	aload 0
	invokenonvirtual lang/bytecode/BaseMathIntegerSingle/<init>()V
	return
.end method

.method public calc(I)I
.limit locals 2
.limit stack 2
	iload_1

; Calculate then return
	ineg
	ireturn
.end method
