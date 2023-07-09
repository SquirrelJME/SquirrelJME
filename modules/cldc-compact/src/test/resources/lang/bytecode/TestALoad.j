; -*- Mode: Jasmin; indent-tabs-mode: t; tab-width: 4 -*-
; ---------------------------------------------------------------------------
; Multi-Phasic Applications: SquirrelJME
;     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
; ---------------------------------------------------------------------------
; SquirrelJME is under the GNU General Public License v3+, or later.
; See license.mkd for licensing and copyright information.
; ---------------------------------------------------------------------------

.class public lang/bytecode/TestALoad
.super net/multiphasicapps/tac/TestSupplier

.method public <init>()V
	aload 0
	invokenonvirtual net/multiphasicapps/tac/TestSupplier/<init>()V
	return
.end method

.method private static __internal(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)[Ljava/lang/String;
.limit locals 8
.limit stack 4
; Setup array to store values into
	bipush 8
	anewarray java/lang/String
	
; Store 0
	dup
	bipush 0
	aload_0
	aastore
	
; Store 1
	dup
	bipush 1
	aload_1
	aastore
	
; Store 2
	dup
	bipush 2
	aload_2
	aastore
	
; Store 3
	dup
	bipush 3
	aload_3
	aastore
	
; Store 4
	dup
	bipush 4
	aload 4
	aastore
	
; Store 5
	dup
	bipush 5
	aload 5
	aastore
	
; Store 6
	dup
	bipush 6
	aload 6
	aastore
	
; Store 7
	dup
	bipush 7
	aload 7
	aastore

; Return array
	areturn
.end method

.method public test()Ljava/lang/Object;
.limit locals 2
.limit stack 9

; Call method and then just return its value
	ldc "a"
	ldc "b"
	ldc "c"
	ldc "d"
	ldc "e"
	ldc "f" 
	ldc "g"
	ldc "h"
	invokestatic lang/bytecode/TestALoad/__internal(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)[Ljava/lang/String;

; Return value
	areturn
.end method
