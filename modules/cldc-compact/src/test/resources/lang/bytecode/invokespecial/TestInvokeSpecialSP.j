; -*- Mode: Jasmin; indent-tabs-mode: t; tab-width: 4 -*-
; ---------------------------------------------------------------------------
; SquirrelJME
;     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
; ---------------------------------------------------------------------------
; SquirrelJME is under the Mozilla Public License Version 2.0.
; See license.mkd for licensing and copyright information.
; ---------------------------------------------------------------------------
; Test `invokespecial` on a public method in the same class, which
; `invokevirtual` should be used instead. There is a DoJa obfuscator out there
; that does this.

.class public lang/bytecode/invokespecial/TestInvokeSpecialSP
.super net/multiphasicapps/tac/TestInteger

.method public <init>()V
	aload 0
	invokenonvirtual net/multiphasicapps/tac/TestInteger/<init>()V
	return
.end method

.method public test()I
.limit stack 2
	aload_0
	invokenonvirtual lang/bytecode/invokespecial/TestInvokeSpecialSP/method()I

	ireturn
.end method
	
.method public method()I
.limit stack 1
	ldc 1337
	ireturn
.end method
