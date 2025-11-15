# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: System and architecture mappings

# Virtual Machines which are available
list(APPEND SQUIRRELJME_JVM_MAP
	"Hosted!hosted"
	"SpringCoat!springcoat"
	"NanoCoat!nanocoat")

# Clutter levels which are available
list(APPEND SQUIRRELJME_CLUTTER_MAP
	"Release!release"
	"Debug!debug")

# Architecture mappings (SquirrelJME!gcc)
list(APPEND SQUIRRELJME_ARCH_MAP
	"ia32!i386"
	"ia32!i486"
	"ia32!i586"
	"ia32!i686"
	"ia32!x86"
	"ia32!x86_32"
	"ia64!ia64"
	"ia64!itanium"
	"ia64!itanic"
	"amd64!amd64"
	"amd64!x86_64"
	"amd64!em64t"
	"mips32b!mips"
	"mips32l!mipsel"
	"mips32b!mips64"
	"mips32l!mips64el"
	"riscv64!riscv64"
	"arm64!aarch64"
	"powerpc32b!powerpc"
	"powerpc32l!powerpcle"
	"powerpc64b!powerpc64"
	"powerpc64l!powerpc64le")

# System mappings (SquirrelJME!gcc)
list(APPEND SQUIRRELJME_SYSTEM_MAP
	"linux!linux"
	"linux!linux-gnu"
	"linux!linux-gnueabi"
	"linux!linux-gnueabihf"
	"linux!linux-gnuabi64"
	"windows!w64-mingw32"
	"windows!w64-mingw32ucrt"
	"windows!win16"
	"windows!win32"
	"windows!win64"
	"beos!beos"
	"bsd!bsd"
	"bsd!openbsd"
	"bsd!freebsd"
	"bsd!netbsd"
	"cygwin!cygwin"
	"cygwin!msys2"
	"dos!dos"
	"dos!freedos"
	"emscripten!emscripten"
	"macosx!macosx"
	"macosx!darwin"
	"3ds!3ds")

# Unmap from mapping fields
function(squirreljme_unmap destVar index source)
	# Convert to actual list
	string(REPLACE "!" ";" sourceList "${source}")

	# Extract the index
	list(GET sourceList ${index} destResult)

	# Return it
	set(${destVar} "${destResult}" PARENT_SCOPE)
endfunction()
