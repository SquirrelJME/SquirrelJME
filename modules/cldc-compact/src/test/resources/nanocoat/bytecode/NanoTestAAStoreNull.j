; -*- Mode: Jasmin; indent-tabs-mode: t; tab-width: 4 -*-
; ---------------------------------------------------------------------------
; Multi-Phasic Applications: SquirrelJME
;     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
; ---------------------------------------------------------------------------
; SquirrelJME is under the Mozilla Public License Version 2.0.
; See license.mkd for licensing and copyright information.
; ---------------------------------------------------------------------------

.class public nanocoat/bytecode/NanoTestAAStoreNull
.super java/lang/Object

.runtime_visible_annotation
	.annotation "Lnano/NanoDetails;"
		.elem .str_kind "thrown" "Ljava/lang/NullPointerException;"
	.end .annotation
.end .annotation_attr

.method public static main([Ljava/lang/String;)V
.limit stack 4
; Grab an array
	invokestatic nano/NanoShelf/makeArrayNull()[Ljava/lang/String;
	
; Write a string to the first index
	iconst_0
	ldc "array0"
	aastore

; Done
	return
.end method
