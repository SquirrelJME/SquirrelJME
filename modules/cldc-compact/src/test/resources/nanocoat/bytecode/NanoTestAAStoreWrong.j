; -*- Mode: Jasmin; indent-tabs-mode: t; tab-width: 4 -*-
; ---------------------------------------------------------------------------
; Multi-Phasic Applications: SquirrelJME
;     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
; ---------------------------------------------------------------------------
; SquirrelJME is under the Mozilla Public License Version 2.0.
; See license.mkd for licensing and copyright information.
; ---------------------------------------------------------------------------

.class public nanocoat/bytecode/NanoTestAAStoreWrong
.super java/lang/Object

.runtime_visible_annotation
	.annotation "Lnano/NanoDetails;"
		.elem .str_kind "expectedException" "Ljava/lang/ArrayStoreException;"
	.end .annotation
.end .annotation_attr

.method public static main([Ljava/lang/String;)V
.limit stack 4
; Grab an array
	iconst_5
	invokestatic nano/NanoShelf/makeArrayString(I)[Ljava/lang/String;
	
; First index
	iconst_0
	
; Make an integer type instead
	new java/lang/Integer
	dup 
	iconst_2
	invokenonvirtual java/lang/Integer/<init>(I)V
	
; Actually store it
	aastore

; Done
	return
.end method
