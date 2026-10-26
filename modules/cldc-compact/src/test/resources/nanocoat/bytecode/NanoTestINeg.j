; -*- Mode: Jasmin; indent-tabs-mode: t; tab-width: 4 -*-
; ---------------------------------------------------------------------------
; Multi-Phasic Applications: SquirrelJME
;     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
; ---------------------------------------------------------------------------
; SquirrelJME is under the Mozilla Public License Version 2.0.
; See license.mkd for licensing and copyright information.
; ---------------------------------------------------------------------------

.class public nanocoat/bytecode/NanoTestINeg
.super java/lang/Object

.runtime_visible_annotation
	.annotation "Lnano/NanoDetails;"
		.elem .int_kind "expectedInteger" -1234567
	.end .annotation
.end .annotation_attr

.method public static main([Ljava/lang/String;)V
.limit stack 2
; Load in a value
	ldc 1234567
	
; Negate it
	ineg

; Give the result
	invokestatic nano/NanoShelf/result(I)V
	return
.end method
