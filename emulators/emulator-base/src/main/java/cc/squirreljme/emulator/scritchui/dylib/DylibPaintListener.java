// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.emulator.scritchui.dylib;

import cc.squirreljme.jvm.mle.constants.UIPixelFormat;
import cc.squirreljme.jvm.mle.scritchui.brackets.ScritchComponentBracket;
import cc.squirreljme.jvm.mle.scritchui.brackets.ScritchPencilBracket;
import cc.squirreljme.jvm.mle.scritchui.callbacks.ScritchPaintListener;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.util.function.Supplier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

/**
 * Wrapped listener for paints.
 *
 * @since 2024/04/24
 */
public class DylibPaintListener
	implements ScritchPaintListener
{
	/** The listener to forward to. */
	protected final ScritchPaintListener listener;
	
	/**
	 * Initializes the wrapped listener.
	 *
	 * @param __listener The listener to call.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/04/24
	 */
	public DylibPaintListener(ScritchPaintListener __listener)
		throws NullPointerException
	{
		if (__listener == null)
			throw new NullPointerException("NARG");
		
		this.listener = __listener;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/05/04
	 */
	@Override
	public void paint(@NotNull ScritchComponentBracket __component,
		@NotNull ScritchPencilBracket __g,
		@Range(from = 0, to = Integer.MAX_VALUE) int __sw,
		@Range(from = 0, to = Integer.MAX_VALUE) int __sh, int __special)
	{
		this.listener.paint(__component, __g, __sw, __sh, __special);
	}
}
