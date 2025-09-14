; -*- Mode: Jasmin; indent-tabs-mode: t; tab-width: 4 -*-
; ---------------------------------------------------------------------------
; Multi-Phasic Applications: SquirrelJME
;     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
; ---------------------------------------------------------------------------
; SquirrelJME is under the GNU General Public License v3+, or later.
; See license.mkd for licensing and copyright information.
; ---------------------------------------------------------------------------

#set($origin="${PACKAGE_NAME}")
#set($slashy=$origin.replace('.', '/'))
.class public ${slashy}/${NAME}
.super java/lang/Object

.field public static final "EXPECTED" I = 0

.method public static nanoTest()I
.limit stack 2
	bipush 0
	ireturn
.end method
