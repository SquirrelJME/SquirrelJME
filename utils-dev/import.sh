#!/bin/sh
# ---------------------------------------------------------------------------
# Multi-Phasic Applications: SquirrelJME
#     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
#     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the GNU Affero General Public License v3, or later.
# For more information see license.txt.
# ---------------------------------------------------------------------------
# DESCRIPTION: Imports source code from my other project and formats it.

# Force C locale
export LC_ALL=C

# Directory of this script
__exedir="$(dirname -- "$0")"

# For each input file
for __file in $*
do
	# Name of this file/class
	__name="$(basename "$__file" .java)"
	
	# Sed work
	sed '/@compliance\.Standard.*/d' < "$__file" |
	
		# Header fixup
		sed 's/Kernel 8 (k8)/SquirrelJME/g' |
		sed 's/k8 is under/SquirrelJME is under/g' |
		sed 's/License v3 (or later)/License v3+/g' |
		sed 's/check COPYING./see license.txt./g' |
		sed 's/Non-program works/Non-programs/g' |
		sed 's/2013-201[0-9]/2013-2016/g' |
		
		# Remove throwing of falsed exceptions and exceptions themselves
		sed '/if[ \t]*([ \t]*false[ \t]*)/d' |
		sed '/throw[ \t]\{1,\}new/d' |
		sed '/import java.io.Serializable;/d' |
		
		# Remove done marks
		sed '/@compliance.Done/d' |
		
		# Translate super to an exception throw
		sed 's/super(.*);/throw new Error();/g' |
		
		# Serialized
		sed '/implements[ \t]*zSerializable[ \t]*z$/d' |
		sed 's/Serializable[ \t]*,[ \t]*//g' |
		sed '/implements Serializable[ \t]*$/d' |
		
		# Remove synchronized this
		sed '/synchronized[ \t]*([ \t]*.*[ \t]*)[ \t]*/d' |
		
		# Translate to alternative for easier processing
		tr '\n' '\v' |
		
		# Remove the optional usage stuff and just use null instead
		sed 's/java\.util\.Optional\.<.*;/null;/g' |
		
		# Remove inner blank synchronized blocks
		sed 's/{[ \t]*\v[ \t]*{[ \t]*\v[ \t]*}[ \t]*\v[ \t]*}/\{\v}/g' |
		
		# Remove empty bodies
		sed 's/)[ \t]*\v[ \t]*{[ \t]*\v[ \t]*}[ \t]*/);/g' |
		
		# And empty bodies after exceptions
		sed 's/\(throws[^\v]*\)\v[ \t]*{[ \t]*\v[ \t]*}[ \t]*/\1;/g' |
		
		# Add native after stuff
		sed 's/public\([ \t]\{1,\}\)/public native\1/g' |
		sed 's/protected\([ \t]\{1,\}\)/protected native\1/g' |
		sed 's/private\([ \t]\{1,\}\)/private native\1/g' |
		
		# Remove native if abstract is on the same line
		sed 's/native[ \t]\{1,\}\(.*[ \t]\{1,\}abstract\)/\1/g' |
		sed 's/\(abstract[ \t]\{1,\}.*\)[ \t]\{1,\}native/\1/g' |
		
		# Remove native abstract
		sed 's/native[ \t]\{1,\}abstract/abstract/g' |
		
		# And remove native if it shares a line with class
		sed 's/native[ \t]\{1,\}\(.*[ \t]\{1,\}class\)/\1/g' |
		sed 's/\(class[ \t]\{1,\}.*\)[ \t]\{1,\}native/\1/g' |
		
		# And interface too
		sed 's/native[ \t]\{1,\}\(.*[ \t]\{1,\}interface\)/\1/g' |
		sed 's/\(interface[ \t]\{1,\}.*\)[ \t]\{1,\}native/\1/g' |
		
		# And annotations
		sed 's/native[ \t]\{1,\}\(.*[ \t]\{1,\}@interface\)/\1/g' |
		sed 's/\(@interface[ \t]\{1,\}.*\)[ \t]\{1,\}native/\1/g' |
		
		# Translate vertical tabs back
		tr '\v' '\n' |
		
		# Remove lines containing default
		#sed '/[ \t]\{1,\}default[ \t]\{1,\}/d' |
		
		# Remove lines containing streams
		sed '/Stream</d' |
		sed '/IntStream/d' |
		sed '/LongStream/d' |
		sed '/FloatStream/d' |
		sed '/DoubleStream/d' |
		sed '/java\.util\.stream./d' |
		
		# Go back
		tr '\n' '\v' |
		
		# These might appear
		sed 's/public native class/public class/g' |
		sed 's/public native interface/public interface/g' |
		sed 's/public native @interface/public @interface/g' |
		sed 's/public native abstract class/public abstract class/g' |
		sed 's/public native final class/public final class/g' |
		
		# Remove duplicate C export lines
		sed 's/@__squirrnix.CExport[ \t\v]\{1,\}@__squirrnix.CExport//g' |
		
		# Remove blank lines following open brace
		sed 's/{[ \t]*\v[ \t]*\v/{/g' |
		
		# And revert
		tr '\v' '\n' |
		
		# Remove native from constructors
		sed "s/native $__name(/$__name(/g" |
		
		# Unique to remove blank lines next to blank lines
		uniq
done

