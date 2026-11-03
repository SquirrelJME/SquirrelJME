; -*- Mode: Jasmin; indent-tabs-mode: t; tab-width: 4 -*-
; ---------------------------------------------------------------------------
; Multi-Phasic Applications: SquirrelJME
;     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
; ---------------------------------------------------------------------------
; SquirrelJME is under the Mozilla Public License Version 2.0.
; See license.mkd for licensing and copyright information.
; ---------------------------------------------------------------------------

.class public nanocoat/bytecode/NanoTestGotoW
.super java/lang/Object

.runtime_visible_annotation
	.annotation "Lnano/NanoDetails;"
		.elem .bool_kind "expectedVoid" 1
	.end .annotation
.end .annotation_attr

.method public static main([Ljava/lang/String;)V
.limit stack 2
; A goto which is rather unconditional
; Hopefully Jasmin emits an actual goto_w and not a goto, because otherwise
; there are going to be 65536 lines of nop or something else...
; stack=2, locals=1, args_size=1
;  0: iconst_0
;  1: ifne          9
;  4: goto_w        16
;  9: nop
; Luckily it does, so yay! So this means no absurdly lined nops and no
; absurdly padded lookupswitch 
	iconst_0
	ifne no
	goto_w yes

; nop filler
no:
	nop
	nop
	nop
	nop
	nop
	nop
	return
	
yes:
	invokestatic nano/NanoShelf/result()V
	return
.end method
