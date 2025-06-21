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

1. Implement the TODO api for fetching todos with a mocked tapir backing using sttp.
2. Integrate kyo Env and Layer to be able to easily create fakes that also can be utilized during development cycle.
3. See if we can do some test miracle as well with this

