; -*- Mode: Jasmin; indent-tabs-mode: t; tab-width: 4 -*-
; ---------------------------------------------------------------------------
; Multi-Phasic Applications: SquirrelJME
;     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
; ---------------------------------------------------------------------------
; SquirrelJME is under the GNU General Public License v3+, or later.
; See license.mkd for licensing and copyright information.
; ---------------------------------------------------------------------------

.class public lang/bytecode/field/TestInstanceFieldConst
.super net/multiphasicapps/tac/TestSupplier

; Even though this would be ignored, Jasmin Sable adds a ConstantValue
; attribute for any field value so we can take advantage of it here
.field "test" I = 42

.method public <init>()V
	aload 0
	invokenonvirtual net/multiphasicapps/tac/TestSupplier/<init>()V
	return
.end method

.method public test()Ljava/lang/Object;
.limit stack 2
; Load in field
	aload 0
	getfield lang/bytecode/field/TestInstanceFieldConst/test I
	
; Box and return it
	invokestatic java/lang/Integer/valueOf(I)Ljava/lang/Integer;
	areturn
.end method
