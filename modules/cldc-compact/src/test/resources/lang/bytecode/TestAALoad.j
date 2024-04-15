; -*- Mode: Jasmin; indent-tabs-mode: t; tab-width: 4 -*-
; ---------------------------------------------------------------------------
; SquirrelJME
;     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
; ---------------------------------------------------------------------------
; SquirrelJME is under the Mozilla Public License Version 2.0.
; See license.mkd for licensing and copyright information.
; ---------------------------------------------------------------------------

.class public lang/bytecode/TestAALoad
.super net/multiphasicapps/tac/TestSupplier

.method public <init>()V
	aload 0
	invokenonvirtual net/multiphasicapps/tac/TestSupplier/<init>()V
	return
.end method

.method public test()Ljava/lang/Object;
.limit stack 2

; Obtain array
	invokestatic lang/bytecode/ByteCodeUtil/makeStringArray()[Ljava/lang/String;
	
; Load from array
	bipush 3
	aaload
	
; Return value
	areturn
.end method
