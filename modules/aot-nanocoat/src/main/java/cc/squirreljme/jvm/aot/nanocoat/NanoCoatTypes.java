// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.aot.nanocoat;

import cc.squirreljme.c.CType;
import cc.squirreljme.runtime.cldc.debug.Debugging;

/**
 * Types that are defined by NanoCoat.
 *
 * @since 2023/06/06
 */
public enum NanoCoatTypes
{
	/** Long. */
	@Deprecated
	JLONG
	{
		/**
		 * {@inheritDoc}
		 * @since 2023/06/06
		 */
		@Override
		CType __build()
		{
			throw Debugging.todo();
		}
	},
	
	/** Float. */
	@Deprecated
	JFLOAT
	{
		/**
		 * {@inheritDoc}
		 * @since 2023/06/06
		 */
		@Override
		CType __build()
		{
			throw Debugging.todo();
		}
	},
	
	/** Double. */
	@Deprecated
	JDOUBLE
	{
		/**
		 * {@inheritDoc}
		 * @since 2023/06/06
		 */
		@Override
		CType __build()
		{
			throw Debugging.todo();
		}
	},
	
	/** Class. */
	@Deprecated
	JCLASS
	{
		/**
		 * {@inheritDoc}
		 * @since 2023/06/06
		 */
		@Override
		CType __build()
		{
			throw Debugging.todo();
		}
	},
	
	/** Object. */
	@Deprecated
	JOBJECT
	{
		/**
		 * {@inheritDoc}
		 * @since 2023/06/06
		 */
		@Override
		CType __build()
		{
			throw Debugging.todo();
		}
	},
	
	/** String. */
	@Deprecated
	JSTRING
	{
		/**
		 * {@inheritDoc}
		 * @since 2023/06/06
		 */
		@Override
		CType __build()
		{
			throw Debugging.todo();
		}
	},
	
	/** Field. */
	@Deprecated
	JFIELD
	{
		/**
		 * {@inheritDoc}
		 * @since 2023/06/06
		 */
		@Override
		CType __build()
		{
			throw Debugging.todo();
		}
	},
	
	/** Method. */
	@Deprecated
	JMETHOD
	{
		/**
		 * {@inheritDoc}
		 * @since 2023/06/06
		 */
		@Override
		CType __build()
		{
			throw Debugging.todo();
		}
	},
	
	/** The NanoCoat state. */
	@Deprecated
	VMSTATE
	{
		/**
		 * {@inheritDoc}
		 * @since 2023/06/06
		 */
		@Override
		CType __build()
		{
			throw Debugging.todo();
		}
	},
	
	/** A NanoCoat thread. */
	@Deprecated
	VMTHREAD
	{
		/**
		 * {@inheritDoc}
		 * @since 2023/06/06
		 */
		@Override
		CType __build()
		{
			throw Debugging.todo();
		}
	},
	
	/** A NanoCoat resource. */
	@Deprecated
	RESOURCE
	{
		/**
		 * {@inheritDoc}
		 * @since 2023/06/06
		 */
		@Override
		CType __build()
		{
			throw Debugging.todo();
		}
	},
	
	/** Field information. */
	@Deprecated
	CLASS_FIELDS
	{
		/**
		 * {@inheritDoc}
		 * @since 2023/06/06
		 */
		@Override
		CType __build()
		{
			throw Debugging.todo();
		}
	},
	
	/** Method information. */
	@Deprecated
	CLASS_METHODS
	{
		/**
		 * {@inheritDoc}
		 * @since 2023/06/06
		 */
		@Override
		CType __build()
		{
			throw Debugging.todo();
		}
	},
	
	/** Stack frame. */
	@Deprecated
	VMFRAME
	{
		/**
		 * {@inheritDoc}
		 * @since 2023/06/06
		 */
		@Override
		CType __build()
		{
			throw Debugging.todo();
		}
	},
	
	/* End. */
	;
	
	/**
	 * Builds the type, this is done at run-time since this is an enum and
	 * some types may refer to each other accordingly using either consts
	 * or pointers.
	 * 
	 * @return The built type.
	 * @since 2023/06/06
	 */
	abstract CType __build();
	
	/**
	 * Returns the type used.
	 * 
	 * @return The type used.
	 * @since 2023/06/06
	 */
	public final CType type()
	{
		throw Debugging.todo();
	}
}
