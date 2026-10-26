; -*- Mode: Jasmin; indent-tabs-mode: t; tab-width: 4 -*-
; ---------------------------------------------------------------------------
; Multi-Phasic Applications: SquirrelJME
;     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
; ---------------------------------------------------------------------------
; SquirrelJME is under the Mozilla Public License Version 2.0.
; See license.mkd for licensing and copyright information.
; ---------------------------------------------------------------------------

.class public nanocoat/bytecode/NanoTestBIPush
.super java/lang/Object

.runtime_visible_annotation
	.annotation "Lnano/NanoDetails;"
		.elem .int_kind "expectedInteger" 123
	.end .annotation
.end .annotation_attr

.method public static main([Ljava/lang/String;)V
.limit stack 2
; Put in some random value that is guaranteed to be random
	bipush 123

; Give the result
	invokestatic nano/NanoShelf/result(I)V
	return
.end method
