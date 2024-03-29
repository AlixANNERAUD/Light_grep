<h1 align="center">Light grep</h1>

## Description

This is a simple implementation of the grep command using JavaCC. It is a simple command line tool that allows you to search for a pattern in a file.

⟨er⟩ ::= ⟨erc⟩ ( '|' ⟨erc⟩ )*
⟨erc⟩ ::= ⟨erb⟩ ( ⟨erb⟩ )*
⟨erb⟩ ::= ⟨ere⟩ ( '*' | '?' | '+' )?
⟨ere⟩ ::= '(' ⟨er⟩ ')' | ⟨lettre⟩ | '.' | '[' ⟨ens-lettres⟩ ']' | '[' '^' ⟨ens-lettres⟩ ']'
⟨ens-lettres⟩ ::= ⟨element-ens-lettres⟩ ( ⟨element-ens-lettres⟩ )*
⟨element-ens-lettre⟩ ::= ⟨lettre⟩ ( '-' ⟨lettre⟩ )?
⟨lettre⟩ ::= alphanumeric-characters | '\' escaped-characters