// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jdwp;

/**
 * Error types for JDWP.
 *
 * @since 2021/03/12
 */
public enum ErrorType
	implements JDWPId
{
	/** Undefined error. */
	UNDEFINED_ERROR(-1),
	
	/** No error. */
	NO_ERROR(0),
	
	/** Invalid thread. */
	INVALID_THREAD(10),
	
	/** Invalid thread group. */
	INVALID_THREAD_GROUP(11),
	
	/** Invalid object. */
	INVALID_OBJECT(20),
	
	/** Invalid class. */
	INVALID_CLASS(21),
	
	/** Invalid method ID. */
	INVALID_METHOD_ID(23),
	
	/** Invalid frame ID. */
	INVALID_FRAME_ID(30),
	
	/** Not implemented. */
	NOT_IMPLEMENTED(99),
	
	/** Absent information. */
	ABSENT_INFORMATION(101),
	
	/** Invalid event type. */
	INVALID_EVENT_TYPE(102),
	
	/** End. */
	;
	
	/** Quick table. */
	private static final __QuickTable__<ErrorType> _QUICK =
		new __QuickTable__<>(ErrorType.values());
	
	/** The ID of the packet. */
	public final int id;
	
	/**
	 * Returns the ID used.
	 * 
	 * @param __id The ID used.
	 * @since 2021/03/13
	 */
	ErrorType(int __id)
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
	 * Looks up the constant by the given Id.
	 * 
	 * @param __id The Id.
	 * @return The found constant.
	 * @since 2021/03/13
	 */
	public static ErrorType of(int __id)
	{
		ErrorType rv = ErrorType._QUICK.get(__id);
		return (rv == null ? ErrorType.UNDEFINED_ERROR : rv);
	}
}
