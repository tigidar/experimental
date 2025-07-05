# experimental

Scala and ScalaJS experiment with frontend / backend stacks.

We want to test out:

- Laminar and Kyo integration
- tapir and sttp using the scalajs stub server to mock frontend work.

## Development

The development environment use `sbt` and in the future `mill` scala build system and `vite` for frontend development.

### Scala build

To start development, first start sbt, then compile the frontend via fastLinkJS:

```bash
sbt # Start sbt shell
~frontend/fastLinkJS # Start frontend fastLinkJS task that compiles
```

You have to connect to the sbt language server, because we have factored out the dependencies into a json file, `deps.json`. This is also experimental, since we want to combine `mill` and `sbt` in the end.

### frontend build (vite)

Then you can start vite:

```bash
npm run dev
```

For now we have separated out the events from the ui. We want to base everything on EventBus, and derive signals from those events in each view.

We want to utilize tapir and its backend stub in the frontend to mock out the backend for an incredible, superfast development loop cycle.

The endpoints should be shared between the frontend and the backend, and then we have two server implementations of those endpoints, one for the stubbed one in the frontend and one for the actual backend. We wil look into routing in the frontend if that can be achieved somehow as well.

## TODOS

0. Ensure common build settings / scala version on all modules
1. Move Frontend server stub out into its own module
2. Add server setup
3. Domain cleanup
4. automate the formatting, most effectively within the CI pipeline, find best practices
5. See if there are ways to write unit tests etc.
6. Fix event model so it works according to the domain
7. Implement simple crud
8. Are there any magical things we can use from kyo for a simple backend?


## Done

- modularize, putting common code in modules available for frontend and backend
- Kyo is integrated with frontend as a PoC
- Implement the TODO api for fetching todos with a mocked tapir backend using sttp.
- Clean up everything, make boundary contexts
- Integrate kyo Env and Layer to be able to easily create fakes that also can be utilized during development cycle.
