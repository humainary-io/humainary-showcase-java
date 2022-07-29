package io.humainary.showcase.observers;

import io.humainary.stacks.Stacks;
import io.humainary.stacks.Stacks.Site;
import io.humainary.substrates.Substrates.Event;
import io.humainary.substrates.Substrates.Extent;

import static io.humainary.observers.Observers.*;
import static io.humainary.substrates.Substrates.Environment.EMPTY;
import static io.humainary.substrates.Substrates.name;

public final class StackDepth {

  record Tally(
    int count,
    int total
  ) {}

  public static void main (
    final String[] args
  ) {

    final var sites =
      Stacks.context ();

    try (
      final var observers =
        context (
          sites.source (
            Extent::depth
          ),
          Event::emittance,
          optic (
            bootstrap ( new Tally ( 0, 0 ) ),
            lens (),
            operant (
              ( tally, depth ) ->
                new Tally (
                  tally.count + 1,
                  tally.total + depth
                )
            )
          ),
          EMPTY
        )
    ) {

      final var site =
        sites.site (
          name (
            "site#1"
          )
        );

      try (
        final var ignored =
          observers.consume (
            event -> {

              final Tally tally =
                event.emittance ();

              System.out.printf (
                "%s called with avg depth %d%n",
                event.emitter ().name (),
                tally.count != 0 ? tally.total / tally.count : 0
              );

            }
          )
      ) {

        call (
          site,
          100
        );

        call (
          site,
          12
        );

        observers
          .observer ( site.reference ().name () )
          .observe ();

      }

    }


  }

  private static void call (
    final Site site,
    final int countDown
  ) {

    if ( countDown == 0 ) {

      site.emit ();

    } else {

      call (
        site,
        countDown - 1
      );

    }


  }

}
