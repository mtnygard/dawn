# Design Objectives

## Principles

1. Simple below, easy above.
2. Prefer fewer, composable mechanisms.
3. Prefer adding an orthogonal mechanism over adding complexity in a mechanism.
4. Create simplicity by removing special cases.
5. Data is the common language.
6. When in conflict, prefer simplicity over reuse.
7. Composition is the preferred means of reuse.
8. Expand the volume of pure functional code.
9.  Reduce the surface area of imperative, side-effecting code.

## Be production-ready

Vase2 services should be suitable for use in a production
environment. That means they are secure, operable, reliable, and
performant.

A production ready service must be configurable for different
environments (dev, test, staging, production, etc.) with different
credentials, and other settings. This should not be done by the
service developer and must be "layered in".

Vase2 services should be [12 Factor](https://12factor.net/) apps.

## Easy to Use

A user working with Vase2 should be able to reach "Hello, world!" in a
few minutes. It should be easy to keep the service working as the user
extends it. When something is wrong with the service's definition,
Vase2 should "fail safe" by alerting the user to the problem as early,
clearly, and safely as possible. This means it is better to abort with
a helpful error message rather than allow deployment of faulty
configuration.

## Embeddable

Even though Vase2 is being built inside
[Dawn](https://github.com/mtnygard/dawn) it should be usable as a jar
file or Git SHA dependency by any Clojure program.

### Embeddable in Java (Stretch)

Vase2 should offer a (perhaps minimal) Java API to start and stop a
service, given a URL to a Fern configuration.

## Extensible via Clojure

Extensions should be easy to contribute.

There should be a clear API namespace or namespaces which is stable
and well-documented.

There will be two means of extension:

1. Pedestal interceptors. An extension can provide functions to create
   interceptors which can be placed in an interceptor chain for any
   Vase2 service.
2. Fern literals. Often used as a way to instantiate the
   interceptors. Literals are exposed to allow Fern descriptions to
   configure interceptors or other objects.

## Simple Core

Vase2 will expose a limited set of interceptors in the core. Mostly
the built-in Pedestal interceptors (e.g., "respond with a JSON body").

Vase2 will come with extensions for common interceptors. Basic HTTP
responses (including vending a static file). Conforming inputs to a
spec (and responding with an error if it doesn't conform.) These are
useful in their own right, but also make sure it is easy to create
extensions from the start.

## High performance

Vase2 interceptors are created from data files. This could mean that
we do a lot of interpretation at request-handling time. But since the
data files don't change from one request to the next, that would be
inefficient.

Instead, Vase2 (like Vase1) will use Clojure's ability to create code
from data. When starting up a service, Vase2 will generate, then eval,
the code that will be run at request time. That adds complexity for
extension authors, so making this easy to understand and still fast is
a key design objective.

## Initialization

Vase2 needs to consider two phases: initialization and request
time. Both can be addressed with interceptors, if we think about
different kinds of [Context
map](http://pedestal.io/reference/context-map).

## Composability

Interceptors chain, but do not compose. Route tables can be
concatenated, but also do not compose. How can we go about building
"bigger pieces" by composing smaller pieces?

For example, REST style resources over a database would comprise four
or five routes with similar names (derived from the resource name),
where each route invokes actions to manipulate a database. It seems
obvious that we should have a way to create a "resource" construct
that would take a resource name, API schema, and DB schema and expand
into the discrete routes and interceptors needed.

Such compositionality would allow us to support
[Swagger](https://swagger.io/) API definitions.

Fern literals can provide this. But that's less like composing big
blocks out of small blocks, and more like throwing code at the
problem. Perhaps Fern could be extended to support literals that emit
other literals.
