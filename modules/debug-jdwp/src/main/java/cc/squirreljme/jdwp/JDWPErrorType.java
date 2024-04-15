// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jdwp;

/**
 * Error types for JDWP.
 *
 * @since 2021/03/12
 */
public enum JDWPErrorType
	implements JDWPHasId
{
	/** Undefined error. */
	UNDEFINED_ERROR(-1),
	
	/** No error. */
	NO_ERROR(0),
	
	/** Invalid thread. */
	INVALID_THREAD(10),
	
	/** Invalid thread group. */
	INVALID_THREAD_GROUP(11),
	
	/** Thread is not suspended. */
	THREAD_NOT_SUSPENDED(13),
	
	/** Invalid object. */
	INVALID_OBJECT(20),
	
	/** Invalid class. */
	INVALID_CLASS(21),
	
	/** Invalid method ID. */
	INVALID_METHOD_ID(23),
	
	/** Invalid location. */
	INVALID_LOCATION(24),
	
	/** Invalid field ID. */
	INVALID_FIELD_ID(25),
	
	/** Invalid frame ID. */
	INVALID_FRAME_ID(30),
	
	/** Frame does not have any information. */
	OPAQUE_FRAME(32),
	
	/** Not the current frame. */
	NOT_CURRENT_FRAME(33),
	
	/** The type for the requested slot is not valid. */
	TYPE_MISMATCH(34),
	
	/** Invalid variable slot. */
	INVALID_SLOT(35),
	
	/** Not implemented. */
	NOT_IMPLEMENTED(99),
	
	/** Absent information. */
	ABSENT_INFORMATION(101),
	
	/** Invalid event type. */
	INVALID_EVENT_TYPE(102),
	
	/** Illegal argument. */
	ILLEGAL_ARGUMENT(103),
	
	/** Virtual machine not running. */
	VM_DEAD(112),
	
	/** Internal error. */
	INTERNAL(113),
	
	/** Thread is not attached. */
	UNATTACHED_THREAD(115),
	
	/** Invalid length. */
	INVALID_LENGTH(504),
	
	/** Invalid string. */
	INVALID_STRING(506),
	
	/** Invalid array. */
	INVALID_ARRAY(508),
	
	/** End. */
	;
	
	/** Quick table. */
	private static final JDWPIdMap<JDWPErrorType> _QUICK =
		new JDWPIdMap<>(JDWPErrorType.values());
	
	/** The ID of the packet. */
	public final int id;
	
	/**
	 * Returns the ID used.
	 * 
	 * @param __id The ID used.
	 * @since 2021/03/13
	 */
	JDWPErrorType(int __id)
	{
		this.id = __id;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/03/13
	 */
	@Override
	public final int debuggerId()
	{
		return this.id;
	}
	
	/**
	 * Throws an exception on any kind of failure.
	 * 
	 * @param __context The context, may be anything.
	 * @param __id The ID number.
	 * @return This is tossed on any command errors.
	 * @since 2021/04/11
	 */
	public final JDWPCommandException toss(Object __context, int __id)
	{
		return this.toss(__context, __id, null);
	}
	
	/**
	 * Throws an exception on any kind of failure.
	 * 
	 * @param __context The context, may be anything.
	 * @param __id The ID number.
	 * @param __cause The cause.
	 * @return This is tossed on any command errors.
	 * @since 2021/04/15
	 */
	public final JDWPCommandException toss(Object __context, int __id,
		Throwable __cause)
	{
		/* {@squirreljme.error AG0o Command cause an exception.
		(The error; The context; The context type; The ID number)} */
		throw new JDWPCommandException(this,
			String.format("AG0o %s %s (%s) %d", this, __context,
				(__context == null ? "null" : __context.getClass()),
				__id), __cause);
	}
	
	/**
	 * Looks up the constant by the given Id.
	 * 
	 * @param __id The Id.
	 * @return The found constant.
	 * @since 2021/03/13
	 */
	public static JDWPErrorType of(int __id)
	{
		JDWPErrorType rv = JDWPErrorType._QUICK.get(__id);
		return (rv == null ? JDWPErrorType.UNDEFINED_ERROR : rv);
	}
}
