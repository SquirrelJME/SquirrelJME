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
import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import org.intellij.lang.annotations.MagicConstant;

/**
 * Base abstract class for stream based connections.
 *
 * @since 2025/12/27
 */
@SquirrelJMEVendorApi
public abstract class AbstractStreamConnection
	extends AbstractBaseConnection
	implements StreamConnection
{
	/**
	 * Initializes the base connection.
	 *
	 * @param __mode The mode this is opened in.
	 * @since 2025/12/27
	 */
	protected AbstractStreamConnection(
		@MagicConstant(valuesFromClass = Connector.class) int __mode)
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
	
	/**
	 * {@inheritDoc}
	 * @since 2025/12/27
	 */
	@Override
	public final DataOutputStream openDataOutputStream()
		throws IOException
	{
		return new DataOutputStream(this.openOutputStream());
	}
}
