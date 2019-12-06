```
  _  __           __ _   ____  _                       ____       _              _       _           
 | |/ /_ __ __ _ / _| |_|  _ \(_)_ __  _ __   ___ _ __/ ___|  ___| |__   ___  __| |_   _| | ___ _ __ 
 | ' /| '__/ _` | |_| __| | | | | '_ \| '_ \ / _ \ '__\___ \ / __| '_ \ / _ \/ _` | | | | |/ _ \ '__|
 | . \| | | (_| |  _| |_| |_| | | | | | | | |  __/ |   ___) | (__| | | |  __/ (_| | |_| | |  __/ |   
 |_|\_\_|  \__,_|_|  \__|____/|_|_| |_|_| |_|\___|_|  |____/ \___|_| |_|\___|\__,_|\__,_|_|\___|_|   
                                                                                                     
```
(Note: 100% cheese-free!)

Group Members:
- Kaitlin de Chastelain Finnigan  ID: 30044556
- Matthew Allwright               ID: 30037812
- Denis Shevchenko                ID: 30020133
- Julio Agostini                  ID: 30049674
- Stefan Jovanovic                ID: 10135783

## Compiling
Compiling the code is made easy through the use of the `COMPILE.sh` script.
This script will generate java binaries in a new local fonder, `bin/`,
where the run script will access them.

## Running
After compiling the code, the `RUN.sh` script will simplify the running of the scheduler.
The script takes 9 arguments. These are (in order):
- Path to the input file
- pen_coursemin
- pen_labmin
- pen_notpaired
- pen_section
- w_minfilled
- w_pref
- w_pair
- w_secdiff

The script will run the scheduler and print the output to your console,
however the output will also be available in the generated `output.txt` file.
