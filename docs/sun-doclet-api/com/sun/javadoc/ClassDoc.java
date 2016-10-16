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

public interface ClassDoc
	extends ProgramElementDoc, Type
{
	public abstract ConstructorDoc[] constructors();
	
	public abstract ConstructorDoc[] constructors(boolean __a);
	
	public abstract boolean definesSerializableFields();
	
	public abstract FieldDoc[] enumConstants();
	
	public abstract FieldDoc[] fields();
	
	public abstract FieldDoc[] fields(boolean __a);
	
	public abstract ClassDoc findClass(String __a);
	
	@Deprecated
	public abstract ClassDoc[] importedClasses();
	
	@Deprecated
	public abstract PackageDoc[] importedPackages();
	
	public abstract ClassDoc[] innerClasses();
	
	public abstract ClassDoc[] innerClasses(boolean __a);
	
	public abstract Type[] interfaceTypes();
	
	public abstract ClassDoc[] interfaces();
	
	public abstract boolean isAbstract();
	
	public abstract boolean isExternalizable();
	
	public abstract boolean isSerializable();
	
	public abstract MethodDoc[] methods();
	
	public abstract MethodDoc[] methods(boolean __a);
	
	public abstract FieldDoc[] serializableFields();
	
	public abstract MethodDoc[] serializationMethods();
	
	public abstract boolean subclassOf(ClassDoc __a);
	
	public abstract ClassDoc superclass();
	
	public abstract Type superclassType();
	
	public abstract ParamTag[] typeParamTags();
	
	public abstract TypeVariable[] typeParameters();
}


