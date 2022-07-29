package io.humainary.showcase.counters;

import io.humainary.counters.Counters;
import io.humainary.counters.Counters.Context;
import io.humainary.counters.Counters.Counter;

import static io.humainary.substrates.Substrates.name;

public final class Counting {

  private static final Context CONTEXT =
    Counters.context ();

  private static final Counter COUNTER =
    CONTEXT.counter (
      name (
        Counting.class
      )
    );

  public static void main (
    final String[] args
  ) {

    final var closure =
      new long[1];

    try (
      final var ignore =
        CONTEXT.subscribe (
          ( reference, registrar ) ->
            registrar.register (
              event ->
                closure[0] +=
                  event.emittance ()
            )
        ) ) {

      for ( int i = 0; i < 100; i++ ) {
        COUNTER.inc ();
      }

      assert closure[0] == 100L;

    }


  }

}
