# oauth-sample

A sample web app using public Oauth to demo how to use Ouath2 in Clojure with Github as the authority.

## Usage

- Register you app with Github
- Set CLIENT_ID and CLIENT_SECRET environment variables as per the values given during Github registration
- Run 'lein ring server' and navigate to browser page, usually http://localhost:3000

Currently there is a single thread through authorizing, to getting
an authorization code to getting a token and then using it to get User information

Contact: keith_mantell@uk.ibm.com

## License

Copyright © 2018 IBM

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.