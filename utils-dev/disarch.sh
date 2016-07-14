#!/bin/sh
# ---------------------------------------------------------------------------
# Multi-Phasic Applications: SquirrelJME
#     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
#     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the GNU General Public License v3, or later.
# For more information see license.mkd.
# ---------------------------------------------------------------------------
# DESCRIPTION: Takes some input hex code and converts it to binary code and
# then disassembles it using binutils, this is so it can be determined if
# instructions are generated properly.

# Force C locale
export LC_ALL=C

# Directory of this script
__exedir="$(dirname -- "$0")"

# Help
print_help()
{
	echo "Usage: $0 [-a arch] [hex digits...]"
	echo ""
	echo " -a Architecture to disassemble for."
}

# Default arch is current machine
__arch="$(uname -m)"

# Read input arguments
while getopts "a:" __iarg
do
	case $__iarg in
		a)
			__arch="$OPTARG"
			;;
		
		*)
			print_help
			exit 1
			;;
	esac 
done

# Down they go
shift $(($OPTIND - 1))

# Lowercase
__arch="$(echo "$__arch" | \
	sed 'y/QWERTYUIOPASDFGHJKLZXCVBNM/qwertyuiopasdfghjklzxcvbnm/')"

# Normalize architecture
	# PowerPC
if [ "$__arch" = "ppc" ] || [ "$__arch" = "powerpc32" ]
then
	__arch="powerpc" 
	
	# PowerPC 64-bit
elif [ "$__arch" = "ppc64" ]
then
	__arch="powerpc64"
	
	# i386
elif [ "$__arch" = "x86" ] || [ "$__arch" = "i486" ] || \
	[ "$__arch" = "i586" ] || [ "$__arch" = "i686" ]
then
	__arch="i386"
	
	# AMD64
elif [ "$__arch" = "x86_64" ] || [ "$__arch" = "em64t" ]
then
	__arch="amd64"
	
	# SPARC
elif [ "$__arch" = "sparc32" ] || [ "$__arch" = "sparcv8" ]
then
	__arch="sparc"
	
	# SPARC64
elif [ "$__arch" = "sparcv9" ]
then
	__arch="sparc64"
	
	# Unknown, keep as is
else
	:
fi

# Commands to run binutils with
__bin=""
__buo=""
__bub=""
__ext=""
case $__arch in
		# PowerPC
	"powerpc")
		__bin="powerpc"
		__buo="elf32-powerpc"
		__bub="powerpc:common"
		__ext=""
		;;
		
		# PowerPC 64
	"powerpc64")
		__bin="powerpc"
		__buo="elf64-powerpc"
		__bub="powerpc:common"
		__ext=""
		;;
		
		# SPARC
	"sparc")
		__bin="sparc sparc64"
		__buo="elf32-sparc"
		__bub="sparc:v8plusb"
		__ext=""
		;;
		
		# SPARC64
	"sparc64")
		__bin="sparc64 sparc"
		__buo="elf64-sparc"
		__bub="sparc:v9"
		__ext="-M v9"
		;;
		
		# SH4
	"sh4")
		__bin="sh4"
		__buo="elf32-sh"
		__bub="sh4"
		__ext=""
		;;
		
		# i386
	"i386")
		__bin="i686 i586 i486 i386 x86_64 amd64"
		__buo="elf32-i386"
		__bub="i386:intel"
		__ext=""
		;;
		
		# amd64
	"amd64")
		__bin="x86_64 amd64"
		__buo="elf64-x86-64"
		__bub="i386:x86-64"
		__ext="-M x86-64"
		;;

	*)
		echo "Unknown architecture: $__arch." 1>&2
		exit 1
		;;
esac

# Find exact binutils command to use
__binxd="objdump"
__binxc="objcopy"
for __sfx in "elf" "linux" "linux-gnu" "w64-mingw32"
do
	for __pfx in $__bin
	do
		# Fill names
		__fd="$__pfx-$__sfx-objdump"
		__fc="$__pfx-$__sfx-objcopy"
	
		# Command must exist
		if which "$__fd" > /dev/null && which "$__fc" > /dev/null
		then
			__binxd="$__fd"
			__binxc="$__fc"
			break
		fi
	done
done

# Clear temp file
rm -f /tmp/$$ /tmp/$$.elf

# Hex to decimal
__hex_to_dec()
{
	case $1 in
		A)
			echo 10
			;;
			
		B)
			echo 11
			;;
			
		C)
			echo 12
			;;
			
		D)
			echo 13
			;;
			
		E)
			echo 14
			;;
			
		F)
			echo 15
			;;
			
		*)
			echo $1
			;;
	esac
}
	
# Take all arguments and convert them into double hex sequences and run through
# them all
echo "$*" | sed 's/[^0-9a-fA-f]//g' | sed 'y/abcdef/ABCDEF/' | \
	sed 's/\(..\)/\1\n/g' | while read -r __line
do
	# If empty, ignore
	if [ -z "$__line" ] || [ "$__line" = " " ]
	then
		continue
	fi
	
	# Obtain the hex digits of each side
	__a="$(echo "$__line" | cut -c 1)"
	__b="$(echo "$__line" | cut -c 2)"
	
	# If missing character, assume zero
	if [ -z "$__b" ]
	then
		__b="0"
	fi
	
	# Convert them to decimal
	__a="$(__hex_to_dec $__a)"
	__b="$(__hex_to_dec $__b)"
	
	# Add them together as one giant number
	__c="$(expr $(expr "$__a" '*' 16) + "$__b")"
	
	# Convert to octal
	__oc="$(expr "$__c" % 8)"
	__c="$(expr "$__c" / 8)"
	__ob="$(expr "$__c" % 8)"
	__c="$(expr "$__c" / 8)"
	__oa="$(expr "$__c" % 8)"
	
	# Make binary from it
	printf '\'"$__oa$__ob$__oc" >> /tmp/$$
done

# Objdump the raw binary code
if "$__binxc" -I binary -O "$__buo" -B "$__bub" \
	--rename-section .data=.text /tmp/$$ /tmp/$$.bin
then
	"$__binxd" -D -d $__ext /tmp/$$.bin | grep -v -e 'file format' \
		-e '[dD]isassembly of section' | cat -s - | sed '/./,$!d'
fi

# Clear temp file
rm -f /tmp/$$ /tmp/$$.bin

# Done
echo

