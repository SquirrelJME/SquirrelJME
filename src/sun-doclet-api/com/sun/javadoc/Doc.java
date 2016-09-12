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

public interface Doc
	extends Comparable<Object>
{
	public abstract String commentText();
	
	public abstract int compareTo(Object __a);
	
	public abstract Tag[] firstSentenceTags();
	
	public abstract String getRawCommentText();
	
	public abstract Tag[] inlineTags();
	
	public abstract boolean isAnnotationType();
	
	public abstract boolean isAnnotationTypeElement();
	
	public abstract boolean isClass();
	
	public abstract boolean isConstructor();
	
	public abstract boolean isEnum();
	
	public abstract boolean isEnumConstant();
	
	public abstract boolean isError();
	
	public abstract boolean isException();
	
	public abstract boolean isField();
	
	public abstract boolean isIncluded();
	
	public abstract boolean isInterface();
	
	public abstract boolean isMethod();
	
	public abstract boolean isOrdinaryClass();
	
	public abstract String name();
	
	public abstract SourcePosition position();
	
	public abstract SeeTag[] seeTags();
	
	public abstract void setRawCommentText(String __a);
	
	public abstract Tag[] tags();
	
	public abstract Tag[] tags(String __a);
}


