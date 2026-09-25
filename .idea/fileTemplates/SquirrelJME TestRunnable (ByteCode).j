; -*- Mode: Jasmin; indent-tabs-mode: t; tab-width: 4 -*-
; ---------------------------------------------------------------------------
; SquirrelJME
;     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
; ---------------------------------------------------------------------------
; SquirrelJME is under the Mozilla Public License Version 2.0.
; See license.mkd for licensing and copyright information.
; ---------------------------------------------------------------------------

#set($origin="${PACKAGE_NAME}")
#set($slashy=$origin.replace('.', '/'))
.class public ${slashy}/${NAME}
.super net/multiphasicapps/tac/TestRunnable

.method public <init>()V
	aload 0
	invokenonvirtual net/multiphasicapps/tac/TestRunnable/<init>()V
	return
.end method

.method public test()V
.limit stack 2
	return
.end method
