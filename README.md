sso-session-link-api
===

This service is a fossil, and always returns a 500 with a response payload of "This service has been deprecated, please remove calls to it. There is no alternative." We are unable to decommission this as the DASS service still calls it, yet this service has been returning to DASS a 500 for several years, so clearly DASS are not using the output of this call. As they are still a subscriber to this API, the API Platform will not allow us to decommission this service.
(Also see the newer/simpler [sso-session-api](https://github.com/hmrc/sso-session-api))

## POST /sso/ssoin/sessionInfo

Always returns a 500. Do not use. This endpoint is no longer in use. The response payload will always be "This service has been deprecated, please remove calls to it. There is no alternative."

## Running the tests

```shell
sbt clean compile coverage test it/test coverageReport
```