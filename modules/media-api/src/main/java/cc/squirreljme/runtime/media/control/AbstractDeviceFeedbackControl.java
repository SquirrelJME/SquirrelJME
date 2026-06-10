// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.media.control;

import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.runtime.media.AbstractControl;

/**
 * Implements device feedback.
 *
 * @since 2026/06/10
 */
@SquirrelJMEVendorApi
public class AbstractDeviceFeedbackControl
	extends AbstractControl<DeviceFeedbackControl>
	implements DeviceFeedbackControl
{
	/**
	 * Initializes the given control.
	 *
	 * @since 2026/06/10
	 */
	public AbstractDeviceFeedbackControl()
	{
		super(DeviceFeedbackControl.class);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2026/06/10
	 */
	@Override
	public void addListener(DeviceFeedbackListener __listener)
		throws NullPointerException
	{
		if (__listener == null)
			throw new NullPointerException();
		
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2026/06/10
	 */
	@Override
	public void emitLight(boolean __large, int __ms)
		throws IllegalArgumentException
	{
		if (__ms < 0)
			throw new IllegalArgumentException("NEGV");
		
		Debugging.todoNote("Media light event");
	}
	
	/**
	 * {@inheritDoc} 
	 * @since 2026/06/10
	 */
	@Override
	public void emitVibrate(int __ms)
		throws IllegalArgumentException
	{
		if (__ms < 0)
			throw new IllegalArgumentException("NEGV");
		
		Debugging.todoNote("Media vibrate event");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2026/06/10
	 */
	@Override
	public void removeListener(DeviceFeedbackListener __listener)
		throws NullPointerException
	{
		if (__listener == null)
			throw new NullPointerException();
		
		throw Debugging.todo();
	}
}
