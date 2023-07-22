// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package com.jblend.util;

import cc.squirreljme.runtime.cldc.annotation.Api;
import cc.squirreljme.runtime.cldc.debug.Debugging;

public final class Secure
{
	@Api
	public static final String javaPackageName = "java.";
	
	@Api
	public static final String javaxPackageName = "javax.";
	
	@Api
	public static void checkPackage()
	{
		throw Debugging.todo();
	}
	
	@Api
	public static void checkPackage(String var0)
	{
		throw Debugging.todo();
	}
}
