// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package javax.microedition.swm;

import java.util.Iterator;
import java.util.Objects;
import java.util.HashSet;
import java.util.Set;
import net.multiphasicapps.util.empty.EmptyIterator;

/**
 * This represents an application suite.
 *
 * Created suites by default have their {@link SuiteStateFlag#AVAILABLE} and
 * {@link SuiteStateFlag#ENABLED} flags set.
 *
 * @since 2016/06/24
 */
public class Suite
{
	/** This is a suite that represents the system. */
	public static Suite SYSTEM_SUITE =
		new __SystemSuite__();
	
	/** The state lock. */
	private volatile Object _statelock =
		new Object();
	
	/** The flags associated with this suite. */
	private volatile int _state;
	
	/**
	 * The constructor of this class is assumed to be internal use.
	 *
	 * @since 2016/06/24
	 */
	private Suite()
	{
		throw new Error("TODO");
	}
	
	/**
	 * Checks if two suites are equal to each other, they are equal when
	 * the vendor and name of the suite match.
	 *
	 * @param __o The object to compare against.
	 * @return {@code true} if this suite is equal to the other object.
	 * @since 2016/06/24
	 */
	@Override
	public boolean equals(Object __o)
	{
		// Not another suite?
		if (!(__o instanceof Suite))
			return false;
		
		// Check
		Suite o = (Suite)__o;
		return Objects.equals(getName(), o.getName()) &&
			Objects.equals(getVendor(), o.getVendor());
	}
	
	/**
	 * Returns the list of attributes which are defined in the JAD or the
	 * manifest.
	 *
	 * @return The iterator of attributes. The system suite always returns
	 * an empty iteration.
	 * @since 2016/06/24
	 */
	public Iterator<String> getAttributes()
	{
		throw new Error("TODO");
	}
	
	/**
	 * Returns the value of an attribute.
	 *
	 * @param __a The name of the attribute to obtain a value for.
	 * @return The value of the given attribute or {@code null} if it was not
	 * found. The system suite always returns null.
	 * @since 2016/06/24
	 */
	public String getAttributeValue(String __a)
	{
		throw new Error("TODO");
	}
	
	/**
	 * Returns the library suites which this suite depends on.
	 *
	 * @return The iterator over the suite dependencies. The system suite
	 * always returns an empty iterator.
	 * @since 2016/06/24
	 */
	public Iterator<Suite> getDependencies()
	{
		throw new Error("TODO");
	}
	
	/**
	 * This returns the URL which a previously installed suite was downloaded
	 * from.
	 *
	 * @return The URL where the suite was downloaded from. If this is the
	 * system suite, the suite was pre-installed, or was installed using the
	 * raw byte array then this will return null.
	 * @since 2016/06/24
	 */
	public String getDownloadUrl()
	{
		throw new Error("TODO");
	}
	
	/**
	 * This returns the names of all classes which are specfied in the MIDlet
	 * attributes in the manifest. The sequence of classes should match the
	 * MIDlet order number.
	 *
	 * @return The names of classes that are specified in the MIDlet attributes
	 * in the manifest. The system suite always returns an empty iterator.
	 * @since 2016/06/24
	 */
	public Iterator<String> getMIDlets()
	{
		throw new Error("TODO");
	}
	
	/**
	 * Returns the name of this suite.
	 *
	 * @return The suite name. The system suite always returns null.
	 * @since 2016/06/24
	 */
	public String getName()
	{
		throw new Error("TODO");
	}
	
	/**
	 * Returns the type of suite that this is.
	 *
	 * @return The type of suite this is. The system suite always returns
	 * {@link SuiteType#SYSTEM}.
	 * @since 2016/06/24
	 */
	public SuiteType getSuiteType()
	{
		return SuiteType.INVALID;
	}
	
	/**
	 * Returns the vendor of this suite.
	 *
	 * @return The vendor of this suite. The system suite always returns null.
	 * @since 2016/06/24
	 */
	public String getVendor()
	{
		throw new Error("TODO");
	}
	
	/**
	 * Returns the version of this suite.
	 *
	 * @return The version of this suite. The system suite always returns null.
	 * @since 2016/06/24
	 */
	public String getVersion()
	{
		throw new Error("TODO");
	}
	
	/**
	 * Calculates the hash code of the given suite, the values are derived from
	 * the name and the vendor.
	 *
	 * @return The hash code.
	 * @since 2016/06/24
	 */
	@Override
	public int hashCode()
	{
		return Objects.hashCode(getVendor()) ^
			Objects.hashCode(getName());
	}
	
	/**
	 * Returns {@code true} if this suite is installed.
	 *
	 * @return {@code true} if this suite is installed. The system suite always
	 * returns {@code true}.
	 * @since 2016/06/24
	 */
	public boolean isInstalled()
	{
		throw new Error("TODO");
	}
	
	/**
	 * Checks if the suite has the specified flag set.
	 *
	 * @param __f The flag to check.
	 * @return {@code true} if the flag is set.
	 * @since 2016/06/24
	 */
	public boolean isSuiteState(SuiteStateFlag __f)
	{
		// Null is never true
		if (__f == null)
			return false;
		
		// Lock
		synchronized (this._statelock)
		{
			return 0 != (this._state & (1 << __f.ordinal()));
		}
	}
	
	/**
	 * Sets whether this suite is trusted or not.
	 *
	 * @return {@code true} if this suite is trusted. The system suite always
	 * returns {@code true}.
	 * @since 2016/06/24
	 */
	public boolean isTrusted()
	{
		throw new Error("TODO");
	}
	
	/**
	 * Sets the given flag to the suite.
	 *
	 * @param __f The flag to set.
	 * @param __v If the flag should be set or cleared.
	 * @throws IllegalArgumentException If an attempt was made to set the
	 * {@link SuiteStateFlag#SYSTEM} or {@link SuiteStateFlag#PREINSTALLED}
	 * flags.
	 * @throws IllegalStateException If the suite was removed or this is the
	 * system suite.
	 * @throws SecurityException If the {@code {@link SWMPermission}("client",
	 * "manageSuite")} or {@code {@link SWMPermission}("crossClient",
	 * "manageSuite")} permission is not permitted.
	 * @since 2016/06/24
	 */
	public void setSuiteStateFlag(SuiteStateFlag __f, boolean __v)
		throws IllegalArgumentException, IllegalStateException,
			SecurityException
	{
		// Ignore
		if (__f == null)
			return;
		
		// {@squirreljme.error DG02 The current suite has been removed.}
		if (!isInstalled())
			throw new IllegalStateException("DG02");
		
		// {@squirreljme.error DG01 The given state flag cannot be set.
		// (The state flag)}
		if (__f == SuiteStateFlag.SYSTEM || __f == SuiteStateFlag.PREINSTALLED)
			throw new IllegalArgumentException(String.format("DG01 %s", __f));
		
		// Lock
		synchronized (this._statelock)
		{
			// {@squirreljme.error DG03 Cannot change flags of the system
			// suite.}
			if (0 != (this._state & (1 << SuiteStateFlag.SYSTEM.ordinal())))
				throw new IllegalStateException("DG03");
			
			// Get the required bit
			int bit = (1 << __f.SYSTEM.ordinal());
			
			// Set or clear?
			if (__v)
				this._state |= bit;
			else
				this._state &= bit;
		}
	}
	
	/**
	 * The system suite.
	 *
	 * @since 2016/06/24
	 */
	private static final class __SystemSuite__
		extends Suite
	{
		/**
		 * Initializes the base suite.
		 *
		 * @since 2016/06/24
		 */
		private __SystemSuite__()
		{
			// The system always has these states
			super._state = 
				(1 << SuiteStateFlag.SYSTEM.ordinal()) |
				(1 << SuiteStateFlag.PREINSTALLED.ordinal()) |
				(1 << SuiteStateFlag.REMOVE_DENIED.ordinal()) |
				(1 << SuiteStateFlag.ENABLED.ordinal()) |
				(1 << SuiteStateFlag.AVAILABLE.ordinal());
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2016/06/24
		 */
		@Override
		public SuiteType getSuiteType()
		{
			return SuiteType.SYSTEM;
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2016/06/24
		 */
		@Override
		public boolean isInstalled()
		{
			return true;
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2016/06/24
		 */
		@Override
		public boolean isTrusted()
		{
			return true;
		}
	}
}

