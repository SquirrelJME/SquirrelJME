// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package com.nttdocomo.ui;

import cc.squirreljme.runtime.cldc.annotation.Api;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.io.IOException;
import java.io.InputStream;

@SuppressWarnings("SpellCheckingInspection")
@Api
public class PalettedImage
	extends Image
{
	@Override
	@Api
	public void dispose()
	{
		throw Debugging.todo();
	}
	
	@Api
	public static PalettedImage createPalettedImage(byte[] __in)
		throws IOException, NullPointerException
	{
		throw Debugging.todo();
	}
	
	@Api
	public static PalettedImage createPalettedImage(InputStream __in)
		throws IOException, NullPointerException
	{
		throw Debugging.todo();
	}
}
