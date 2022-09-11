; -*- Mode: Jasmin; indent-tabs-mode: t; tab-width: 4 -*-
; ---------------------------------------------------------------------------
; Multi-Phasic Applications: SquirrelJME
;     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
; ---------------------------------------------------------------------------
; SquirrelJME is under the GNU General Public License v3+, or later.
; See license.mkd for licensing and copyright information.
; ---------------------------------------------------------------------------

.class public MonitorEnterExit
.super java/lang/Object

.method public <init>()V
.limit vars 2
	aload 0
	invokenonvirtual java/lang/Object/<init>()V
	
	; Enter the monitor
	aload 0
	monitorenter
	
	; Leave the monitor
	aload 0
	monitorexit
	
	return
.end method
