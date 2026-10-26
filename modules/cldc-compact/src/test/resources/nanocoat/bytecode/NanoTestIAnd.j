; -*- Mode: Jasmin; indent-tabs-mode: t; tab-width: 4 -*-
; ---------------------------------------------------------------------------
; Multi-Phasic Applications: SquirrelJME
;     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
; ---------------------------------------------------------------------------
; SquirrelJME is under the Mozilla Public License Version 2.0.
; See license.mkd for licensing and copyright information.
; ---------------------------------------------------------------------------

.class public nanocoat/bytecode/NanoTestIAnd
.super java/lang/Object

.runtime_visible_annotation
	.annotation "Lnano/NanoDetails;"
		.elem .int_kind "expectedInteger" 2364928
	.end .annotation
.end .annotation_attr

.method public static main([Ljava/lang/String;)V
.limit stack 6
; Push a bunch of constants.
	ldc 0xDEADBEEF
	ldc 0x00FFFFFF
	ldc 0xFFFFFF00
	ldc 0x12345678
	
; Operate on them
	iand
	iand
	iand
	
; Give the result of it
	invokestatic nano/NanoShelf/result(I)V
	return
.end method
