; -*- Mode: Jasmin; indent-tabs-mode: t; tab-width: 4 -*-
; ---------------------------------------------------------------------------
; Multi-Phasic Applications: SquirrelJME
;     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
; ---------------------------------------------------------------------------
; SquirrelJME is under the GNU General Public License v3+, or later.
; See license.mkd for licensing and copyright information.
; ---------------------------------------------------------------------------

.class public nanocoat/NanoTestByteCodeNop
.super java/lang/Object

.field public static final "EXPECTED_VOID" I = 0

.method public static main(Ljava/lang/String;)V
.limit stack 2
	nop
	return
.end method
