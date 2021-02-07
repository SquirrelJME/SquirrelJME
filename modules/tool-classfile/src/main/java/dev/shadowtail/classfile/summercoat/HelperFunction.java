// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package dev.shadowtail.classfile.summercoat;

import cc.squirreljme.jvm.mle.brackets.TypeBracket;
import cc.squirreljme.jvm.summercoat.LogicHandler;
import cc.squirreljme.jvm.summercoat.brackets.QuickCastCheckBracket;
import net.multiphasicapps.classfile.ClassName;
import net.multiphasicapps.classfile.MethodDescriptor;
import net.multiphasicapps.classfile.MethodName;
import net.multiphasicapps.classfile.MethodNameAndType;

/**
 * Helper function for JVM calls, calls into {@link LogicHandler}.
 *
 * @since 2020/11/28
 */
public enum HelperFunction
{
	/** {@link LogicHandler#checkArrayStore(Object, Object)}. */
	CHECK_ARRAY_STORE("checkArrayStore",
		"(Ljava/lang/Object;Ljava/lang/Object;)Z"),
	
	/** {@link LogicHandler#typeBracketGetProperty(int, int)}. */
	TYPE_BRACKET_GET_PROPERTY("typeBracketGetProperty",
		"(II)I"),
	
	/** {@link LogicHandler#gcMemHandle(int)}.  */
	GC_MEM_HANDLE("gcMemHandle", "(I)V"),
	
	/** {@link LogicHandler#isClassInit(int)}.  */
	IS_CLASS_INIT("isClassInit", "(I)Z"),
	
	/** {@link LogicHandler#isArray(Object)}. */
	IS_ARRAY("isArray", "(Ljava/lang/Object;)Z"),
	
	/**
	  * {@link LogicHandler#isInstance(int, int,
	  * QuickCastCheckBracket)}.
	  */
	IS_INSTANCE("isInstance", "(I" +
		"I"/*"Lcc/squirreljme/jvm/mle/brackets/TypeBracket;"*/ +
		"Lcc/squirreljme/jvm/summercoat/brackets/QuickCastCheckBracket;)Z"),
	
	/** {@link LogicHandler#initClass(TypeBracket)}.  */
	INIT_CLASS("initClass", "(Lcc/squirreljme/jvm/mle/" +
		"brackets/TypeBracket;)V"),
	
	/** {@link LogicHandler#newArray(TypeBracket, int)}. */
	NEW_ARRAY("newArray", "(Lcc/squirreljme/jvm/mle/brackets" +
		"/TypeBracket;I)Ljava/lang/Object;"),
	
	/** {@link LogicHandler#newInstance(TypeBracket)}. */
	NEW_INSTANCE("newInstance", "(Lcc/squirreljme/jvm/mle/" +
		"brackets/TypeBracket;)Ljava/lang/Object;"),
	
	/** {@link LogicHandler#objectTypeBracket(int)}. */
	OBJECT_TYPE_BRACKET("objectTypeBracket", "(I)" +
		"Lcc/squirreljme/jvm/mle/brackets/TypeBracket;"),
	
	/** {@link LogicHandler#objectTypeBracket(Object)}. */
	OBJECT_TYPE_BRACKET_OBJECT("objectTypeBracket",
		"(Ljava/lang/Object;)" +
		"Lcc/squirreljme/jvm/mle/brackets/TypeBracket;"),
	
	/** {@link LogicHandler#staticVmAttribute(int)}. */ 
	STATIC_VM_ATTRIBUTE("staticVmAttribute", "(I)I"),
	
	/** End. */
	;
	
	/** The helper class to use. */
	public static final ClassName HELPER_CLASS =
		new ClassName("cc/squirreljme/jvm/summercoat/LogicHandler");
	
	/** The member to reference. */
	public final MethodNameAndType member;
	
	/**
	 * Initializes the helper information.
	 * 
	 * @param __name The name of the member.
	 * @param __type The type of the member.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/11/28
	 */
	HelperFunction(String __name, String __type)
		throws NullPointerException
	{
		if (__name == null || __type == null)
			throw new NullPointerException("NARG");
			
		this.member = new MethodNameAndType(new MethodName(__name),
			new MethodDescriptor(__type));
	}
}
