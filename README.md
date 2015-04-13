# blockbuster

[![Build Status](https://travis-ci.org/JordanDoyle/blockbuster.svg?branch=master)](https://travis-ci.org/JordanDoyle/blockbuster) [![License](https://poser.pugx.org/laravel/framework/license.svg)](http://github.com/jordandoyle/blockbuster)


My interpretation of steps 1 to 6 of part 3 of [University of Salford's Java Project](https://drive.google.com/file/d/0B1SXk11s15KuQkRfa2NJcmxyN00/view?usp=sharing).

To aid with quick and easy expandability of this application in the upcoming parts, the application parses the `data is` line provided by the professor and uses [reflection](http://en.wikipedia.org/wiki/Reflection_%28computer_programming%29#Java) to set and get the values of each `LibraryItem` instance based on the values from the `data is` line which means there is no need the hardcode where each value is positioned in the data file, again, allowing easy portability.

Each part of this application has been documented to show what each part is doing for amateur developers.
