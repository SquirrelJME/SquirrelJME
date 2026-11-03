; -*- Mode: Jasmin; indent-tabs-mode: t; tab-width: 4 -*-
; ---------------------------------------------------------------------------
; Multi-Phasic Applications: SquirrelJME
;     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
; ---------------------------------------------------------------------------
; SquirrelJME is under the Mozilla Public License Version 2.0.
; See license.mkd for licensing and copyright information.
; ---------------------------------------------------------------------------

.class public nanocoat/bytecode/NanoTestSwapOO
.super java/lang/Object

.runtime_visible_annotation
	.annotation "Lnano/NanoDetails;"
		.elem .arr_kind "expectedStrings"
			.elem .str_kind "" "string2"
			.elem .str_kind "" "string1"
		.end .arr_elem
	.end .annotation
.end .annotation_attr

.method public static main([Ljava/lang/String;)V
.limit stack 2
; Two strings to swap
	ldc "string1"
	ldc "string2"
	
; Swap them
	swap
	
; Put them into an array
	invokestatic nano/NanoShelf/result(Ljava/lang/String;Ljava/lang/String;)V
	return
.end method
