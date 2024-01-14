; -*- Mode: Jasmin; indent-tabs-mode: t; tab-width: 4 -*-
; ---------------------------------------------------------------------------
; Multi-Phasic Applications: SquirrelJME
;     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
; ---------------------------------------------------------------------------
; SquirrelJME is under the GNU General Public License v3+, or later.
; See license.mkd for licensing and copyright information.
; ---------------------------------------------------------------------------

; Tests special invocation elsewhere, that is a package private method in
; another class

.class public lang/bytecode/TestInvokeSpecialPPInSuper
.super lang/bytecode/__SuperWithPP__

.method public <init>()V
	aload 0
	invokenonvirtual lang/bytecode/__SuperWithPP__/<init>()V
	return
.end method

.method public test()I
.limit stack 3
; Perform the call
	aload_0
	invokenonvirtual lang/bytecode/__SuperWithPP__/__method()I

; Return resultant value
	ireturn
.end method
