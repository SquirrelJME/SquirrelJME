; -*- Mode: Jasmin; indent-tabs-mode: t; tab-width: 4 -*-
; ---------------------------------------------------------------------------
; Multi-Phasic Applications: SquirrelJME
;     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
; ---------------------------------------------------------------------------
; SquirrelJME is under the GNU General Public License v3+, or later.
; See license.mkd for licensing and copyright information.
; ---------------------------------------------------------------------------

.class lang/bytecode/TestOverShiftInt
.super net/multiphasicapps/tac/TestInteger

.method public <init>()V
	aload 0
	invokenonvirtual net/multiphasicapps/tac/TestInteger/<init>()V
	return
.end method

.method public test()I
.limit stack 5

	; 2 << 134 (really 4)
	sipush 2
	sipush 134
	ishl

	; 2 >> 130 (really 2)
	sipush 130
	ishr

	; 2 >>> 1153 (really 1)
	sipush 1153
	iushr

	ireturn
.end method

