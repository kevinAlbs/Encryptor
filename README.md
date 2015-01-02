Template Encryptor
==================

An implementation of huffman encoding and (naive/insecure) encryption of password data. This is meant to be a starting point, and is structured so extending with a custom encryption algorithm is easy. I plan to implement more sophisticated encryption classes in the future as examples.

Usage
-----

The CLI version can be run from Runner in the encryption package.

To encrypt a file:
`java Runner --action encrypt --from-file corpus.txt --to-file encrypted_corpus.txt --key password`

To decrypt a file (and print to stdout):
`java Runner --action decrypt --from-file encrypted_corpus.txt --key password`

The GUI can be run from the GUI class in the gui package.

Current Status
--------------

The encryption algorithm simply copies the user supplied "master password" to the length of the encrypted message and XORs with the message. This is blatantly insecure since a [one time pad](http://en.wikipedia.org/wiki/One-time_pad) should never be used more than once. By repeating the password, the one-time pad is almost always used more than once, unless the password is long enough. Additionally, the tree signature of the huffman tree is not encrypted in file. This can be easily resolved by placing the tree signature in a separate file and encoding it the same way the main message is encoded.

The current GUI is a very simple text editor. The main point is to have a quick way to lookup passwords and account information locally.

Upcoming features/fixes
------------------------
- revision history, find ability, and other keyboard shortcuts (ctrl-z, ctrl-f, ctrl-s, etc.)
- visual feedback of IO errors (not println's)
- single letter bug (fails to encrypt if file is one letter)
- more sophisticated encryption class
- improve performance of string manipulation with stringbuilder
- either support for or explicit error on non-ascii characters