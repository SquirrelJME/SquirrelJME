; -*- Mode: Jasmin; indent-tabs-mode: t; tab-width: 4 -*-
; ---------------------------------------------------------------------------
; Multi-Phasic Applications: SquirrelJME
;     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
; ---------------------------------------------------------------------------
; SquirrelJME is under the Mozilla Public License Version 2.0.
; See license.mkd for licensing and copyright information.
; ---------------------------------------------------------------------------

.class public nanocoat/bytecode/NanoTestIMul
.super java/lang/Object

.runtime_visible_annotation
	.annotation "Lnano/NanoDetails;"
		.elem .int_kind "expectedInteger" -1757483856
	.end .annotation
.end .annotation_attr

.method public static main([Ljava/lang/String;)V
.limit stack 6
; Load in values
	ldc 1234567
	ldc 42
	ldc 4
	ldc -1234
	
; Do the math
	imul
	imul
	imul

; Give the result
	invokestatic nano/NanoShelf/result(I)V
	return
.end method
