; -*- Mode: Jasmin; indent-tabs-mode: t; tab-width: 4 -*-
; ---------------------------------------------------------------------------
; Multi-Phasic Applications: SquirrelJME
;     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
; ---------------------------------------------------------------------------
; SquirrelJME is under the Mozilla Public License Version 2.0.
; See license.mkd for licensing and copyright information.
; ---------------------------------------------------------------------------

.class public nanocoat/bytecode/NanoTestIfEq
.super java/lang/Object

.runtime_visible_annotation
	.annotation "Lnano/NanoDetails;"
		.elem .bool_kind "expectedVoid" 1
	.end .annotation
.end .annotation_attr

.method public static main([Ljava/lang/String;)V
.limit stack 6
; Passing check
	ldc 0
	
	ifeq pass1
	goto fail1
	
; Failing check
pass1:
	ldc 1234
	
	ifeq fail2
	invokestatic nano/NanoShelf/result()V
	
fail1:
fail2:
	return
.end method
