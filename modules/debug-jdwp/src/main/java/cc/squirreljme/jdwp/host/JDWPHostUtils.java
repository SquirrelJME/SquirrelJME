// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jdwp.host;

import cc.squirreljme.jdwp.JDWPClassType;
import cc.squirreljme.jdwp.host.views.JDWPViewObject;
import cc.squirreljme.jdwp.host.views.JDWPViewThread;
import cc.squirreljme.jdwp.host.views.JDWPViewType;

/**
 * General utilities for JDWP Support.
 *
 * @since 2021/04/11
 */
public final class JDWPHostUtils
{
	/** Interface bit. */
	private static final int _INTERFACE_BIT = 
		0x0200;
	
	/**
	 * Not used.
	 * 
	 * @since 2021/04/11
	 */
	private JDWPHostUtils()
	{
	}
	
	/**
	 * Returns the type that the given class is.
	 * 
	 * @param __controller The controller.
	 * @param __class The class to check.
	 * @return The type that the class is.
	 * @throws NullPointerException On null arguments.
	 * @since 2021/04/11
	 */
	public static JDWPClassType classType(JDWPHostController __controller,
		Object __class)
		throws NullPointerException
	{
		if (__controller == null)
			throw new NullPointerException("NARG");
		
		// If null or not valid, treat as an object
		JDWPViewType viewType = __controller.viewType();
		if (__class == null || !viewType.isValid(__class))
		{
			// If this was an object, then get the class of the object
			JDWPViewObject viewObject = __controller.viewObject();
			if (__class != null && viewObject.isValid(__class))
				return JDWPHostUtils.classType(__controller,
					viewObject.type(__class));
			
			return JDWPClassType.CLASS;
		}
		
		// Array type?
		if (viewType.signature(__class).startsWith("["))
			return JDWPClassType.ARRAY;
		
		// Is this potentially an interface?
		int flags = viewType.flags(__class);
		if ((flags & JDWPHostUtils._INTERFACE_BIT) != 0)
			return JDWPClassType.INTERFACE;
		
		// Just a plain class
		return JDWPClassType.CLASS;
	}
	
	/**
	 * Finds the field ID for the given type.
	 * 
	 * @param __viewType The type view.
	 * @param __type The type to look in.
	 * @param __fieldName The field name.
	 * @param __fieldDesc The field descriptor.
	 * @return The field ID for the given field or a negative if not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2022/09/24
	 */
	public static int findFieldId(JDWPViewType __viewType, Object __type,
		String __fieldName, String __fieldDesc)
		throws NullPointerException
	{
		if (__viewType == null || __type == null || __fieldName == null ||
			__fieldDesc == null)
			throw new NullPointerException("NARG");
		
		// Search through all fields
		for (int fieldId : __viewType.fields(__type))
			if (__fieldName.equals(__viewType.fieldName(__type, fieldId)) &&
				__fieldDesc.equals(__viewType.fieldSignature(__type, fieldId)))
				return fieldId;
		
		// Not found
		return -1;
	}
	
	/**
	 * Is this a visible thread?
	 * 
	 * @param __view The view used.
	 * @param __thread The thread.
	 * @return If this thread is visible or not.
	 * @since 2022/09/24
	 */
	public static boolean isVisibleThread(JDWPViewThread __view,
		Object __thread)
	{
		return !__view.isTerminated(__thread) &&
			!__view.isDebugCallback(__thread) &&
			__view.frames(__thread).length > 0;
	}
	
	/**
	 * Converts a signature to a runtime name.
	 * 
	 * @param __signature The signature to convert.
	 * @return The runtime name of the signature.
	 * @throws NullPointerException On null arguments.
	 * @since 2021/04/18
	 */
	public static String signatureToRuntime(String __signature)
		throws NullPointerException
	{
		if (__signature == null)
			throw new NullPointerException("NARG");
		
		// If not a class, keep as is!
		if (!__signature.startsWith("L"))
			return __signature;
		
		// Clip off the L; and un-binary name it
		return __signature.substring(1, __signature.length() - 1)
			.replace('/', '.');
	}
}
