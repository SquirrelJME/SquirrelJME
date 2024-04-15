; -*- Mode: Jasmin; indent-tabs-mode: t; tab-width: 4 -*-
; ---------------------------------------------------------------------------
; SquirrelJME
;     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
; ---------------------------------------------------------------------------
; SquirrelJME is under the Mozilla Public License Version 2.0.
; See license.mkd for licensing and copyright information.
; ---------------------------------------------------------------------------

.class public lang/bytecode/field/TestStaticSuperFieldByInherit
.super net/multiphasicapps/tac/TestSupplier

.method public <init>()V
	aload 0
	invokenonvirtual net/multiphasicapps/tac/TestSupplier/<init>()V
	return
.end method

.method public test()Ljava/lang/Object;
.limit stack 2
	; Read value from the B field
	getstatic lang/bytecode/field/ClassB/VALUE Ljava/lang/String;
	areturn
.end method
