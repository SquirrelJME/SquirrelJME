// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.cldc.io;

/**
 * Base for Shift-JIS.
 *
 * @since 2022/07/12
 */
public abstract class ShiftJisBase
	implements NamedCodec
{
	/**
	 * {@inheritDoc}
	 * @since 2021/06/13
	 */
	@Override
	public final double averageSequenceLength()
	{ 
		return 2.0D;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/06/13
	 */
	@Override
	public final String encodingName()
	{
		return "shift-jis";
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/06/13
	 */
	@Override
	public final int maximumSequenceLength()
	{
		return 2;
	}
}
