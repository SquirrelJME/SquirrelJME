; -*- Mode: Jasmin; indent-tabs-mode: t; tab-width: 4 -*-
; ---------------------------------------------------------------------------
; Multi-Phasic Applications: SquirrelJME
;     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
; ---------------------------------------------------------------------------
; SquirrelJME is under the GNU General Public License v3+, or later.
; See license.mkd for licensing and copyright information.
; ---------------------------------------------------------------------------

.class public BasicForLoop
.super java/lang/Object

.method public <init>()V
.limit vars 3
	aload 0
	invokenonvirtual java/lang/Object/<init>()V
	
	; for (int i = 0, n = 10;...;...)
	; Zero
	bipush 0
	istore 1
	
	; Loop count
	bipush 10
	istore 2
	
	; for(...; i < n; ...) {
	iload 1
	iload 2
	if_icmpge loopEnded ; (i >= n)
	
	; i++
	iinc 1 1

	; }
loopEnded:
	
	return
.end method
