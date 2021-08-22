Challenges in [Vase 1](https://github.com/cognitect-labs/vase):

- Vase is tightly coupled to Datomic. Both on-prem and cloud are
  supported, but either way jar files must be installed in the
  container by the user. This is friction every time a codespace is
  created.

- Mix of old EDN syntax and new Fern syntax is hard to
  reconcile. Leads to unclear relationships among namespaces. E.g.,
  "actions" applies for both Fern and EDN, but "literals" only applies
  to Fern. We have to keep data_readers.clj synchronized with
  namespace changes to preserve EDN round-tripping. But there is no
  Fern round-tripping. (Not sure such a thing is possible.)

- Vase allows multiple EDN configs to be concatenated. (Technically
  this is a reduce with merge from left to right in the seq of
  configs.) Fern configs cannot be concatenated or merged.

- EDN version assumes an external caller creates the Pedestal server
  map. The convenience "main" function does this, but with a different
  configuration mechanism than an EDN caller would use.

- The Specs are hard to keep in sync with both syntaxes. These specs
  were supposed to reflect the "pure data" interface that both Fern
  and EDN would target. However, as mentioned above, EDN and Fern
  don't match in terms of "level" or "scope" of the data. The EDN
  format only addresses the routes and norms (schema) while the Fern
  format also encompasses the server setup.

- Vase allows extension to use other back ends, but to date not one
  person in the world has ever exercised this extension. This suggests
  both that Vase has a small user base (known to be true!) and that
  extension is too hard (also known to be true!) Perhaps if it were
  easier to use Vase in different contexts, we would see more
  users. More users would lead to more extensions. Then again, more
  extensions would lead to more users.

- There is no way to support "configuration overrides" as one would
  need for e.g., different credentials in different environments.

- It is awkward to pass results from one action to another.

- It is awkward to run multiple queries or multiple transactions.
