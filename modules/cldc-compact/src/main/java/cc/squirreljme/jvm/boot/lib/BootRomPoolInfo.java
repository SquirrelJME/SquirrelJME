// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.boot.lib;

/**
 * This class contains the information on the boot pool addresses.
 *
 * @since 2019/11/17
 */
public final class BootRomPoolInfo
{
	/** This is the static constant pool offset. */
	protected final int staticpooladdress;
	
	/** This is the static constant pool size. */
	protected final int staticpoolsize;
	
	/** This is the run-time constant pool address. */
	protected final int runtimepooladdress;
	
	/** This is the run-time constant pool size. */
	protected final int runtimepoolsize;
	
	/**
	 * Initializes the boot ROM pool information.
	 *
	 * @param __so Static pool address.
	 * @param __ss Static pool size.
	 * @param __ro Run-time pool address.
	 * @param __rs Run-time pool size.
	 * @since 2019/11/18
	 */
	public BootRomPoolInfo(int __so, int __ss, int __ro, int __rs)
	{
		this.staticpooladdress = __so;
		this.staticpoolsize = __ss;
		this.runtimepooladdress = __ro;
		this.runtimepoolsize = __rs;
	}
	
	/**
	 * Returns the address of the pool.
	 *
	 * @param __rt The run-time pool.
	 * @return The address.
	 * @since 2019/11/18
	 */
	public final int address(boolean __rt)
	{
		return (__rt ? this.runtimepooladdress : this.staticpooladdress);
	}
	
	/**
	 * Is this pool valid?
	 *
	 * @param __rt The run-time pool.
	 * @return If it is valid.
	 * @since 2019/11/18
	 */
	public final boolean isDefined(boolean __rt)
	{
		// The address is not considered because it may be negative
		return (__rt ? this.runtimepoolsize : this.staticpoolsize) > 0;
	}
	
	/**
	 * Returns the size of the pool.
	 *
	 * @param __rt The run-time pool.
	 * @return The size.
	 * @since 2019/11/18
	 */
	public final int size(boolean __rt)
	{
		return (__rt ? this.runtimepoolsize : this.staticpoolsize);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/11/24
	 */
	@Override
	public final String toString()
	{
		return "Pools{st=(@" + Integer.toString(this.staticpooladdress, 16) +
			" len=" + this.staticpoolsize + "), rt=(@" +
			Integer.toString(this.runtimepooladdress, 16) +
			" len=" + this.runtimepoolsize + ")}";
	}
}

