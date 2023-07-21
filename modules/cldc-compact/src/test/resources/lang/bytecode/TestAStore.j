; -*- Mode: Jasmin; indent-tabs-mode: t; tab-width: 4 -*-
; ---------------------------------------------------------------------------
; SquirrelJME
;     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
; ---------------------------------------------------------------------------
; SquirrelJME is under the Mozilla Public License Version 2.0.
; See license.mkd for licensing and copyright information.
; ---------------------------------------------------------------------------

.class public lang/bytecode/TestAStore
.super net/multiphasicapps/tac/TestSupplier

.method public <init>()V
	aload 0
	invokenonvirtual net/multiphasicapps/tac/TestSupplier/<init>()V
	return
.end method

; Store then load 0
.method private static __0()Ljava/lang/String;
.limit locals 8
.limit stack 3
	ldc "a"
	astore_0
	aload_0
	areturn
.end method

; Store then load 1
.method private static __1()Ljava/lang/String;
.limit locals 8
.limit stack 3
	ldc "b"
	astore_1
	aload_1
	areturn
.end method

; Store then load 2
.method private static __2()Ljava/lang/String;
.limit locals 8
.limit stack 3
	ldc "c"
	astore_2
	aload_2
	areturn
.end method

; Store then load 3
.method private static __3()Ljava/lang/String;
.limit locals 8
.limit stack 3
	ldc "d"
	astore_3
	aload_3
	areturn
.end method

; Store then load 4
.method private static __4()Ljava/lang/String;
.limit locals 8
.limit stack 3
	ldc "e"
	astore 4
	aload 4
	areturn
.end method

; Store then load 5
.method private static __5()Ljava/lang/String;
.limit locals 8
.limit stack 3
	ldc "f"
	astore 5
	aload 5
	areturn
.end method

; Store then load 6
.method private static __6()Ljava/lang/String;
.limit locals 8
.limit stack 3
	ldc "g"
	astore 6
	aload 6
	areturn
.end method

; Store then load 7
.method private static __7()Ljava/lang/String;
.limit locals 8
.limit stack 3
	ldc "h"
	astore 7
	aload 7
	areturn
.end method

.method public test()Ljava/lang/Object;
.limit stack 4
; Create array
	bipush 8
	anewarray java/lang/String

; Store 0
	dup
	bipush 0
	invokestatic lang/bytecode/TestAStore/__0()Ljava/lang/String;
	aastore

; Store 1
	dup
	bipush 1
	invokestatic lang/bytecode/TestAStore/__1()Ljava/lang/String;
	aastore

; Store 2
	dup
	bipush 2
	invokestatic lang/bytecode/TestAStore/__2()Ljava/lang/String;
	aastore

; Store 3
	dup
	bipush 3
	invokestatic lang/bytecode/TestAStore/__3()Ljava/lang/String;
	aastore

; Store 4
	dup
	bipush 4
	invokestatic lang/bytecode/TestAStore/__4()Ljava/lang/String;
	aastore

; Store 5
	dup
	bipush 5
	invokestatic lang/bytecode/TestAStore/__5()Ljava/lang/String;
	aastore

; Store 6
	dup
	bipush 6
	invokestatic lang/bytecode/TestAStore/__6()Ljava/lang/String;
	aastore

; Store 7
	dup
	bipush 7
	invokestatic lang/bytecode/TestAStore/__7()Ljava/lang/String;
	aastore
	
; Return the array itself
	areturn
.end method
