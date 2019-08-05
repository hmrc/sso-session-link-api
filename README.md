sso-session-link-api
===

May be used with [sso-session-api](github.com/hmrc/sso-session-api) to SSO users from the API platform to the web

## POST /sso/ssoin/sessionInfo

_NOTE: requires MDTP bearer token_
#### Example request body
```json
{
    "id_token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJ0ZXN0Iiwic3ViIjoiRXh0LWYwYzRmZGIxLTkwMGMtNGVkNi05Mzg2LTYwZDY4ZDIwZWViNCIsImF1ZCI6WyJkZGQ4ZjNhMi1iZTNjLTQ3MDAtYmU4YS04MTMxZDczOGIzMDciXSwiZXhwIjoxNDk0NTA2MTQ1ODg4LCJpYXQiOjE0OTQ0OTE3NDU4ODh9.nieB3jw06H1Z9CrawcHO5WXXXEovle7dRYTYBq9UsRs",
    "mdtpSessionId": "asdas-2321-asdad-fdsdfsd",
    "portalSessionId": "asdas-2321-asdad-fdsdfsd"
}
```

#### Example response
```json
{
    "_links": {
        "browser": {
            "href": "/sso/ssoin/5a003c6352000072009a711e"
        }
    }
}
```
