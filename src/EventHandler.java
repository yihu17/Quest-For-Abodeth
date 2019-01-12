/**
 * Custom handler for all events that occur and can be bound to
 * specific objects.
 * By making the class abstract it cannot be instantiated normally but can
 * be used as anonymous classes.
 */
public abstract class EventHandler {
    /**
     * Overridden by the user
     */
    public abstract void run();
}
