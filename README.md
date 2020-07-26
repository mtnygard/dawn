# dawn

Fast start for a Clojure + Pedestal + Vase + Fern project

Fork this to your project, then fire up a Codespace to get started.

You'll probably want to change "dawn" to your project's name. Be sure to make
the same change in `deps.edn`.

## Included in this starter

- [Vase](https://github.com/cognitect-labs/vase)
- [Fern](https://github.com/cognitect-labs/fern)
- [Pedestal](https://github.com/pedestal/)
- [Datomic Pro](https://www.datomic.com) client
- [Test Runner](https://github.com/cognitect-labs/test-runner)
- [Friendly Repl](https://gitlab.com/mtnygard/frenpl)

## Aliases

- :dev - Start a friendly REPL
- :test - Run all tests

## Fetching Datomic Client Jar

You will need to set up `~/.clojure/deps.edn` and `~/.m2/settings.xml` according
to the instructions at [my.datomic.com](https://my.datomic.com) in order to
fetch the Datomic Pro jar file from `repo.datomic.com`.

Codespaces users will want to look at [setting up personal
dotfiles](https://docs.github.com/en/github/developing-online-with-codespaces/personalizing-codespaces-for-your-account)
to make that easier.