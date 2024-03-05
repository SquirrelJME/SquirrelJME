// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.nttdocomo.io;

import cc.squirreljme.jvm.launch.IModeApplication;
import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import cc.squirreljme.runtime.cldc.util.IntegerList;
import cc.squirreljme.runtime.cldc.util.StringUtils;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

/**
 * Parameters for the scratch pad.
 *
 * @since 2021/12/01
 */
@SquirrelJMEVendorApi
public final class ScratchPadParams
{
	/** The maximum number of allowed scratch pads. */
	@SquirrelJMEVendorApi
	public static final int MAX_SCRATCH_PADS = 16;
	
	/** Declared parameters. */
	private static volatile Reference<ScratchPadParams> _params;
	
	/** The sizes of the scratch pads. */
	private final int[] _sizes;
	
	/**
	 * Initializes the parameters.
	 *
	 * @param __sizes The sizes to use for the scratch pads.
	 * @throws NullPointerException On null arguments.
	 * @since 2021/12/01
	 */
	@SquirrelJMEVendorApi
	public ScratchPadParams(int... __sizes)
		throws NullPointerException
	{
		if (__sizes == null)
			throw new NullPointerException("NARG");
		
		this._sizes = __sizes;
	}
	
	/**
	 * Returns the number of available scratch pads.
	 *
	 * @return The number of scratch pads available.
	 * @since 2021/12/01
	 */
	@SquirrelJMEVendorApi
	public int count()
	{
		return this._sizes.length;
	}
	
	/**
	 * Gets the given scratch pad size.
	 *
	 * @param __i The index to get.
	 * @return The size to use.
	 * @throws IndexOutOfBoundsException If this is not a valid scratchpad.
	 * @since 2021/12/01
	 */
	@SquirrelJMEVendorApi
	public int getLength(int __i)
		throws IndexOutOfBoundsException
	{
		return this._sizes[__i];
	}
	
	/**
	 * Loads the scratch pad parameters.
	 *
	 * @return The scratch pad parameters.
	 * @since 2021/12/01
	 */
	static ScratchPadParams __load()
	{
		// Use pre-existing reference?
		Reference<ScratchPadParams> ref = ScratchPadParams._params;
		ScratchPadParams rv = (ref != null ? ref.get() : null);
		if (rv != null)
			return rv;
		
		// This system property comes from the SquirrelJME launch API
		String prop = System.getProperty(
			IModeApplication.SCRATCH_PAD_PROPERTY);
		if (prop == null)
			return null;
		
		// Load in all the various sizes, assuming it was declared
		IntegerList sizes = new IntegerList();
		if (!prop.isEmpty())
			for (String item : StringUtils.basicSplit(',', prop))
			{
				// {@squirreljme.error AH06 Scratch pad property has an empty
				// item within the size list. (The property)}
				if (item == null || item.isEmpty())
					throw new IllegalArgumentException("AH06 " + prop);
				
				// Decode the number which represents the byte count
				try
				{
					// {@squirreljme.error AH08 Scratch pad property has an
					// item which is an empty size. (The property)}
					int val = Integer.parseInt(item, 10);
					if (val < 0)
						throw new IllegalArgumentException("AH08 " + prop);
					
					sizes.addInteger(val);
				}
				catch (NumberFormatException __e)
				{
					// {@squirreljme.error AH07 Scratch pad property is badly
					// formatted and contains an invalid number. (The
					// property)}
					throw new IllegalArgumentException("AH07 " + prop, __e);
				}
			}
		
		// {@squirreljme.error AH09 Too many scratch pads were requested for
		// the application. (The property)}
		if (sizes.size() > ScratchPadParams.MAX_SCRATCH_PADS)
			throw new IllegalArgumentException("AH09 " + prop);
		
		// Cache and use it
		rv = new ScratchPadParams(sizes.toIntegerArray());
		ScratchPadParams._params = new WeakReference<>(rv);
		return rv;
	}
}
