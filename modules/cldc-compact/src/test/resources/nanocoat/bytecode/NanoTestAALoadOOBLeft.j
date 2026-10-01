; -*- Mode: Jasmin; indent-tabs-mode: t; tab-width: 4 -*-
; ---------------------------------------------------------------------------
; Multi-Phasic Applications: SquirrelJME
;     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
; ---------------------------------------------------------------------------
; SquirrelJME is under the Mozilla Public License Version 2.0.
; See license.mkd for licensing and copyright information.
; ---------------------------------------------------------------------------

.class public nanocoat/bytecode/NanoTestAALoadOOBLeft
.super java/lang/Object

.runtime_visible_annotation
	.annotation "Lnano/NanoDetails;"
		.elem .str_kind "expectedException" "Ljava/lang/ArrayIndexOutOfBoundsException;"
	.end .annotation
.end .annotation_attr

.method public static main([Ljava/lang/String;)V
.limit stack 2
; Grab an array
	iconst_5
	invokestatic nano/NanoShelf/makeArrayString(I)[Ljava/lang/String;
	
; Read the negative value from it
	iconst_m1
	aaload

; Done
	return
.end method
