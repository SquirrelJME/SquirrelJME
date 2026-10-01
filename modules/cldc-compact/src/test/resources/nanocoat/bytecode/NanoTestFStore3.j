; -*- Mode: Jasmin; indent-tabs-mode: t; tab-width: 4 -*-
; ---------------------------------------------------------------------------
; Multi-Phasic Applications: SquirrelJME
;     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
; ---------------------------------------------------------------------------
; SquirrelJME is under the Mozilla Public License Version 2.0.
; See license.mkd for licensing and copyright information.
; ---------------------------------------------------------------------------

.class public nanocoat/bytecode/NanoTestFStore3
.super java/lang/Object

.runtime_visible_annotation
	.annotation "Lnano/NanoDetails;"
	.end .annotation
.end .annotation_attr

.method public static main([Ljava/lang/String;)V
.limit stack 2
	invokestatic nano/NanoShelf/todo()V
	return
.end method
