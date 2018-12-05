// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.cldc.lang;

/**
 * Version 1 class data.
 *
 * @since 2018/12/04
 */
public class ClassDataV1
	extends ClassData
{
	/** The super class of this class. */
	private final Class<?> _superclass;
	
	/** The interface classes of this class. */
	private final Class<?>[] _interfaceclasses;
	
	/** The component type. */
	private final Class<?> _component;
	
	/** The binary name of this class. */
	private final String _binaryname;
	
	/** Special class reference index. */
	private final int _specialindex;
	
	/** The number of dimensions. */
	private final int _dimensions;
	
	/** The JAR this class is in. */
	private final String _injar;
	
	/** Class flags. */
	private final int _flags;
	
	/**
	 * Version 1 constructor.
	 *
	 * @param __csi Class special index.
	 * @param __bn The binary name of this class.
	 * @param __sc Super classes.
	 * @param __ic Interface classes.
	 * @param __ij The JAR this class is in.
	 * @param __flags Class flags.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/04/12
	 */
	public ClassDataV1(int __csi, String __bn, Class<?> __sc, Class<?>[] __ic,
		Class<?> __ct, String __ij, int __flags)
		throws NullPointerException
	{
		super(1);
		
		if (__bn == null)
			throw new NullPointerException("NARG");
		
		this._specialindex = __csi;
		this._binaryname = __bn;
		this._superclass = __sc;
		this._interfaceclasses = __ic;
		this._component = __ct;
		this._injar = __ij;
		this._flags = __flags;
		
		// Count dimensions, used for comparison purposes
		int dims = 0;
		for (; __bn.charAt(dims) == '['; dims++)
			;
		this._dimensions = dims;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/04
	 */
	@Override
	public String binaryName()
	{
		return this._binaryname;
	}
	
	/**
	 * Returns the component of the array.
	 *
	 * @return The array component.
	 * @since 2018/12/04
	 */
	public Class<?> component()
	{
		return this._component;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/04
	 */
	@Override
	public int dimensions()
	{
		return this._dimensions;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/04
	 */
	@Override
	public int flags()
	{
		return this._flags;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/04
	 */
	@Override
	public String inJar()
	{
		return this._injar;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/04
	 */
	@Override
	public Class<?>[] interfaceClasses()
	{
		return this._interfaceclasses;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/04
	 */
	@Override
	public Class<?> superClass()
	{
		return this._superclass;
	}
}

