# 8085 Microprocessor Simulator — Project Structure

## Why this layout?

Maven expects a fixed directory convention so it knows where to find things
without you configuring paths manually (this is called "convention over
configuration" — the opposite of a Makefile where you specify every path).

```
sim85/
├── pom.xml                          # Project manifest: identity, dependencies, build/run config
├── README.md
└── src/
    ├── main/
    │   ├── java/sim85/
    │   │   ├── core/                # Registers, Flags, Memory, CPU — no GUI dependency, unit-testable
    │   │   ├── instructions/        # Instruction interface + all instruction implementations
    │   │   ├── assembler/           # Assembler, SymbolTable (two-pass assembly)
    │   │   └── gui/                 # SimulatorApp (JavaFX entry point), panels/controllers
    │   └── resources/               # Non-code files (e.g. FXML layouts, icons) — optional
    └── test/
        └── java/sim85/              # JUnit tests, mirrors main/java package structure
```

**Key convention:** `src/main/java` is your actual source, `src/test/java` is
tests, and the package path (`sim85/...`) must physically match
the `package` declaration inside each `.java` file. Maven/the compiler
enforce this — it's not optional like it might be in other languages.

## Why these packages specifically

- `core` has **zero JavaFX imports** — this is intentional. It means you can
  write and run JUnit tests against `CPU`/`Memory`/`Registers` from the
  command line with no GUI involved, which is both good practice and useful
  for demoing correctness independent of the UI.
- `assembler` also has no dependency on `core`'s execution logic — it only
  needs instruction sizes/opcodes to assemble, not how they run.
- `gui` is the only package that imports both `core` and `assembler` and
  wires them together (this is the Model-View-Controller split mentioned
  earlier).

## Commands you'll actually use

```bash
mvn compile        # compiles src/main/java -> target/classes
mvn test           # runs JUnit tests in src/test/java
mvn javafx:run     # runs the JavaFX app (configured via javafx-maven-plugin in pom.xml)
mvn package        # builds a .jar into target/
```

## What's already scaffolded

- `pom.xml` — configured with JavaFX 21 dependencies, JUnit 5 for testing,
  and the `javafx-maven-plugin` so `mvn javafx:run` works out of the box.
- `SimulatorApp.java` — minimal working JavaFX window, just to confirm your
  setup runs end-to-end before you write any simulator logic.

## Next steps (not yet created)

Empty packages (`core`, `instructions`, `assembler`) have no files yet —
add `Registers.java`, `Memory.java`, `Flags.java` to `core` first, since
they have no dependencies and let you validate the rest of the toolchain
incrementally.
