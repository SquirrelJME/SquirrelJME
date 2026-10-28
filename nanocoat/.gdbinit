# For this to be loaded, there needs to be a .gdbinit in HOME with:
# set auto-load local-gdbinit on
# add-auto-load-safe-path /

# The data directory needs to be changed
# > Python scripts used by GDB should be installed in data-directory/python,
# > where data-directory is the data directory as determined at GDB startup
# > (see GDB Data Files). This directory, known as the python directory, is
# > automatically added to the Python Search Path in order to allow the
# > Python interpreter to locate all scripts installed at this location.

# Load in pretty printer
set python print-stack full
source ./assets/prettyprint/prettygdb.py

