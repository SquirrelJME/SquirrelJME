// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.scritchui.fb;

import cc.squirreljme.jvm.mle.scritchui.brackets.ScritchComponentBracket;
import cc.squirreljme.jvm.mle.scritchui.brackets.ScritchPencilBracket;
import cc.squirreljme.jvm.mle.scritchui.callbacks.ScritchPaintListener;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

/**
 * Forwards paint calls to the component.
 *
 * @since 2024/03/26
 */
@Deprecated
final class __PaintListener__
	implements ScritchPaintListener
{
	/** The component to draw on. */
	protected final Reference<FramebufferComponentObject> component;
	
	/**
	 * Initializes the paint listener.
	 *
	 * @param __component The component to draw on.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/03/26
	 */
	__PaintListener__(FramebufferComponentObject __component)
		throws NullPointerException
	{
		if (__component == null)
			throw new NullPointerException("NARG");
		
		this.component = new WeakReference<>(__component);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/03/26
	 */
	@Override
	public void paint(ScritchComponentBracket __component,
		ScritchPencilBracket __g, int __sw, int __sh, int __special)
	{
		// Do nothing if it was GCed
		FramebufferComponentObject forward = this.component.get();
		if (forward == null)
			return;
		
		// Forward
		forward.__paintInternal(__g, __sw, __sh, __special);
	}
}
