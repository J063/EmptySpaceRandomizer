# Empty disk space randomizer

Created in order to clear unused disk space (and determine writing speed)

## Description

The script allocates 1GB of memory to generate a byte[] full of random bytes
which is populated upon the starting of the script and re-used over and over again
to write to two files; 'smallFile' and 'bigFile'

The contents is dumped once in the 1GB file 'smallFile' and then successively
dumped into 'bigFile' until the disk is full or the specified lowerLimit has
been reached. The file 'smallFile' exists in order to quickly delete and fix
system unresponsiveness due to lack of disk space.

## Getting Started

Typically I have something like this in my ~/.profile:

```
alias cldsk="cd $HOME/Projecten/EmptySpaceRandomizer && java Randomizer && cd"
```

You can also run the program manually as follows:
```
java Randomizer 130
```

Where the randomizer will fill the disk until the optional lower limit of 130GB has been reached

### Prerequisites

- >= java 1.7
- > 2GB of hard drive space
- >1GB of memory

## Contributing
1. Fork it!
2. Create your feature branch: `git checkout -b my-new-feature`
3. Commit your changes: `git commit -am 'Add some feature'`
4. Push to the branch: `git push origin my-new-feature`
5. Submit a pull request :D

## Versioning

don't care

## Authors

* **J63** - *Initial work* - [J063](https://github.com/J063)

## License
This particular set of settings is subject to the [MIT License](https://opensource.org/licenses/MIT).
