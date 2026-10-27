; -*- Mode: Jasmin; indent-tabs-mode: t; tab-width: 4 -*-
; ---------------------------------------------------------------------------
; Multi-Phasic Applications: SquirrelJME
;     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
; ---------------------------------------------------------------------------
; SquirrelJME is under the Mozilla Public License Version 2.0.
; See license.mkd for licensing and copyright information.
; ---------------------------------------------------------------------------

.class public nanocoat/bytecode/NanoTestIInc
.super java/lang/Object

.runtime_visible_annotation
	.annotation "Lnano/NanoDetails;"
		.elem .int_kind "expectedInteger" 2468
	.end .annotation
.end .annotation_attr

.method public static main([Ljava/lang/String;)V
.limit locals 8
.limit stack 6
; Store value
	ldc 1234
	istore_1
	
; Increment
	iinc 1 1234
	
; Load back in and give the result
	iload_1
	invokestatic nano/NanoShelf/result(I)V
	return
.end method
