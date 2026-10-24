#!/bin/sh
# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: Creates patches to add @throw onto NanoCoat headers

# Force C locale
export LC_ALL=C

# Directory of this script
__execDir="$(dirname -- "$0")"

# Are there enough arguments
if [ "$#" -lt "1" ]
then
	echo "Usage: $0 source.c"
	exit 1
fi

# Need a specific source file
__src="$1"

# Try to make less files if things happen to fail
if [ "$PPID" != "" ] && [ "$PPID" -ne "0" ]
then
	__pid="$PPID"
else
	__pid="$$"
fi

# Makes an actual patch
__make_patch() {
	___context="$1"
	___src="$2"
	___funcFile="$3"
	___errsFile="$4"
	___forFile="$5"
	___sinceDate="$6"
	___guess="$7"
	
	# Anchor point for patches
	__anchor="$(cat "$___funcFile")"
	
	# How many lines to add?
	__lines="$(wc -l < "$___errsFile")"
	__newLines="$(expr '3' + "$__lines")"
	
	# Accumulate lines
	__was="$(cat -- "$___errsFile.line" 2> /dev/null || echo '0')"
	__now="$(expr "$__was" '+' "$__lines")"
	echo "$__now" > "$___errsFile.line"
	
	# Determine total accumulation
	__accum="$(expr "$___guess" + "$__now")"
	
	# Print the header if this is the first item
	if [ "$__context" -eq "100" ]
	then
		printf -- '--- a/%s\n' "$___forFile"
		printf -- '+++ b/%s\n' "$___forFile"
	fi
	
	# Then each individual unified diff set 
	printf -- '@@ -%d,%d +%d,%d @@\n' \
		"$___guess" "3" "$__accum" "$__newLines"
		
	# Add in each throw
	cat "$___errsFile" | while read __err
	do
		printf -- '+ * @throws %s ...?\n' "$__err"
	done
	
	# And the function context
	printf -- '  * @since %s\n' "$___sinceDate"
	printf -- '  */\n'
	printf -- ' %s\n' "$__anchor"
	
	# For reference, the working patch...
	#--- a/include/sjme/nvm/classyVm.h
	#+++ b/include/sjme/nvm/classyVm.h
	#@@ -100,2 +100,5 @@
	#+ * @throws SJME_ERROR_NULL_ARGUMENTS
	#+ * @throws SJME_ERROR_INVALID_ARGUMENT
	#+ * @throws SJME_ERROR_INVALID_FIELD_TYPE
	#  */
	# sjme_errorCode sjme_nvm_vmClass_loaderLoadPrimitive(
}

# Forget function we are in
__funcFile="/tmp/$__pid.func" 
__errsFile="/tmp/$__pid.errs" 
rm -f -- "$__funcFile" "$__errsFile" "$__errsFile.line"

# The base context to use
__context="100"

# Read in base lines
grep -o -P \
	'(sjme_errorCode sjme_[a-zA-Z0-9_]+\(|SJME_ERROR_[A-Z_0-9]+)' < \
	"$__src" | while read __base
do
	# Is this an error or something else?
	if echo "$__base" | grep -e 'SJME_ERROR_' > /dev/null
	then
		# Put this error into the error file
		echo "$__base" >> "$__errsFile"
		
		# Remove duplicates and NONE, and also sort them
		sort -u < "$__errsFile" | grep -v "SJME_ERROR_NONE" > "$__errsFile.1"
		mv -- "$__errsFile.1" "$__errsFile"
	else
		# Make a patch, if both files exist
		if [ -f "$__funcFile" ] && [ -f "$__errsFile" ]
		then
			# Which function?
			__forFunc="$(cat "$__funcFile" | \
				sed 's/sjme_errorCode //g' | \
				sed 's/[^a-zA-Z_]//g')"
			
			# Find the header this is in
			__forFile="$(grep -rl -- "$__forFunc" | grep '\.h$' | tail -n 1)"
			
			# Find any surrounding @since to anchor onto
			__sinceDate=""
			if [ -n "$__forFile" ]
			then
				# * @since 2025/10/04
				# */
				#sjme_errorCode sjme_nvm_vmClass_loaderLoadU(
				__sinceDate="$(grep -B 2 -- "^sjme_errorCode $__forFunc($" < \
					"$__forFile" | grep '@since' |
					grep -o '20..\/..\/..')"
			fi 
			
			# Make the patch if the file was found, and the date is valid as
			# well
			if [ -n "$__forFile" ] && [ -n "$__sinceDate" ]
			then
				# Guess the line
				__guess="$(grep -hn "^sjme_errorCode $__forFunc($" < \
					"$__forFile" 2> /dev/null | head -n 1 | cut -d ':' -f 1)"
				
				# Generate patch
				__make_patch "$__context" "$__src" \
					"$__funcFile" "$__errsFile" "$__forFile" \
					"$__sinceDate" "$__guess"
		
				# Bump up context
				__context="$(expr "$__context" + 100)"
			fi
		fi
		
		# Put this into the function file
		echo "$__base" > "$__funcFile"
		
		# Clear the old error file
		rm -f -- "$__errsFile"
	fi
done

# Cleanup
rm -f -- "$__funcFile" "$__errsFile" "$__errsFile.line"
