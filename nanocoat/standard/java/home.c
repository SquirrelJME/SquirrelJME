/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "java.h"

void foo(void)
{
	/* Windows. */
	/* HKEY_LOCAL_MACHINE\SOFTWARE\JavaSoft */
	
	/* Linux. */
	/* /usr/lib/jvm/default-java (Debian) */
	/* /usr/lib/jvm/default (Arch) */
	/* /usr/lib/jvm/default-runtime (Arch) */
	/* /etc/alternatives/jre (RHEL) */
	
	/* Mac OS. */
	/* /System/Library/Frameworks/JavaVM.framework/Versions/Current */
	
	/* BSD. */
	/* /usr/local/etc/javavms */
	
	/* Any. */
	/* $SQUIRRELJME_JAVA_HOME */
	/* $JAVA_HOME */
}
