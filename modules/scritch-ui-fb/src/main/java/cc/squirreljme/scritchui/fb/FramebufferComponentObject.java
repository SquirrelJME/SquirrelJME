// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.scritchui.fb;

import cc.squirreljme.jvm.mle.constants.UIPixelFormat;
import cc.squirreljme.jvm.mle.scritchui.ScritchInterface;
import cc.squirreljme.jvm.mle.scritchui.brackets.ScritchComponentBracket;
import cc.squirreljme.jvm.mle.scritchui.brackets.ScritchPanelBracket;
import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import java.lang.ref.Reference;
import org.intellij.lang.annotations.MagicConstant;
import org.jetbrains.annotations.Async;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

/**
 * Base class for all component types.
 *
 * @since 2024/03/26
 */
@SquirrelJMEVendorApi
public abstract class FramebufferComponentObject
	extends FramebufferBaseObject
	implements ScritchComponentBracket
{
	/** The panel this is based upon. */
	@SquirrelJMEVendorApi
	protected final ScritchPanelBracket corePanel;
	
	/**
	 * Initializes the base component object.
	 *
	 * @param __selfApi The reference to our own API.
	 * @param __coreApi The core API for accessing wrapped objects.
	 * @param __corePanel The core panel to draw on top of.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/03/26
	 */
	@SquirrelJMEVendorApi
	public FramebufferComponentObject(
		Reference<FramebufferScritchInterface> __selfApi,
		ScritchInterface __coreApi,
		ScritchPanelBracket __corePanel)
		throws NullPointerException
	{
		super(__selfApi, __coreApi);
		
		if (__corePanel == null)
			throw new NullPointerException("NARG");
		
		// Store for later
		this.corePanel = __corePanel;
		
		// We handle drawing everything ourselves depending on the widget we
		// will be drawing for
		__coreApi.panel().setPaintListener(__corePanel,
			new __PaintListener__(this));
	}
	
	/**
	 * Internal painting logic for this component.
	 *
	 * @param __pf The {@link UIPixelFormat} used for the draw.
	 * @param __bw The buffer width, this is the scanline width of the buffer.
	 * @param __bh The buffer height.
	 * @param __buf The target buffer to draw to, this is cast to the correct
	 * buffer format.
	 * @param __offset The offset to the start of the buffer.
	 * @param __pal The color palette, may be {@code null}.
	 * @param __sx Starting surface X coordinate.
	 * @param __sy Starting surface Y coordinate.
	 * @param __sw Surface width.
	 * @param __sh Surface height.
	 * @param __special Special value for painting, may be {@code 0} or any
	 * other value if it is meaningful to what is being painted.
	 * @since 2024/03/26
	 */
	@SquirrelJMEVendorApi
	@Async.Execute
	abstract void __paintInternal(
		@MagicConstant(valuesFromClass = UIPixelFormat.class) int __pf,
		@Range(from = 0, to = Integer.MAX_VALUE) int __bw,
		@Range(from = 0, to = Integer.MAX_VALUE) int __bh,
		@NotNull Object __buf,
		@Range(from = 0, to = Integer.MAX_VALUE) int __offset,
		@Nullable int[] __pal,
		@Range(from = 0, to = Integer.MAX_VALUE) int __sx,
		@Range(from = 0, to = Integer.MAX_VALUE) int __sy,
		@Range(from = 0, to = Integer.MAX_VALUE) int __sw,
		@Range(from = 0, to = Integer.MAX_VALUE) int __sh,
		int __special);
	
	/**
	 * Common focus enablement.
	 *
	 * @param __enabled Should focus be enabled?
	 * @since 2024/03/26
	 */
	protected void __enableFocus(boolean __enabled)
	{
		this.coreApi.panel().enableFocus(this.corePanel, __enabled);
	}
}
