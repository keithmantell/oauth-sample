# oauth-sample

A sample web app using Oauth to demo how to use Oauth2 in Clojure.

It has been tested with Github and with a private server as the Oauth
authorization server.

## Warnings
- This is a sample which worked at the end of November 2018
- It was mainly a vehicle for my learning:  raising my experience in
  Clojure, adding a basic knowledge of Oauth.  I could not make other
  libraries work but that coukld be down to me.  I have a todo about
  revisiting say ring-oauth now I understand more.
- My code currently hardcodes the grant-type as "authorization_code".
  So far bothttest servers have allowed this - one insisted it was
  set, the other (github) accepted it.


## Github Oauth

 The main Github Oauth page is here:
 https://developer.github.com/apps/building-oauth-apps/
 and is an excellent resource.

## Usage

- Register you app with Github https://developer.github.com/apps/building-oauth-apps/
- Set environment variables:
 -- CLIENT_ID and  CLIENT_SECRET as per the values given during
 registration step
 -- AUTHORIZATION_ENDPOINT for the authorization server (see example
 set-variables file)
 -- TOKEN_ENDPOINT to exchange the authorization code for tokens ( as
 per the Oauth Server setup)
 -- HOST_URL for your website (e.g. https://localhost:3000 - this is
 also set during registration)
 -- REDIRECT_URI where you code handles the successful authentication
 return ( again set with the Oauth server during registration)
 -- SCOPE : what resources access is being granted.  This varies by
 Oauth server.

- Run 'lein ring server' and navigate to browser page, usually http://localhost:3000

Currently there is a single thread through authorizing, to getting
an authorization code to getting a token and then using it to get User information

Contact: keithmantell@gmail.com

## License

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
