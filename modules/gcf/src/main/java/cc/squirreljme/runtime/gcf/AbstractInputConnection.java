// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.gcf;

import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import java.io.DataInputStream;
import java.io.IOException;
import javax.microedition.io.Connector;
import javax.microedition.io.InputConnection;
import org.intellij.lang.annotations.MagicConstant;

/**
 * Abstract base class for input connections.
 *
 * @since 2026/01/08
 */
@SquirrelJMEVendorApi
public abstract class AbstractInputConnection
	extends AbstractBaseConnection
	implements InputConnection
{
	/**
	 * Initializes the base connection.
	 *
	 * @param __mode The mode this is opened in.
	 * @throws IllegalArgumentException If the connection mode is not valid.
	 * @since 2026/01/08
	 */
	@SquirrelJMEVendorApi
	protected AbstractInputConnection(
		@MagicConstant(flagsFromClass = Connector.class) int __mode)
		throws IllegalArgumentException
	{
		super(__mode);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/12/27
	 */
	@Override
	public final DataInputStream openDataInputStream()
		throws IOException
	{
		return new DataInputStream(this.openInputStream());
	}
}
