# dawn

Fast start for a Clojure + Pedestal + Vase + Fern project

Fork this to your project, then fire up a Codespace to get started.

You'll probably want to change "dawn" to your project's name. Be sure to make
the same change in `deps.edn`, `shadow-cljs.edn` and the various folder names.

## Vendored in this starter

- [Vase](https://github.com/cognitect-labs/vase)
- [Fern](https://github.com/cognitect-labs/fern)
- [Pedestal](https://github.com/pedestal/)
- [Datomic Pro](https://www.datomic.com) client
- [Test Runner](https://github.com/cognitect-labs/test-runner)
- [Friendly Repl](https://gitlab.com/mtnygard/frenpl)
- [Shadow-cljs Browser Quickstart](https://github.com/shadow-cljs/quickstart-browser)

## Starting a REPL with VS Code or Codespaces

These instructions are for a codespace using VS Code (either in a browser or running locally with a remote connection).

To get a Clojure REPL:

0. Configure your .m2/settings.xml for access to download Datomic. (See below for instructions.)
1. Click the nREPL badge in the status bar
2. Select the "jack in" option
3. For project type, select "Clojure CLI"
4. Use the `:dev` alias

To get a CLJS REPL:

1. Click the nREPL badge in the status bar.
2. Select the "jack in" option
3. For project type, select "shadow-cljs"
4. Use `:app` for the build to start

Shadow-cljs will run an HTTP server at http://localhost:8020. To access this
port, select the "Remote Explorer" tool from the left-hand navigation in your VS
Code window. Under "Codespace Details" you will see "Forwarded Ports". Click
"Port: 8020" to open a new tab connected to the shadow-cljs server.

 To change this port number, modify `forwardPorts` in
`.devcontainer/devcontainer.json` and the value at `[:builds :app :dev-tools]`
in `shadow-cljs.edn`. After making this change, you must commit it, push it to
your fork of Dawn, and start a new codespace.

## Starting a REPL from command line

To get a Clojure REPL:

0. Configure your .m2/settings.xml for access to download
   Datomic. (See below for instructions.)
1. In a terminal, run `clj -A:dev`

To get a CLJS REPL:

0. Ensure you have [npx installed](https://www.npmjs.com/package/npx#install)
1. In a terminal, run `npx shadow-cljs watch app`

Your app will be on `http://localhost:8020` and a Shadow CLJS
dashboard (for monitoring builds) will be on `http://localhost:9630`

## Aliases

### Clojure

- :dev - Start a friendly REPL
- :test - Run all tests

### Shadow-CLJS

- :app - Build the main application,

## Fetching Datomic Client Jar

Vase depends on Datomic. You will need to set up `~/.clojure/deps.edn` and `~/.m2/settings.xml` according
to the instructions at [my.datomic.com](https://my.datomic.com) in order to
fetch the Datomic Pro jar file from `repo.datomic.com`.

Codespaces users will want to look at [setting up personal
dotfiles](https://docs.github.com/en/github/developing-online-with-codespaces/personalizing-codespaces-for-your-account)
to make that easier.

If you do not want to use Datomic, remove both `com.cognitect/vase` and
`com.datomic/datomic-pro` from deps.edn.
