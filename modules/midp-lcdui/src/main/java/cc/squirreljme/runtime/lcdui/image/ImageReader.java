// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.lcdui.image;

import java.io.IOException;

/**
 * Base interface for all image readers.
 *
 * @since 2024/01/14
 */
public interface ImageReader
{
	/**
	 * Parses the image.
	 * 
	 * @throws IOException On null arguments.
	 * @since 2021/12/04
	 */
	void parse()
		throws IOException;
}
