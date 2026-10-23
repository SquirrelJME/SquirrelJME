; -*- Mode: Jasmin; indent-tabs-mode: t; tab-width: 4 -*-
; ---------------------------------------------------------------------------
; Multi-Phasic Applications: SquirrelJME
;     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
; ---------------------------------------------------------------------------
; SquirrelJME is under the Mozilla Public License Version 2.0.
; See license.mkd for licensing and copyright information.
; ---------------------------------------------------------------------------

.class public nanocoat/bytecode/NanoTestAAStore
.super java/lang/Object

.runtime_visible_annotation
	.annotation "Lnano/NanoDetails;"
		.elem .arr_kind "expectedStrings"
			.elem .str_kind "" "array0"
			.elem .str_kind "" "string1"
			.elem .str_kind "" "string2"
			.elem .str_kind "" "string3"
			.elem .str_kind "" "string4"
		.end .arr_elem
	.end .annotation
.end .annotation_attr

.method public static main([Ljava/lang/String;)V
.limit stack 4
; Grab an array
	iconst_5
	invokestatic nano/NanoShelf/makeArrayString(I)[Ljava/lang/String;
	dup
	
; Write a string to the first index
	iconst_0
	ldc "array0"
	aastore

; Result
	invokestatic nano/NanoShelf/result([Ljava/lang/String;)V

; Done
	return
.end method
