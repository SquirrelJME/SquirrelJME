; -*- Mode: Jasmin; indent-tabs-mode: t; tab-width: 4 -*-
; ---------------------------------------------------------------------------
; Multi-Phasic Applications: SquirrelJME
;     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
; ---------------------------------------------------------------------------
; SquirrelJME is under the GNU General Public License v3+, or later.
; See license.mkd for licensing and copyright information.
; ---------------------------------------------------------------------------

.class lang/bytecode/TestNop
.super net/multiphasicapps/tac/TestRunnable

.method public <init>()V
	aload 0
	invokenonvirtual net/multiphasicapps/tac/TestRunnable/<init>()V
	return
.end method

.method public test()V

	; Nothing should happen here
	nop
	nop
	nop
	nop
	nop
	nop
	nop
	nop
	nop
	nop
	nop

	return
.end method

