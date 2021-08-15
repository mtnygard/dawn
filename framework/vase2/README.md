# Vase 2: Extensible data-driven microservices

This library provides a data-driven interface to define
microservices. It can be used for rapid prototyping or for production
level applications.

## Rationale

Most microservices shuttle data into and out of a database. They apply
a small amount of logic along the way: data validation on the way in,
various joins and formatting on the way out. It should be as easy as
writing the API specification, then letting the system figure out the
rest.

Instead, we see codebases with a high level of duplication. Every
service goes through the same motions to set up route definitions,
database connections, data filters, authentication, and so on.

Vase2 offers a way to write a data file that describes your
service. Inside that data file you specify your routes, your database
schema, and logic (where needed) to connect them. In the easiest
example, everything derives from your schema.

## Concepts

Vase2 creates [Pedestal](http://pedestal.io) services. You don't need
to be a Pedestal expert to use Vase2. However, if you do know how
Pedestal works, then you can extend Vase2 by using Pedestal
interceptors to add richer functionality by referring to code in
Clojure files.

Vase2 creates [OpenAPI](https://swagger.io/specification/)
specifications. These can be exposed at a route of your choice.

## Fern

The input format for Vase2 is
[Fern](https://github.com/cognitect-labs/fern).

### Cross-cutting concerns

Logging, metrics, and tracing is provided by default using Pedestal's
[logging
functions](http://pedestal.io/api/pedestal.log/index.html). Every
route collects metrics on invocation and error counts.

Authorization is not currently handled by Vase2. However, you can add
authorization checks to routes using any library that supports
Pedestal interceptors. ([Buddy
auth](https://github.com/funcool/buddy-auth/) is a reasonable choice
here.) This is an area that needs more design and development.


### Initialization

There are two "magic" key names that Vase2 will look for in the input
file:

- `vase2/plugins` - A list of Clojure namespaces to load before
  processing the rest of the input. This is the main way to extend
  Vase2 (see [Plugins](#Plugins) below.)
- `vase2/service` - The value for this key should be a "Vase2 Service"
  map. See [Service Map](#ServiceMap) for the specification of this
  map.

Vase2 will reach every other value by traversing references from the
service map. Values that are not reachable will not be
evaluated... which also means any side effects of evaluating those
(such as connecting to a database) will not occur if the value is not
reachable from the Service Map.

(Note to self: a diagram would help here.)

### Service Map

The service map has these keys:

- `:on-startup` - a seq of interceptors to be run when the whole
  service starts up
- `:apis` - a seq of Api Maps

The `on-startup` seq is for things that need to be done once when
starting your service. Vase2 uses Pedestal
[interceptors](http://pedestal.io/reference/interceptors) for
these. Instead of a [context
map](http://pedestal.io/reference/context-map), these interceptors
receive the whole Pedestal [service
map](http://pedestal.io/reference/service-map). This allows them to
define or alter routes, add extra data, or inject configuration. If
any of these interceptors return an error response, then service
startup aborts with a log message.

### API Map

Each API map has these keys:

- `:on-startup` - a seq of interceptors to be run when the API is
  initialized. (This is "nested" within the overall service startup.)
- `:on-shutdown` - a seq of interceptors to be run when the API is
  shut down. Don't assume these will actually run... the process could
  be killed abruptly. These are just for graceful shutdown.
- `:base` - A string with a path fragment. The API's routes will be
  "nested" under this path.
- `:routes` - A seq of routes (see [Route map](#RouteMap).)
- `:swagger/info` - An optional value with metadata needed for an
  [Info Object](https://swagger.io/specification/#info-object).

#### Providing Swagger Info

The info map for an API sits at the key `:swagger/info`. Its value is
a map with the keys that correspond to the Info Object's fixed fields,
but each key is prepended with `swagger.info`. As of v3.0.3, the fixed
fields key names are:

- `:swagger.info/title`
- `:swagger.info/description`
- `:swagger.info/termsOfService`
- `:swagger.info/contact`
- `:swagger.info/license`
- `:swagger.info/version`

At least two of those keys have further nested values. In each case,
the "object type name" moves into the namespace portion of the key,
while the field name inside that object type goes after the slash. So,
for example, the fields of the contact objects are named like:

- `:swagger.info.contact/name`
- `:swagger.info.contact/url`

And so on.

### Swagger APIs from Vase2 APIs

To expose a Swagger API specification for a Vase2 API, define an
ordinary route. It should have exactly one interceptor defined in it,
which would look like this:

```
{vase/plugins [vase2.api.swagger]
 vase/service (fern/lit vase2/service {:apis [@example/v1]})
 example/v1   (fern/lit vase2/api {:base "v1"
                                   :routes @example.v1/routes})
 example.v1/routes [["/swagger.json" :get @vase2.api.swagger/spec]]}
```

(Note, the '@' symbols mean "substitute the value of this name
here". See [Fern's
usage](https://github.com/cognitect-labs/fern#usage) for how that
works.)

The handler for "/swagger.json" is a reference to an interceptor that
isn't defined anywhere in this map. That's because it is _injected_ to
the environment by the vase2.api.swagger plugin.
