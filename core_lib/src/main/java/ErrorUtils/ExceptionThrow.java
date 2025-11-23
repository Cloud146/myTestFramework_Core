package ErrorUtils;

public final  class ExceptionThrow {

    private ExceptionThrow() {}

    public static void elementActionException(String action, String selector, Runnable call) {
        try {
            call.run();
        } catch (Throwable t) {
            if (t instanceof FrameworkException) {
                throw t;
            }
            ErrorBuilder.forAction(action)
                    .selector(selector)
                    .causedBy(t)
                    .message("Не удалось выполнить действие '" + action + "' с элементом: " + selector)
                    .raise();
        }
    }

}
