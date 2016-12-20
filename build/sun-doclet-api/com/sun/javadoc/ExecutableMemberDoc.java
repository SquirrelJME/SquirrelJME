// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package com.sun.javadoc;

public interface ExecutableMemberDoc
	extends MemberDoc
{
	public abstract String flatSignature();
	
	public abstract boolean isNative();
	
	public abstract boolean isSynchronized();
	
	public abstract boolean isVarArgs();
	
	public abstract ParamTag[] paramTags();
	
	public abstract Parameter[] parameters();
	
	public abstract Type receiverType();
	
	public abstract String signature();
	
	public abstract Type[] thrownExceptionTypes();
	
	public abstract ClassDoc[] thrownExceptions();
	
	public abstract ThrowsTag[] throwsTags();
	
	public abstract ParamTag[] typeParamTags();
	
	public abstract TypeVariable[] typeParameters();
}


