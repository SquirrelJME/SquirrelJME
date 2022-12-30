# ---------------------------------------------------------------------------
# Multi-Phasic Applications: SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the GNU General Public License v3+, or later.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: IEEE1275 Specific options

# Enable assembly for IEEE1275
enable_language(ASM)

# Define used to specify this system
add_compile_definitions(SJME_SYSTEM_IEEE1275=1)

# Disable unit testing, we cannot reliably test on this platform
set(SQUIRRELJME_ALLOW_TESTING NO)

# Use bundled LibStdC
set(SQUIRRELJME_BUNDLED_STDC YES)

# Options needed for IEEE1275 that affects everything, not for ASM)
# Do not use built-ins for methods
add_compile_options($<$<COMPILE_LANGUAGE:C,CXX>:-fno-builtin-fputs>)
add_compile_options($<$<COMPILE_LANGUAGE:C,CXX>:-fno-builtin-fputc>)
add_compile_options($<$<COMPILE_LANGUAGE:C,CXX>:-fno-builtin-puts>)
add_compile_options($<$<COMPILE_LANGUAGE:C,CXX>:-fno-builtin-putc>)
add_compile_options($<$<COMPILE_LANGUAGE:C,CXX>:-fno-builtin-strlen>)
add_compile_options($<$<COMPILE_LANGUAGE:C,CXX>:-fno-builtin-memset>)
add_compile_options($<$<COMPILE_LANGUAGE:C,CXX>:-fno-builtin-memcpy>)
add_compile_options($<$<COMPILE_LANGUAGE:C,CXX>:-fno-builtin-memmove>)

# Freestanding environment
add_compile_options($<$<COMPILE_LANGUAGE:C,CXX>:-ffreestanding>)

# Put every function within its own section
add_compile_options($<$<COMPILE_LANGUAGE:C,CXX>:-ffunction-sections>)

# Disable exceptions and unwinding
add_compile_options($<$<COMPILE_LANGUAGE:C,CXX>:-fno-exceptions>)
add_compile_options($<$<COMPILE_LANGUAGE:C,CXX>:-fno-unwind-tables>)
add_compile_options($<$<COMPILE_LANGUAGE:C,CXX>:-fno-asynchronous-unwind-tables>)