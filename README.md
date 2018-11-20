# oauth-sample

A sample web app using public Oauth to demo how to use Ouath2 in
Clojure with Github as the authority.

## Setup

If using lein ring server see these setup instructions

## Usage

- Register you app with Github https://developer.github.com/apps/building-oauth-apps/
- Set CLIENT_ID and CLIENT_SECRET environment variables as per the values given during Github registration
- Run 'lein ring server' and navigate to browser page, usually http://localhost:3000

Currently there is a single thread through authorizing, to getting
an authorization code to getting a token and then using it to get User information

Contact: keith_mantell@uk.ibm.com

## License

Copyright © 2018 IBM

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
