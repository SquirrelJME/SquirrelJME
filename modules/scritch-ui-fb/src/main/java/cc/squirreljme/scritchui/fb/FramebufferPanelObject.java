// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.scritchui.fb;

import cc.squirreljme.jvm.mle.scritchui.ScritchInterface;
import cc.squirreljme.jvm.mle.scritchui.brackets.ScritchComponentBracket;
import cc.squirreljme.jvm.mle.scritchui.brackets.ScritchPanelBracket;
import cc.squirreljme.jvm.mle.scritchui.callbacks.ScritchPaintListener;
import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.lang.ref.Reference;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

/**
 * Framebuffer based panel.
 *
 * @since 2024/03/26
 */
@SquirrelJMEVendorApi
public class FramebufferPanelObject
	extends FramebufferComponentObject
	implements ScritchPanelBracket
{
	/** The listener to call on paint events. */
	private volatile ScritchPaintListener _paintListener;
	
	/**
	 * Initializes the framebuffer panel.
	 *
	 * @param __selfApi Our own API.
	 * @param __coreApi The native API to access.
	 * @param __corePanel The panel that exists at the core API level.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/03/26
	 */
	public FramebufferPanelObject(
		Reference<FramebufferScritchInterface> __selfApi,
		ScritchInterface __coreApi, ScritchPanelBracket __corePanel)
		throws NullPointerException
	{
		super(__selfApi, __coreApi, __corePanel);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/03/26
	 */
	@Override
	void __paintInternal(int __pf, int __bw, int __bh,
		Object __buf, int __offset, int[] __pal, int __sx, int __sy,
		int __sw, int __sh, int __special)
	{
		// Forward to the canvas listener
		ScritchPaintListener listener = this._paintListener;
		if (listener != null)
			listener.paint(this, __pf, __bw, __bh, __buf, __offset,
				__pal, __sx, __sy, __sw, __sh, __special);
	}
	
	/**
	 * Sets or clears the layered paint handler.
	 *
	 * @param __listener The listener to call into when additionally drawing.
	 * @since 2024/03/26
	 */
	void __setPaintListener(ScritchPaintListener __listener)
	{
		this._paintListener = __listener;
	}
}
