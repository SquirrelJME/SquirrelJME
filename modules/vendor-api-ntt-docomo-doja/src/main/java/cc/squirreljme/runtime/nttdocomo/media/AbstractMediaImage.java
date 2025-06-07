// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.nttdocomo.media;

import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import com.nttdocomo.ui.MediaImage;
import javax.microedition.io.InputConnection;

/**
 * Base class for media images.
 *
 * @since 2025/05/05
 */
@SquirrelJMEVendorApi
public abstract class AbstractMediaImage
	extends AbstractMediaResource
	implements MediaImage
{
	/**
	 * Initializes the base resource with the resource connection.
	 *
	 * @param __source The original input for data.
	 * @throws NullPointerException On null arguments.
	 * @since 2025/05/05
	 */
	protected AbstractMediaImage(InputConnection __source)
		throws NullPointerException
	{
		super(__source);
	}
}
