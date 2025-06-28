# experimental

Scala and ScalaJS experiment with frontend / backend stacks.

We want to test out:

- Laminar and Kyo integration
- tapir and sttp using the scalajs stub server to mock frontend work.

To start development, first start sbt, then

```bash
sbt # Start sbt shell
~frontend/fastLinkJS # Start frontend fastLinkJS task that compiles
```

Then you can start vite:

```bash
npm run dev
```

For now we have separated out the events from the ui. We want to base everything on EventBus, and derive signals from those events in each view.

We want to utilize tapir and its backend stub in the frontend to mock out the backend for an incredible, superfast development loop cycle.

The endpoints should be shared between the frontend and the backend, and then we have two server implementations of those endpoints, one for the stubbed one in the frontend and one for the actual backend. We wil look into routing in the frontend if that can be achieved somehow as well.

## TODOS

1. See if there are ways to write unit tests etc.
2. Fix event model so it works according to the domain
3. Implement simple crud
4. Are there any magical things we can use from kyo for a simple backend?
5. Try to integrate kyo?

## Done

- Implement the TODO api for fetching todos with a mocked tapir backend using sttp.
- Clean up everything, make boundary contexts
- Integrate kyo Env and Layer to be able to easily create fakes that also can be utilized during development cycle.

